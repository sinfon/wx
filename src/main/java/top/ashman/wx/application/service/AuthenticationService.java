package top.ashman.wx.application.service;

/**
 * @author singoasher
 * @date 2018/1/30
 */
public interface AuthenticationService {

    /**
     * 修改服务器配置时，响应微信发送的 Token 验证
     *
     * @param signature 数据源计算的签名值
     * @param timestamp 时间戳
     * @param nonce 随机字符串
     * @return
     */
    boolean authenticate(String signature, String timestamp, String nonce);
}
