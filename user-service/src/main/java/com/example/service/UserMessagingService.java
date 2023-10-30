package com.example.service;

import com.example.dto.mq_dto.UpdateDto;

public interface UserMessagingService {

    void send(UpdateDto updateDto);
}
