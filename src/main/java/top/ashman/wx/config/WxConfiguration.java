package top.ashman.wx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.ashman.wx.config.property.WxProperties;

/**
 * @author singoasher
 * @date 2018/1/30
 */
@Configuration
@EnableConfigurationProperties(WxProperties.class)
public class WxConfiguration {
    private WxProperties wxProperties;

    @Autowired
    public WxConfiguration(WxProperties wxProperties) {
        this.wxProperties = wxProperties;
    }

    @Bean
    public WxProperties wxProperties() {
        return wxProperties;
    }
}
