package top.ashman.wx.infrastructure.persistence.mongo.message;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * @author singoasher
 * @date 2018/1/31
 */
@Data
@Builder
@Document(collection = "message")
public class MessageDocument {
    @Id
    private String id;
    private String toUserName;
    private String fromUserName;
    private Instant createTime;
    private String msgType;
    private String content;
    private String msgId;
    private String encrypt;
}
