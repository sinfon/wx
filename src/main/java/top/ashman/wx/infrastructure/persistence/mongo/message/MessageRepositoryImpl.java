package top.ashman.wx.infrastructure.persistence.mongo.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import top.ashman.wx.domain.model.message.Message;
import top.ashman.wx.domain.model.message.MessageRepository;

/**
 * @author singoasher
 * @date 2018/1/31
 */
@Repository
public class MessageRepositoryImpl implements MessageRepository {
    private MessageMongoRepository messageMongoRepository;

    @Autowired
    public MessageRepositoryImpl(MessageMongoRepository messageMongoRepository) {
        this.messageMongoRepository = messageMongoRepository;
    }

    @Override
    public Message save(Message message) {
        return MessageAssembler.assemble(messageMongoRepository.save(MessageAssembler.assemble(message)));
    }
}
