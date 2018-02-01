package top.ashman.wx.infrastructure.util;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author singoasher
 * @date 2018/1/31
 */
public class MessageDigestUtil {

    public static byte[] digest(Algorithm algorithm, byte[] input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm.value());
        return messageDigest.digest(input);
    }

    public static byte[] toBytes(String[] input) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : input) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString().getBytes(Charset.forName(WxConsts.DEFAULT_CHARSET_NAME));
    }

    /**
     * Converts an array of bytes into a Hex String with lowercase
     *
     * @param bytes a byte[] to convert to Hex String
     * @return Hex String
     */
    public static String encodeHexString(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }
}
