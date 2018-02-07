package top.ashman.wx.domain.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * @author singoasher
 * @date 2018/1/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
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


    private String encodingAesKey;

    private Integer encodingAesKeyLength;

    public Message(String toUserName,
                   String fromUserName,
                   Instant createTime,
                   String msgType,
                   String content,
                   String msgId,
                   String encrypt) {
        this.toUserName = toUserName;
        this.fromUserName = fromUserName;
        this.createTime = createTime;
        this.msgType = msgType;
        this.content = content;
        this.msgId = msgId;
        this.encrypt = encrypt;
    }

    //    public Message(String encodingAesKey, Integer encodingAesKeyLength) {
//        if (encodingAesKey.length() != encodingAesKeyLength) {
//            throw new AesException(AesException.ILLEGAL_AES_KEY);
//        }
//    }





//    /**
//     * 对密文进行解密.
//     *
//     * @param text 需要解密的密文
//     * @return 解密得到的明文
//     * @throws AesException aes解密失败
//     */
//    public String decrypt(String text) throws AesException {
//
//
//
//        byte[] original;
//        try {
//            // 设置解密模式为AES的CBC模式
//            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//            SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
//            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
//            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);
//
//            // 使用BASE64对密文进行解码
//            byte[] encrypted = Base64.decodeBase64(text);
//
//            // 解密
//            original = cipher.doFinal(encrypted);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new AesException(AesException.DECRYPT_AES_ERROR);
//        }
//
//        String xmlContent, from_appid;
//        try {
//            // 去除补位字符
//            byte[] bytes = PKCS7Encoder.decode(original);
//
//            // 分离16位随机字符串,网络字节序和AppId
//            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);
//
//            int xmlLength = recoverNetworkBytesOrder(networkOrder);
//
//            xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), CHARSET);
//            from_appid = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length),
//                    CHARSET);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new AesException(AesException.ILLEGAL_BUFFER);
//        }
//
//        // appid不相同的情况
//        if (!from_appid.equals(appId)) {
//            throw new AesException(AesException.VALIDATE_APP_ID_ERROR);
//        }
//        return xmlContent;
//
//    }
}
