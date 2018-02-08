package top.ashman.wx.application.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ashman.wx.application.odd.OddFeignClient;
import top.ashman.wx.application.odd.task.TaskCommand;
import top.ashman.wx.application.odd.task.TaskDTO;
import top.ashman.wx.application.service.MessageService;
import top.ashman.wx.config.FeignHttpHeaderInterceptor;
import top.ashman.wx.config.property.WxProperties;
import top.ashman.wx.domain.model.message.Message;
import top.ashman.wx.domain.model.message.MessageRepository;
import top.ashman.wx.domain.model.message.ReplyMessage;
import top.ashman.wx.domain.model.message.XmlMessageAssembler;
import top.ashman.wx.infrastructure.util.WxConstants;
import top.ashman.wx.infrastructure.util.security.AESUtil;
import top.ashman.wx.infrastructure.util.security.SHA1Util;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

/**
 * @author singoasher
 * @date 2018/1/31
 */
@Service
public class MessageServiceImpl implements MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    private MessageRepository messageRepository;
    private OddFeignClient oddFeignClient;
    private WxProperties wxProperties;
    private ExecutorService executorService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository,
                              OddFeignClient oddFeignClient,
                              WxProperties wxProperties,
                              ExecutorService executorService) {
        this.messageRepository = messageRepository;
        this.oddFeignClient = oddFeignClient;
        this.wxProperties = wxProperties;
        this.executorService = executorService;
    }

    @Override
    public String handle(Message message) {
        LOGGER.info("Message Received After Assemble: {}", message.toString());
        Message messageToSave = Message.copy(message);
        executorService.execute(() -> save(messageToSave));
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        switch (wxProperties.getMode()) {
            case CLEAR:
                // 明文模式，仅处理明文消息
                return handleClearMessage(message, timestamp);
            case CIPHER:
                // 安全模式，仅处理加密消息
                return handleCipherMessage(message, timestamp);
            case COMPAT:
                // 兼容模式，优先处理加密消息
                if (Optional.ofNullable((message.getEncrypt())).isPresent()) {
                    return handleCipherMessage(message, timestamp);
                } else {
                    return handleClearMessage(message, timestamp);
                }
            default:
                return null;
        }
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    private String handleClearMessage(Message message, String timestamp) {
        String toUserName = message.getToUserName();
        String fromUserName = message.getFromUserName();
        String content = message.getContent();

        String replyContent = Optional.ofNullable(handleMessageContent(content, fromUserName)).orElse(null);

        String returnXmlMessage = XmlMessageAssembler.generateClearXml(fromUserName, toUserName, timestamp, replyContent);
        LOGGER.info("Clear Xml Message Return: {}", returnXmlMessage);
        return returnXmlMessage;
    }

    private String handleMessageContent(String content, String fromUserName) {
        return Optional.ofNullable(content).map(c -> {
            // 去除首尾多余空格
            final String trimmedContent = c.trim();

            // Hello
            for (String keyword : ReplyMessage.HELLO.keywords()) {
                if (trimmedContent.toUpperCase().equals(keyword)) {
                    return ReplyMessage.HELLO.normalReply();
                }
            }

            // 新建任务
            for (String keyword : ReplyMessage.CREATE_TASK.keywords()) {
                if (trimmedContent.length() < keyword.length()) {
                    continue;
                }

                if (trimmedContent.toUpperCase().equals(keyword)) {
                    return ReplyMessage.CREATE_TASK.abnormalReply();
                }

                if (trimmedContent.substring(0, keyword.length()).toUpperCase().equals(keyword)) {
                    executorService.execute(() -> {
                        FeignHttpHeaderInterceptor.USER_ID_THREAD_LOCAL.set(fromUserName);
                        oddFeignClient.create(TaskCommand.builder()
                                .content(trimmedContent.substring(keyword.length()).trim())
                                .build());
                    });
                    return ReplyMessage.CREATE_TASK.normalReply();
                }
            }

            // 任务列表
            for (String keyword : ReplyMessage.LIST_TASK.keywords()) {
                if (trimmedContent.toUpperCase().equals(keyword)) {
                    FeignHttpHeaderInterceptor.USER_ID_THREAD_LOCAL.set(fromUserName);
                    List<TaskDTO> taskDTOList = oddFeignClient.list();
                    StringBuilder stringBuilder = new StringBuilder();
                    return Optional.ofNullable(taskDTOList)
                            .map(list -> {
                                if (list.size() == 0) {
                                    return ReplyMessage.LIST_TASK.abnormalReply();
                                } else {
                                    list.forEach(t ->
                                            stringBuilder.append("\n").append(t.getCreatedTime())
                                                    .append("\n").append(t.getContent())
                                                    .append("\n"));
                                    return stringBuilder.toString();
                                }
                            })
                            .orElse(ReplyMessage.LIST_TASK.abnormalReply());
                }
            }

            // 列出所有过期任务
            for (String keyword : ReplyMessage.LIST_EXPIRED_TASK.keywords()) {
                if (trimmedContent.toUpperCase().equals(keyword)) {
                    return ReplyMessage.LIST_EXPIRED_TASK.abnormalReply();
                }
            }

            // 清空任务
            for (String keyword : ReplyMessage.REMOVE_ALL_TASK.keywords()) {
                if (trimmedContent.toUpperCase().equals(keyword)) {
                    return ReplyMessage.REMOVE_ALL_TASK.abnormalReply();
                }
            }

            // 删除所有过期任务
            for (String keyword : ReplyMessage.REMOVE_EXPIRED_TASK.keywords()) {
                if (trimmedContent.toUpperCase().equals(keyword)) {
                    FeignHttpHeaderInterceptor.USER_ID_THREAD_LOCAL.set(fromUserName);
                    executorService.execute(() -> oddFeignClient.removeAllExpired());
                    return ReplyMessage.REMOVE_EXPIRED_TASK.normalReply();
                }
            }

            // 激活所有过期任务
            for (String keyword : ReplyMessage.ACTIVE_TASK.keywords()) {
                if (trimmedContent.toUpperCase().equals(keyword)) {
                    FeignHttpHeaderInterceptor.USER_ID_THREAD_LOCAL.set(fromUserName);
                    executorService.execute(() -> oddFeignClient.activeAllExpired());
                    return ReplyMessage.ACTIVE_TASK.normalReply();
                }
            }

            return ReplyMessage.HELP.normalReply();
        }).orElse(null);
    }

    private String handleCipherMessage(Message message, String timestamp) {
        // 解密
        String encodingAESKey = wxProperties.getServer().getEncodingAESKey();
        Integer encodingAESKeyLength = wxProperties.getServer().getEncodingAESKeyLength();
        String appId = wxProperties.getAppId();
        String token = wxProperties.getServer().getToken();
        Message decryptedMessage = message.decrypt(encodingAESKey, encodingAESKeyLength, wxProperties.getAppId());
        LOGGER.info("Decrypted Message: {}", decryptedMessage.toString());

        // 进入明文处理流程
        String clearReturnXmlMessage = handleClearMessage(decryptedMessage, timestamp);

        // 明文处理结果判断
        if (WxConstants.SUCESS_MESSAGE.equals(clearReturnXmlMessage)) {
            LOGGER.info("Cipher Xml Message Return: {}", WxConstants.SUCESS_MESSAGE);
            return WxConstants.SUCESS_MESSAGE;
        }

        // 加密明文处理结果
        String nonce = AESUtil.generateNonce();
        String encrypt = AESUtil.encrypt(nonce, clearReturnXmlMessage, appId, encodingAESKey);
        String signature = SHA1Util.getSignature(token, timestamp, nonce, encrypt);

        String returnXmlMessage = XmlMessageAssembler.generateCipherXml(encrypt, signature, timestamp, nonce);
        LOGGER.info("Cipher Xml Message Return: {}", returnXmlMessage);
        return returnXmlMessage;
    }
}
