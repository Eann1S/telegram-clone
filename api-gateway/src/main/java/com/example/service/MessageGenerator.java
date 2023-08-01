package com.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageGenerator {

    private final Environment environment;

    public String generateMessage(String property) {
        return environment.getProperty(property);
    }

    public String generateMessage(String property, Object... params) {
        String prop = environment.getProperty(property);
        if (prop == null) {
            throw new IllegalArgumentException(String.format("Invalid property name: %s", property));
        }
        return prop.formatted(prop, params);
    }
}
