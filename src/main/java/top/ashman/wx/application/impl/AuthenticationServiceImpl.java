package top.ashman.wx.application.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ashman.wx.application.AuthenticationService;
import top.ashman.wx.config.property.WxProperties;
import top.ashman.wx.domain.model.authentication.Authentication;
import top.ashman.wx.interfaces.AuthenticationQuery;

/**
 * @author singoasher
 * @date 2018/1/30
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private WxProperties wxProperties;

    @Autowired
    public AuthenticationServiceImpl(WxProperties wxProperties) {
        this.wxProperties = wxProperties;
    }

    @Override
    public String authenticate(AuthenticationQuery authenticationQuery) {
        Authentication authentication = new Authentication(
                authenticationQuery.getSignature(),
                authenticationQuery.getTimestamp(),
                authenticationQuery.getNonce(),
                authenticationQuery.getEchostr(),
                wxProperties.getServer().getToken());
        return authentication.authenticate();
    }
}
