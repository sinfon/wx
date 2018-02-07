package top.ashman.wx.infrastructure.persistence.mongo.message;

import top.ashman.wx.domain.model.message.Message;

import java.util.Optional;

/**
 * @author singoasher
 * @date 2018/1/31
 */
public class MessageAssembler {
    public static Message assemble(MessageDocument messageDocument) {
        return Optional.ofNullable(messageDocument)
                .map(m -> Message.builder()
                        .id(m.getId())
                        .toUserName(m.getToUserName())
                        .fromUserName(m.getFromUserName())
                        .createTime(m.getCreateTime())
                        .msgType(m.getMsgType())
                        .content(m.getContent())
                        .msgId(m.getMsgId())
                        .msgId(m.getEncrypt())
                        .build())
                .orElse(null);
    }

    public static MessageDocument assemble(Message message) {
        return Optional.ofNullable(message)
                .map(m -> MessageDocument.builder()
                        .id(m.getId())
                        .toUserName(m.getToUserName())
                        .fromUserName(m.getFromUserName())
                        .createTime(m.getCreateTime())
                        .msgType(m.getMsgType())
                        .content(m.getContent())
                        .msgId(m.getMsgId())
                        .encrypt(m.getEncrypt())
                        .build())
                .orElse(null);
    }
}
