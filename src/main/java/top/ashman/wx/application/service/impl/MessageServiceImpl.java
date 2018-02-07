package top.ashman.wx.application.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ashman.wx.application.client.OddClient;
import top.ashman.wx.application.service.MessageService;
import top.ashman.wx.config.property.WxProperties;
import top.ashman.wx.domain.model.message.Message;
import top.ashman.wx.domain.model.message.MessageRepository;
import top.ashman.wx.domain.model.message.XmlMessageAssembler;
import top.ashman.wx.infrastructure.util.WxConstants;
import top.ashman.wx.infrastructure.util.security.AESUtil;
import top.ashman.wx.infrastructure.util.security.SHA1Util;

import java.time.Instant;
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
    private OddClient oddClient;
    private WxProperties wxProperties;
    private ExecutorService executorService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository,
                              OddClient oddClient,
                              WxProperties wxProperties,
                              ExecutorService executorService) {
        this.messageRepository = messageRepository;
        this.oddClient = oddClient;
        this.wxProperties = wxProperties;
        this.executorService = executorService;
    }

    @Override
    public String handle(Message message) {
        LOGGER.info("Message Received After Assemble: {}", message.toString());
        executorService.execute(() -> save(message));

        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        switch (wxProperties.getMode()) {
            case CLEAR:
                // 明文模式，仅处理明文消息
                return handleClearText(message, timestamp);
            case CIPHER:
                // 安全模式，仅处理加密消息
                return handleCipherText(message, timestamp);
            case COMPAT:
                // 兼容模式，优先处理加密消息
                if (Optional.ofNullable((message.getEncrypt())).isPresent()) {
                    return handleCipherText(message, timestamp);
                } else {
                    return handleClearText(message, timestamp);
                }
            default:
                return null;
        }
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    private String handleClearText(Message message, String timestamp) {
        String returnXmlMessage = handleMessage(message, timestamp);
        LOGGER.info("Clear Xml Message Return: {}", returnXmlMessage);
        return returnXmlMessage;
    }

    private String handleCipherText(Message message, String timestamp) {
        String encodingAESKey = wxProperties.getServer().getEncodingAESKey();
        Integer encodingAESKeyLength = wxProperties.getServer().getEncodingAESKeyLength();
        String appId = wxProperties.getAppId();
        String token = wxProperties.getServer().getToken();

        String clearText = handleMessage(
                message.decrypt(encodingAESKey, encodingAESKeyLength, wxProperties.getAppId()),
                timestamp);

        if (WxConstants.SUCESS_MESSAGE.equals(clearText)) {
            LOGGER.info("Cipher Xml Message Return: {}", WxConstants.SUCESS_MESSAGE);
            return WxConstants.SUCESS_MESSAGE;
        }

        String nonce = AESUtil.generateNonce();
        String encrypt = AESUtil.encrypt(nonce, clearText, appId, encodingAESKey);
        String signature = SHA1Util.getSignature(token, timestamp, nonce, encrypt);

        String returnXmlMessage = XmlMessageAssembler.generateCipherXml(encrypt, signature, timestamp, nonce);
        LOGGER.info("Cipher Xml Message Return: {}", returnXmlMessage);
        return returnXmlMessage;
    }

    private String handleMessage(Message message, String timestamp) {
        String toUserName = message.getToUserName();
        String fromUserName = message.getFromUserName();
        String content = message.getContent();

        StringBuilder replyContent = new StringBuilder();
        switch (content) {
            case "你好":
                replyContent = replyContent.append("● v ●");
                break;
            default:
        }

        return XmlMessageAssembler.generateClearXml(fromUserName, toUserName, timestamp, replyContent.toString());
    }
}
