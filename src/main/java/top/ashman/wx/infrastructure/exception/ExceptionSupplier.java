package top.ashman.wx.infrastructure.exception;

import java.util.function.Supplier;

/**
 * @author singoasher
 * @date 2018/2/6
 */
public class ExceptionSupplier implements Supplier<XmlMessageException> {
    private String message;

    public ExceptionSupplier(String message) {
        this.message = message;
    }

    @Override
    public XmlMessageException get() {
        return new XmlMessageException(message);
    }
}
