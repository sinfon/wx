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
    private Mode mode;
    private Server server;

    @Data
    public static class Server {
        private String token;
        private String encodingAESKey;
        private Integer encodingAESKeyLength = 43;
    }

    /**
     * 微信后台发送消息有三种模式，此处名称与其对应，但具体含义不同 <br/>
     * 安全模式：仅当微信消息包含 Encrypt 元素时进行解析 [可处理微信后台的兼容，安全模式]
     * 明文模式：包含明文数据时进行解析 [可处理微信后台的兼容，明文模式]
     * 兼容模式：判断数据类型，优先解析加密数据 [可处理微信后台的三种模式]
     */
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
