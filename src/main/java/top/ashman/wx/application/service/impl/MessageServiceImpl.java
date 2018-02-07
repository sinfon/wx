package top.ashman.wx.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.ashman.wx.application.client.OddClient;
import top.ashman.wx.application.service.MessageService;
import top.ashman.wx.config.property.WxProperties;
import top.ashman.wx.domain.model.message.Message;
import top.ashman.wx.domain.model.message.MessageRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

/**
 * @author singoasher
 * @date 2018/1/31
 */
@Service
public class MessageServiceImpl implements MessageService {
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
        switch (wxProperties.getMode()) {
            case CLEAR:
                return handleCipherText(message);
            case CIPHER:
                return handleCipherText(message);
            case COMPAT:
                if (Optional.ofNullable((message.getEncrypt())).isPresent()) {
                    return handleCipherText(message);
                } else {
                    return handleClearText(message);
                }
            default:
                return null;
        }
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }

    private String handleCipherText(Message message) {
        return "success";
    }

    private String handleClearText(Message message) {
        executorService.execute(() -> save(message));

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

        return formatXmlAnswer(fromUserName, toUserName, replyContent.toString());
    }

    /**
     * 封装文字类的返回消息
     *
     * @param to Message To
     * @param from Message From
     * @param content Message
     * @return Xml String
     */
    private static String formatXmlAnswer(String to, String from, String content) {
        if (StringUtils.isEmpty(content)) {
            return "success";
        }

        return  "<xml>" +
                "<ToUserName><![CDATA[" + to + "]]>" +
                "</ToUserName><FromUserName><![CDATA[" + from + "]]></FromUserName>" +
                "<CreateTime>" + Instant.now().toEpochMilli() + "</CreateTime>" +
                "<MsgType><![CDATA[text]]></MsgType>" +
                "<Content><![CDATA[" + content + "]]></Content>" +
                "<FuncFlag>0</FuncFlag>" +
                "</xml>";
    }
}
