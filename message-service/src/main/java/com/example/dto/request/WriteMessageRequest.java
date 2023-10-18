package com.example.dto.request;

public record WriteMessageRequest(
        Long senderId,
        Long receiverId,
        String text
) {
    public static WriteMessageRequest of(Long senderId, Long receiverId, String text) {
        return new WriteMessageRequest(senderId, receiverId, text);
    }
}
