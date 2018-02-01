package top.ashman.wx.domain.model.message;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 * @author singoasher
 * @date 2018/1/30
 */
@Data
@Builder
public class Message {
    private String id;

    /**
     * 公众号
     */
    private String toUserName;

    /**
     * 粉丝号
     */
    private String fromUserName;

    /**
     * 微信公众平台记录粉丝发送该消息的具体时间
     */
    private Instant createTime;

    /**
     * 用于标记 xml 的消息类型 [ text - 文本消息 ]
     */
    private String msgType;

    /**
     * 粉丝号发送的具体消息内容
     */
    private String content;

    /**
     * 公众平台为记录识别该消息的一个标记数值, 微信后台系统自动产生
     */
    private String msgId;
}
