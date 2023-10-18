package test_util;

import com.example.entity.Message;
import org.instancio.Instancio;

import java.util.List;

import static org.instancio.Select.field;

public class TestMessageUtil {

    public static Message createMessageWithSenderIdAndReceiverId(Long senderId, Long receiverId) {
        return Instancio.of(Message.class)
                .set(field(Message::getSenderId), senderId)
                .set(field(Message::getReceiverId), receiverId)
                .create();
    }

    public static List<Message> createMessagesWithSenderIdAndReceiverId(Long senderId, Long receiverId) {
        return Instancio.ofList(Message.class)
                .set(field(Message::getSenderId), senderId)
                .set(field(Message::getReceiverId), receiverId)
                .create();
    }
}
