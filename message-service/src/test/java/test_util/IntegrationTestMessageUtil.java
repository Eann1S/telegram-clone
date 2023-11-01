package test_util;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.instancio.Instancio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.List;

import static org.instancio.Select.field;
import static org.instancio.Select.root;

@TestComponent
public class IntegrationTestMessageUtil {

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessageWithSenderIdAndReceiverId(Long senderId, Long receiverId) {
        Message message = Instancio.of(Message.class)
                .set(field(Message::getSenderId), senderId)
                .set(field(Message::getReceiverId), receiverId)
                .create();
        return messageRepository.save(message);
    }

    public List<Message> createMessagesWithSenderIdAndReceiverId(Long senderId, Long receiverId, int size) {
        List<Message> messages = Instancio.ofList(Message.class)
                .generate(root(), gen -> gen.collection().size(size))
                .set(field(Message::getSenderId), senderId)
                .set(field(Message::getReceiverId), receiverId)
                .create();
        return messageRepository.saveAll(messages);
    }
}
