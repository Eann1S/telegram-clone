package integration_tests.controller;

import com.example.MessageServiceApplication;
import com.example.client.UserServiceClient;
import com.example.dto.MessageDto;
import com.example.dto.UserDto;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
import com.google.gson.reflect.TypeToken;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import test_util.IntegrationTestMessageUtil;
import test_util.starter.AllServicesStarter;

import java.util.List;

import static com.example.json.JsonConverter.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static test_util.TestControllerUtil.expectStatus;
import static test_util.TestControllerUtil.getResponseContentWithExpectedStatus;
import static test_util.constant.UrlConstant.*;

@SpringBootTest(classes = {MessageServiceApplication.class, IntegrationTestMessageUtil.class})
@ActiveProfiles("test")
@ExtendWith(InstancioExtension.class)
@AutoConfigureMockMvc
public class ChatControllerIntegrationTests implements AllServicesStarter {

    @MockBean
    private UserServiceClient userServiceClient;
    @Autowired
    private IntegrationTestMessageUtil testMessageUtil;
    @Autowired
    @SpyBean
    private MessageRepository messageRepository;
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @InstancioSource
    void shouldWriteMessage_whenGivenValidFriendId(
            String text, Long userId, Long friendId, UserDto sender, UserDto receiver
    ) throws Exception {
        mockUserServiceClientToReturnSenderAndReceiver(userId, friendId, sender, receiver);

        String jsonResponse = writeMessageAndExpectStatus(text, userId, friendId, CREATED);

        MessageDto messageDto = fromJson(jsonResponse, MessageDto.class);
        assertThat(messageDto)
                .extracting(MessageDto::text, MessageDto::sender, MessageDto::receiver, MessageDto::messageId)
                .contains(text, sender, receiver)
                .doesNotContainNull();
    }

    @ParameterizedTest
    @InstancioSource
    void shouldReturnChat_whenGivenValidFriendId(
            Long userId, Long friendId, UserDto sender, UserDto receiver
    ) throws Exception {
        mockUserServiceClientToReturnSenderAndReceiver(userId, friendId, sender, receiver);
        List<Message> messages = testMessageUtil.createMessagesWithSenderIdAndReceiverId(userId, friendId);

        String jsonResponse = requestChatAndExpectStatus(userId, friendId, 0, OK);

        List<MessageDto> messageDtos = fromJson(jsonResponse, getTypeTokenForMessageDtos());
        assertThat(messageDtos)
                .flatExtracting(MessageDto::sender, MessageDto::receiver)
                .containsOnly(sender, receiver);
    }

    @ParameterizedTest
    @InstancioSource
    void shouldReturnMessageFromChat_whenGivenValidFriendIdAndMessageId(
            Long userId, Long friendId, UserDto sender, UserDto receiver
    ) throws Exception {
        mockUserServiceClientToReturnSenderAndReceiver(userId, friendId, sender, receiver);
        Message message = testMessageUtil.createMessageWithSenderIdAndReceiverId(userId, friendId);

        String jsonResponse = requestMessageFromChatAndExpectStatus(userId, friendId, message.getMessageId(), OK);

        MessageDto messageDto = fromJson(jsonResponse, MessageDto.class);
        assertThat(messageDto.messageId())
                .isEqualTo(message.getMessageId());
    }

    @ParameterizedTest
    @InstancioSource
    void shouldDeleteMessageFromChat_whenGivenValidFriendIdAndMessageId(
            Long userId, Long friendId, UserDto sender, UserDto receiver
    ) throws Exception {
        mockUserServiceClientToReturnSenderAndReceiver(userId, friendId, sender, receiver);
        Message message = testMessageUtil.createMessageWithSenderIdAndReceiverId(userId, friendId);

        deleteMessageFromChatAndExpectStatus(userId, friendId, message.getMessageId(), OK);

        verify(messageRepository).delete(message);
    }

    private String writeMessageAndExpectStatus(String text, Long userId, Long friendId, HttpStatus status) throws Exception {
        ResultActions resultActions = mockMvc.perform(post(WRITE_MESSAGE_URL, friendId)
                .header("User-Id", userId)
                .content(text));
        return getResponseContentWithExpectedStatus(resultActions, status);
    }

    private String requestChatAndExpectStatus(Long userId, Long friendId, Integer page, HttpStatus status) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(GET_CHAT_URL, friendId)
                .header("User-Id", userId)
                .param("page", page.toString()));
        return getResponseContentWithExpectedStatus(resultActions, status);
    }

    private String requestMessageFromChatAndExpectStatus(Long userId, Long friendId, String messageId, HttpStatus status) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(GET_MESSAGE_FROM_CHAT_URL, friendId, messageId)
                .header("User-Id", userId));
        return getResponseContentWithExpectedStatus(resultActions, status);
    }

    private void deleteMessageFromChatAndExpectStatus(Long userId, Long friendId, String messageId, HttpStatus status) throws Exception {
        ResultActions resultActions = mockMvc.perform(delete(DELETE_MESSAGE_FROM_CHAT_URL, friendId, messageId)
                .header("User-Id", userId));
        expectStatus(resultActions, status);
    }

    private void mockUserServiceClientToReturnSenderAndReceiver(Long senderId, Long receiverId, UserDto sender, UserDto receiver) {
        when(userServiceClient.getUserById(senderId)).thenReturn(sender);
        when(userServiceClient.getUserById(receiverId)).thenReturn(receiver);
    }

    private TypeToken<List<MessageDto>> getTypeTokenForMessageDtos() {
        return new TypeToken<>() {
        };
    }
}
