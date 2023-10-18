package com.example.exception;

import static com.example.message.ErrorMessage.MESSAGE_NOT_FOUND;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(Long senderId, Long receiverId, String messageId) {
        super(MESSAGE_NOT_FOUND.formatWith(senderId, receiverId, messageId));
    }
}
