package top.ashman.wx.application.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ashman.wx.application.service.AuthenticationService;
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
    public boolean authenticate(String signature, String timestamp, String nonce) {
        Authentication authentication = new Authentication(signature, timestamp, nonce,
                wxProperties.getServer().getToken());
        return authentication.authenticate();
    }
}
