package top.ashman.wx.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.ashman.wx.config.property.ThreadPoolProperties;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author singoasher
 * @date 2018/1/31
 */
@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolConfiguration {
    private ThreadPoolProperties threadPoolProperties;

    @Autowired
    public ThreadPoolConfiguration(ThreadPoolProperties threadPoolProperties) {
        this.threadPoolProperties = threadPoolProperties;
    }

    @Bean
    public ThreadFactory threadFactory() {
        return new ThreadFactoryBuilder().setNameFormat("wx-thread-%d").build();
    }

    @Bean
    public ExecutorService executorService(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(
                threadPoolProperties.getCorePoolSize(), threadPoolProperties.getMaximumPoolSize(),
                threadPoolProperties.getKeepAliveTime(), threadPoolProperties.getTimeUnit(),
                new LinkedBlockingDeque<>(threadPoolProperties.getQueueSize()), threadFactory);
    }
}
