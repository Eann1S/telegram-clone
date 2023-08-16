package com.example.dto.kafka_message;

public record KafkaMessage<T>(
        T content
) {

    public static <T> KafkaMessage<T> of(T content) {
        return new KafkaMessage<>(content);
    }
}
