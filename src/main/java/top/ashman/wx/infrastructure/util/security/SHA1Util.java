package top.ashman.wx.infrastructure.util.security;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author singoasher
 * @date 2018/2/6
 */
public class SHA1Util {
    private static final Algorithm ALGORITHM_SHA_1 = Algorithm.SHA_1;

    /**
     * 用 SHA1 算法生成 <b>数据源认证请求数据</b> 的安全签名
     *
     * @param token 服务器令牌
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @return 安全签名
     */
    public static String getSignature(String token, String timestamp, String nonce) {
        // token, timestamp, nonce 字典序排序得到字符串 list
        String[] list = {token, timestamp, nonce};
        return getSignature(list);
    }

    /**
     * 用 SHA1 算法生成 <b>安全模式下待回传加密数据</b> 的安全签名
     *
     * @param token 票据
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @param cipherText 密文
     * @return 安全签名
     */
    public static String getSignature(String token, String timestamp, String nonce, String cipherText) {
        // token, timestamp, nonce, cipherText 字典序排序得到字符串 list
        String[] list = {token, timestamp, nonce, cipherText};
        return getSignature(list);
    }

    private static String getSignature(String[] list) {
        // 字符串排序
        Arrays.sort(list);

        // 哈希算法加密 list 得到 hashcode 即为所需要的安全签名
        try {
            byte[] bytesHashCode = MessageDigestUtil.digest(ALGORITHM_SHA_1, MessageDigestUtil.toBytes(list));
            return MessageDigestUtil.encodeHexString(bytesHashCode);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
