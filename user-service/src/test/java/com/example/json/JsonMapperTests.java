package com.example.json;

import com.example.dto.response.UserDto;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class JsonMapperTests {

    @ParameterizedTest
    @MethodSource("userDtoJsonFactory")
    void shouldConvertObjectToJson(String json) {
        assertThat(json).contains("1", "username", "email", "123");
    }

    @ParameterizedTest
    @MethodSource("userDtoJsonFactory")
    void shouldConvertObjectFromJson(String json) {
        UserDto userDto = JsonMapper.fromJson(json, UserDto.class);

        assertThat(userDto)
                .extracting(UserDto::id, UserDto::username, UserDto::email, UserDto::phoneNumber)
                .containsExactly("1", "username", "email", "123");
    }

    @ParameterizedTest
    @MethodSource("userDtoJsonFactory")
    void shouldConvertObjectFromJsonUsingTypeToken(String json) {
        UserDto userDto = JsonMapper.fromJson(json, new TypeToken<>() {});

        assertThat(userDto)
                .extracting(UserDto::id, UserDto::username, UserDto::email, UserDto::phoneNumber)
                .containsExactly("1", "username", "email", "123");
    }

    static Stream<String> userDtoJsonFactory() {
        return Stream.of(
                JsonMapper.toJson(UserDto.of("1", "username", "email", "123"))
        );
    }
}