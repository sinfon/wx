package top.ashman.wx.infrastructure.persistence.mongo.message;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author singoasher
 * @date 2018/1/31
 */
public interface MessageMongoRepository extends MongoRepository<MessageDocument, String> {
}
