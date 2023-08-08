package com.example.generator;

import com.example.entity.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.neo4j.core.schema.IdGenerator;

import java.util.UUID;

public class UserIdGenerator implements IdGenerator<String> {

    @Override
    @NotNull
    public String generateId(@NotNull String primaryLabel, @NotNull Object entity) {
        User user = (User) entity;
        return user.getId() != null ? user.getId() : UUID.randomUUID().toString();
    }
}
