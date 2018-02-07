package top.ashman.wx.infrastructure.util;

import java.nio.charset.Charset;

/**
 * @author singoasher
 * @date 2018/2/1
 */
public class WxConstants {
    private static final String DEFAULT_CHARSET_NAME = "UTF-8";
    public static final Charset DEFAULT_CHARSET = Charset.forName(DEFAULT_CHARSET_NAME);
    public static final String RANDOM_BASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static final int RANDOM_LENGTH = 16;
    public static final String SUCESS_MESSAGE = "success";
}
