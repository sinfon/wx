package top.ashman.wx.infrastructure.util.security;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.ashman.wx.infrastructure.util.WxConstants;

import java.util.Arrays;
import java.util.Random;

/**
 * @author singoasher
 * @date 2018/2/6
 */
public class AESUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtil.class);

    /**
     * 对明文进行加密
     *
     * @param nonce 随机字符串
     * @param clearText 需要加密的明文
     * @param appId App Id
     * @param encodingAESKey 加密密钥
     * @return 加密后 Base64 编码的字符串
     */
    public static String encrypt(String nonce, String clearText, String appId, String encodingAESKey) {
        ByteGroup byteGroup = new ByteGroup();
        byte[] nonceBytes = nonce.getBytes(WxConstants.DEFAULT_CHARSET);
        byte[] clearTextBytes = clearText.getBytes(WxConstants.DEFAULT_CHARSET);
        byte[] textNetworkBytesOrder = getNetworkBytesOrder(clearTextBytes.length);
        byte[] appIdBytes = appId.getBytes(WxConstants.DEFAULT_CHARSET);

        // random + textNetworkBytesOrder + text + appId
        byteGroup.addBytes(nonceBytes);
        byteGroup.addBytes(textNetworkBytesOrder);
        byteGroup.addBytes(clearTextBytes);
        byteGroup.addBytes(appIdBytes);

        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        byte[] padBytes = PKCS7Encoder.encode(byteGroup.size());
        byteGroup.addBytes(padBytes);

        // 获得最终的字节流, 未加密
        byte[] unencrypted = byteGroup.toBytes();


        return CipherUtil.encrypt(Transformation.AES_CBC_NO_PADDING, Algorithm.AES, Base64.decodeBase64(encodingAESKey), unencrypted);
    }

    /**
     * 对密文进行解密
     *
     * @param cipherText 需要解密的密文
     * @param encodingAESKey 解密密钥
     * @param appId App Id
     * @return 解密结果
     */
    public static String decrypt(String cipherText, String encodingAESKey, String appId) {
        byte[] bytesCipherText = CipherUtil.decrypt(Transformation.AES_CBC_NO_PADDING, Algorithm.AES,
                Base64.decodeBase64(encodingAESKey), cipherText);

        String xmlContent, fromAppId;
        try {
            // 去除补位字符
            byte[] bytes = PKCS7Encoder.decode(bytesCipherText);

            // 分离16位随机字符串,网络字节序和AppId
            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

            int xmlLength = recoverNetworkBytesOrder(networkOrder);

            xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), WxConstants.DEFAULT_CHARSET);
            fromAppId = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), WxConstants.DEFAULT_CHARSET);
        } catch (Exception e) {
            LOGGER.error("AES decrypt error: {}", e.getMessage());
            throw new AesException(AesException.DECRYPT_AES_ERROR);
        }

        // App Id 不相同的情况
        if (!fromAppId.equals(appId)) {
            LOGGER.error("AES decrypt error: [ App Id from Cipher ] NOT same with [ App Id from properties ]");
            throw new AesException(AesException.VALIDATE_APP_ID_ERROR);
        }

        return xmlContent;
    }

    /**
     * 生成4个字节的网络字节序
     *
     * @param sourceNumber
     * @return
     */
    private static byte[] getNetworkBytesOrder(int sourceNumber) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }

    /**
     * 还原 4 个字节的网络字节序
     *
     * @param orderBytes
     * @return
     */
    private static int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    /**
     * 生成随机字符串
     * @return 随机字符串
     */
    public static String generateNonce() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < WxConstants.RANDOM_LENGTH; i++) {
            int number = random.nextInt(WxConstants.RANDOM_BASE.length());
            stringBuilder.append(WxConstants.RANDOM_BASE.charAt(number));
        }
        return stringBuilder.toString();
    }
}
