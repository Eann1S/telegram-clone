package com.example.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MessageGeneratorTests {

    @Test
    void shouldGenerateEntityNotFoundMessage() {
        String actualMessage = MessageGenerator.generateEntityNotFoundMessage( "invalid property");

        assertThat(actualMessage).isEqualTo("Entity with property invalid property does not exist");
    }
}