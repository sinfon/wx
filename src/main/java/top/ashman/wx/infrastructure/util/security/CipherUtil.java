package top.ashman.wx.infrastructure.util.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * @author singoasher
 * @date 2018/2/6
 */
class CipherUtil {

    /**
     * 加密明文
     *
     * @param transformation 加密模式
     * @param algorithm 算法
     * @param key 加密密钥
     * @param clearText 待加密明文
     * @return Base64 编码的加密字符串
     */
    static String encrypt(Transformation transformation, Algorithm algorithm, byte[] key, byte[] clearText) {
        try {
            // 设置加密模式
            Cipher cipher = Cipher.getInstance(transformation.value());
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm.value());
            IvParameterSpec ivParameterSpec = new IvParameterSpec(key, 0, 16);

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // 加密
            byte[] encrypted = cipher.doFinal(clearText);

            // 使用 Base64 对加密后的字节数组进行编码
            return Base64.encodeBase64String(encrypted);
        } catch (Exception e) {
            e.printStackTrace();

            // 加密失败返回 null
            return null;
        }
    }

    static byte[] decrypt(Transformation transformation, Algorithm algorithm, byte[] key, String cipherText) {
        try {
            // 设置解密模式
            Cipher cipher = Cipher.getInstance(transformation.value());
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm.value());
            IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOfRange(key, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            // 使用 Base64 对密文进行解码
            byte[] bytesCipherText = Base64.decodeBase64(cipherText);

            // 解密
            return cipher.doFinal(bytesCipherText);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
