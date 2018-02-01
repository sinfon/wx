package top.ashman.wx.application;

import top.ashman.wx.interfaces.AuthenticationQuery;

/**
 * @author singoasher
 * @date 2018/1/30
 */
public interface AuthenticationService {
    /**
     * 修改服务器配置时，响应微信发送的 Token 验证
     * @param authenticationQuery 验证信息
     * @return
     */
    String authenticate(AuthenticationQuery authenticationQuery);
}
