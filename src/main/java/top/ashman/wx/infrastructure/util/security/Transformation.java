package top.ashman.wx.infrastructure.util.security;

/**
 * 加密模式
 *
 * @author singoasher
 * @date 2018/2/6
 */
public enum Transformation {
    /**
     * 加密模式 AES CBC 模式 <br/>
     * AES/CBC/NoPadding
     */
    AES_CBC_NO_PADDING("AES/CBC/NoPadding");

    private String value;

    Transformation(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
