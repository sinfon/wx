package top.ashman.wx.application.service;

import top.ashman.wx.domain.model.message.Message;

import java.text.ParseException;

/**
 * @author singoasher
 * @date 2018/1/31
 */
public interface MessageService {
    /**
     * 处理消息
     * @param message 消息
     * @return 处理结果
     */
    String handle(Message message);

    /**
     * 保存消息
     * @param message 消息
     * @return 保存后的消息
     */
    Message save(Message message);
}
