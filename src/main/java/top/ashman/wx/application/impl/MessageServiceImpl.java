package top.ashman.wx.application.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ashman.wx.application.MessageService;
import top.ashman.wx.domain.model.message.Message;
import top.ashman.wx.domain.model.message.MessageRepository;

/**
 * @author singoasher
 * @date 2018/1/31
 */
@Service
public class MessageServiceImpl implements MessageService {
    private MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message save(Message message) {
        return messageRepository.save(message);
    }
}
