package top.ashman.wx.application;

import top.ashman.wx.domain.model.message.Message;

/**
 * @author singoasher
 * @date 2018/1/31
 */
public interface MessageService {
    Message save(Message message);
}
