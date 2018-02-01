package top.ashman.wx.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author singoasher
 * @date 2018/1/30
 */
@Data
@ConfigurationProperties(prefix = "ashman.wx")
public class WxProperties {
    private String appId;
    private Server server;

    @Data
    public static class Server {
        private String token;
        private String encodingAesKey;
        private Integer encodingAesKeyLength = 43;
        private Mode mode;
    }

    public enum Mode {
        /**
         * 安全模式
         */
        CIPHER,

        /**
         * 明文模式
         */
        CLEAR,

        /**
         * 兼容模式
         */
        COMPAT
    }
}
