package top.ashman.wx.infrastructure.exception;

/**
 * @author singoasher
 * @date 2018/2/6
 */
public class XmlMessageException extends RuntimeException {
    public static final String TO_USER_NAME_ERROR = "ToUserName Error";

    public XmlMessageException(String message) {
        super(message);
    }
}
