package top.ashman.wx.interfaces;

import lombok.Data;
import lombok.ToString;

/**
 * signature, timestamp, nonce 无论 GET 还是 POST 请求，均会发送，用以进行数据源认证 <br/>
 * echostr 仅会在 GET 请求（服务器认证）时发送，认证成功返回给微信后台
 *
 * @author singoasher
 * @date 2018/1/30
 */
@Data
@ToString
public class AuthenticationQuery {
    private String signature;
    private String timestamp;
    private String nonce;
    private String echostr;
}
