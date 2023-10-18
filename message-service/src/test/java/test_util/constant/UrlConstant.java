package test_util.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlConstant {

    public static final String WRITE_MESSAGE_URL = "/api/v1/chat/{friendId}/write";
    public static final String GET_CHAT_URL = "/api/v1/chat/{friendId}";
    public static final String GET_MESSAGE_FROM_CHAT_URL = "/api/v1/chat/{friendId}/message/{messageId}";
    public static final String DELETE_MESSAGE_FROM_CHAT_URL = "/api/v1/chat/{friendId}/message/{messageId}/delete";
}
