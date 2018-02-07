package top.ashman.wx.interfaces;

import lombok.Data;

import java.time.Instant;

/**
 * @author singoasher
 * @date 2018/2/2
 */
@Data
public class ResponseMessage {
    private String toUserName;
    private String fromUserName;
    private Instant createTime;
    private String msgType;
    private String content;
    private Integer funcFlag;
}
