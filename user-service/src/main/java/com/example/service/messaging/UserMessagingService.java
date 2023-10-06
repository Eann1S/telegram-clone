package com.example.service.messaging;

import com.example.dto.mq_dto.UpdateDto;

public interface UserMessagingService {

    void send(UpdateDto updateDto);
}
