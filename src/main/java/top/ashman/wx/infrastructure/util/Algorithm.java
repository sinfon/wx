package top.ashman.wx.infrastructure.util;

/**
 * @author singoasher
 * @date 2018/2/1
 */
public enum Algorithm {
    /**
     * SHA-1
     */
    SHA_1("SHA-1"),

    /**
     * AES
     */
    AES("AES");

    private String value;

    Algorithm(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
