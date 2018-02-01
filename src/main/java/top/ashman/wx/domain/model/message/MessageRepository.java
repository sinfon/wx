package top.ashman.wx.domain.model.message;

/**
 * @author singoasher
 * @date 2018/1/31
 */
public interface MessageRepository {
    /**
     * Save Message
     * @param message Message
     * @return Saved Message
     */
    Message save(Message message);
}
