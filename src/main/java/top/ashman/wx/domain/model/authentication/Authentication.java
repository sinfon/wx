package top.ashman.wx.domain.model.authentication;

import lombok.Data;
import top.ashman.wx.infrastructure.util.security.SHA1Util;

import java.util.Optional;

/**
 * 微信后台发来的服务器认证信息
 * @author singoasher
 * @date 2018/1/30
 */
@Data
public class Authentication {

    /**
     * 数据源生成的安全签名
     */
    private String signature;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 随机字符串
     */
    private String nonce;

    /**
     * 服务器令牌
     */
    private String token;

    public Authentication(String signature, String timestamp, String nonce, String token) {
        this.signature = Optional.of(signature).orElseThrow(NullPointerException::new);
        this.timestamp = Optional.of(timestamp).orElseThrow(NullPointerException::new);
        this.nonce = Optional.of(nonce).orElseThrow(NullPointerException::new);
        this.token = Optional.of(token).orElseThrow(NullPointerException::new);
    }

    /**
     * 认证数据源是否为微信后台 <br/>
     * 用 SHA-1 算法生成安全签名，并验证是否与数据来源发送的签名一致
     *
     * @return 认证成功与否
     */
    public boolean authenticate() {
        String hashCode = SHA1Util.getSignature(token, timestamp, nonce);

        // hashcode 等于 signature 可确定数据源为微信后台
        return Optional.ofNullable(hashCode).map(h -> h.equals(signature.toLowerCase())).orElse(false);
    }
}
