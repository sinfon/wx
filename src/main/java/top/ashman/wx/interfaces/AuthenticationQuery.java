package top.ashman.wx.interfaces;

import lombok.Data;
import lombok.ToString;

/**
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
