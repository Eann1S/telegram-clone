package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageGenerator {

    private final Environment environment;

    public String generateMessage(String propertyName) {
        return environment.getProperty(propertyName);
    }

    public String generateMessage(String propertyName, Object... params) {
        String property = environment.getProperty(propertyName);
        if (property == null) {
            throw new IllegalArgumentException("Property %s does not exists".formatted(propertyName));
        }
        return property.formatted(params);
    }
}
