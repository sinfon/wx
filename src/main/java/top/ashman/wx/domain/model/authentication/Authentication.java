package top.ashman.wx.domain.model.authentication;

import lombok.Data;
import top.ashman.wx.infrastructure.util.Algorithm;
import top.ashman.wx.infrastructure.util.MessageDigestUtil;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

/**
 * 微信后台发来的服务器认证信息
 * @author singoasher
 * @date 2018/1/30
 */
@Data
public class Authentication {

    /**
     * 微信加密签名
     */
    private String signature;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 随机数
     */
    private String nonce;

    /**
     * 随机字符串
     */
    private String echostr;

    /**
     * 服务器令牌
     */
    private String token;

    public Authentication(String signature, String timestamp, String nonce, String echostr, String token) {
        this.signature = signature;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.echostr = echostr;
        this.token = token;
    }

    /**
     * 认证数据源是否为微信后台，认证成功返回 echostr 否则返回空值 <br/>
     * 用 SHA-1 算法生成安全签名，并验证是否与数据来源发送的签名一致
     *
     * @return echostr or null
     */
    public String authenticate() {
        // token, timestamp, nonce 字典序排序得到字符串 list
        String[] list = {token, timestamp, nonce};
        Arrays.sort(list);

        // 哈希算法加密 list 得到 hashcode
        String hashCode;
        try {
            byte[] bytesHashCode = MessageDigestUtil.digest(Algorithm.SHA_1, MessageDigestUtil.toBytes(list));
            hashCode = MessageDigestUtil.encodeHexString(bytesHashCode);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        // hashcode 等于 signature 可确定数据源为微信后台
        boolean isAuthenticated = Optional.of(hashCode).map(h -> h.equals(signature.toLowerCase())).orElse(false);

        // 认证成功，将 echostr 返回，供微信后台认证 token，否则返回 null
        return isAuthenticated ? echostr : null;
    }
}
