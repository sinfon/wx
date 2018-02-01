package top.ashman.wx.interfaces;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.util.StringUtils;
import top.ashman.wx.application.MessageService;
import top.ashman.wx.config.property.WxProperties;
import top.ashman.wx.domain.model.message.Message;

import java.text.ParseException;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

/**
 * 微信发送明文模式消息，无 Encrypt 元素，仅有明文元素 <br/>
 * 密文模式有 Encrypt 元素，以及 ToUserName 元素，无明文元素 <br/>
 * 兼容模式则是全部都会发送过来
 *
 * @author singoasher
 * @date 2018/1/31
 */
public class WxRequestProcessor {
    private static final String ELEMENT_ENCRYPT_NAME = "Encrypt";

    public static String response(WxProperties.Mode mode, Document document, MessageService messageService, ExecutorService executorService) throws ParseException {
        Element root = document.getRootElement();
        switch (mode) {
            case CLEAR:
                return handleClearText(root, messageService, executorService);
            case CIPHER:
                return handleCipherText(root, messageService, executorService);
            case COMPAT:
                if (Objects.isNull(root.element(ELEMENT_ENCRYPT_NAME))) {
                    return handleClearText(root, messageService, executorService);
                } else {
                    return handleCipherText(root, messageService, executorService);
                }
            default:
                return null;
        }
    }


    private static String handleCipherText(Element root, MessageService messageService, ExecutorService executorService) {
        String encrypt = root.element(ELEMENT_ENCRYPT_NAME).getStringValue();
        return "success";
    }

    public static String handleClearText(Element root, MessageService messageService, ExecutorService executorService) throws ParseException {
        String toUserName = root.element("ToUserName").getStringValue();
        String fromUserName = root.element("FromUserName").getStringValue();
        Instant createTime = Instant.ofEpochMilli(Long.parseLong(root.element("CreateTime").getStringValue()));
        String msgType = root.elementText("MsgType");
        String content = root.elementText("Content");
        String msgId = root.elementText("MsgId");

        Message message = Message.builder()
                .toUserName(toUserName)
                .fromUserName(fromUserName)
                .createTime(createTime)
                .msgType(msgType)
                .content(content)
                .msgId(msgId)
                .build();

        executorService.execute(() -> messageService.save(message));

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
