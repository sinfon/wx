package top.ashman.wx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author singoasher
 * @date 2018/2/8
 */
@Configuration
public class FeignConfiguration {

    /**
     * 创建 Feign 请求拦截器，在发送请求前设置请求头
     *
     * @return FeignHttpHeaderInterceptor
     */
    @Bean
    public FeignHttpHeaderInterceptor feignHttpHeaderInterceptor() {
        return new FeignHttpHeaderInterceptor();
    }
}
