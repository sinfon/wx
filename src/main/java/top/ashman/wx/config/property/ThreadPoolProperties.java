package top.ashman.wx.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author singoasher
 * @date 2018/1/31
 */
@Data
@ConfigurationProperties(prefix = "ashman.thread.pool")
public class ThreadPoolProperties {
    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Long keepAliveTime;
    private TimeUnit timeUnit;
    private Integer queueSize;
}
