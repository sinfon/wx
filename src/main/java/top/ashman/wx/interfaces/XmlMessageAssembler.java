package top.ashman.wx.interfaces;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.util.StringUtils;
import top.ashman.wx.domain.model.message.Message;

import java.time.Instant;
import java.util.Optional;

/**
 * @author singoasher
 * @date 2018/2/2
 */
public class XmlMessageAssembler {
    public static Message assemble(String xmlString) throws DocumentException {
        Document document = DocumentHelper.parseText(xmlString);
        Element root = document.getRootElement();
        String toUserName = parseStringValue(root, ElementNames.TO_USER_NAME);
        String fromUserName = parseStringValue(root, ElementNames.FROM_USER_NAME);
        Instant createTime = parseTimestampValue(root, ElementNames.CREATE_TIME);
        String msgType = parseStringValue(root, ElementNames.MSG_TYPE);
        String content = parseStringValue(root, ElementNames.CONTENT);
        String msgId = parseStringValue(root, ElementNames.MSG_ID);
        String encrypt = parseStringValue(root, ElementNames.ENCRYPT);
        return new Message(toUserName, fromUserName, createTime, msgType, content, msgId, encrypt);
    }

    private static String parseStringValue(Element root, String elementName) {
        return Optional.ofNullable(root.element(elementName))
                .map(Element::getStringValue)
                .orElse(null);
    }

    private static Instant parseTimestampValue(Element root, String elementName) {
        return Optional.ofNullable(root.element(elementName))
                .map(element -> Instant.ofEpochMilli(Long.parseLong(element.getStringValue())))
                .orElse(null);
    }

    /**
     * 提取出 xml 数据包中的加密消息
     *
     * @param xmlString 待提取的xml字符串
     * @return 提取出的加密消息字符串
     * @throws DocumentException
     */
    public static Object[] extract(String xmlString) throws DocumentException {
        Document document = DocumentHelper.parseText(xmlString);

        Element root = document.getRootElement();
        String encrypt = root.element("Encrypt").getStringValue();
        String toUserName = root.element("ToUserName").getStringValue();

        Object[] result = new Object[3];
        result[0] = 0;
        result[1] = encrypt;
        result[2] = toUserName;

        return result;
    }


    /**
     * 生成明文 xml 消息
     *
     * @param to Message To
     * @param from Message From
     * @param content Message
     * @return Xml String
     */
    private static String generateClearXml(String to, String from, String content) {
        if (StringUtils.isEmpty(content)) {
            return "success";
        }

        return  "<xml>\n" +
                "<ToUserName><![CDATA[" + to + "]]>\n" +
                "</ToUserName><FromUserName><![CDATA[" + from + "]]></FromUserName>\n" +
                "<CreateTime>" + Instant.now().toEpochMilli() + "</CreateTime>\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "<Content><![CDATA[" + content + "]]></Content>\n" +
                "<FuncFlag>0</FuncFlag>\n" +
                "</xml>";
    }

    /**
     * 生成加密 xml 消息
     *
     * @param encrypt 加密后的消息密文
     * @param signature 安全签名
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @return 生成的xml字符串
     */
    public static String generateCipherXml(String encrypt, String signature, String timestamp, String nonce) {
        String format = "<xml>\n" +
                "<Encrypt><![CDATA[%1$s]]></Encrypt>\n" +
                "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n" +
                "<TimeStamp>%3$s</TimeStamp>\n" +
                "<Nonce><![CDATA[%4$s]]></Nonce>\n" +
                "</xml>";
        return String.format(format, encrypt, signature, timestamp, nonce);

    }

    private class ElementNames {
        private static final String TO_USER_NAME = "ToUserName";
        private static final String FROM_USER_NAME = "FromUserName";
        private static final String CREATE_TIME = "CreateTime";
        private static final String MSG_TYPE = "MsgType";
        private static final String CONTENT = "Content";
        private static final String MSG_ID = "MsgId";
        private static final String ENCRYPT = "Encrypt";

    }
}
