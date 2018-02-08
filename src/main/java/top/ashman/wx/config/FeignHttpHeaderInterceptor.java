package top.ashman.wx.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import top.ashman.wx.infrastructure.http.CurrentRequest;

/**
 * @author singoasher
 * @date 2018/2/8
 */
public class FeignHttpHeaderInterceptor implements RequestInterceptor {
    public static final ThreadLocal<String> USER_ID_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(CurrentRequest.HttpHeaders.USER_ID_NAME, USER_ID_THREAD_LOCAL.get());
        USER_ID_THREAD_LOCAL.remove();
    }
}
