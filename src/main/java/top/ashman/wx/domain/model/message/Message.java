package top.ashman.wx.domain.model.message;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ashman.wx.infrastructure.util.security.AESUtil;
import top.ashman.wx.infrastructure.util.security.AesException;

import java.time.Instant;
import java.util.Objects;

/**
 * @author singoasher
 * @date 2018/1/30
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    private static final Logger LOGGER = LoggerFactory.getLogger(Message.class);

    private String id;

    /**
     * 公众号
     */
    private String toUserName;

    /**
     * 粉丝号
     */
    private String fromUserName;

    /**
     * 微信公众平台记录粉丝发送该消息的具体时间
     */
    private Instant createTime;

    /**
     * 用于标记 xml 的消息类型 [ text - 文本消息 ]
     */
    private String msgType;

    /**
     * 粉丝号发送的具体消息内容
     */
    private String content;

    /**
     * 公众平台为记录识别该消息的一个标记数值, 微信后台系统自动产生
     */
    private String msgId;

    /**
     * 加密消息字符串
     */
    private String encrypt;

    Message(String toUserName, String fromUserName, Instant createTime, String msgType, String content, String msgId, String encrypt) {
        this.toUserName = toUserName;
        this.fromUserName = fromUserName;
        this.createTime = createTime;
        this.msgType = msgType;
        this.content = content;
        this.msgId = msgId;
        this.encrypt = encrypt;
    }

    public Message decrypt(String encodingAESKey, int encodingAESKeyLength, String appId) {
        if (Objects.isNull(this.encrypt)) {
            LOGGER.error("Message.encrypt is null, do not need to decrypt");
            return this;
        }

        if (Objects.isNull(encodingAESKey) || encodingAESKey.length() != encodingAESKeyLength) {
            LOGGER.error("EncodingAESKey: {}, ERROR, should not be null or length should be {}",
                    encodingAESKey,
                    encodingAESKeyLength);
            throw new AesException(AesException.ILLEGAL_AES_KEY);
        }

        String decryptedXmlMessage = AESUtil.decrypt(encrypt, encodingAESKey, appId);
        LOGGER.info("Decrypted Xml Message: {}", decryptedXmlMessage);

        XmlMessageAssembler.assembleDecrypted(decryptedXmlMessage, this);
        LOGGER.info("After Assemble Decrypted Message: {}", this.toString());

        return this;
    }
}
