package com.example.json;

import com.example.dto.UserDto;
import com.google.gson.reflect.TypeToken;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(InstancioExtension.class)
class JsonConverterTests {

    @ParameterizedTest
    @InstancioSource
    void shouldConvertObjectToJson(UserDto userDto) {
        String json = JsonConverter.toJson(userDto);

        assertThat(json)
                .contains(userDto.id().toString(), userDto.email(), userDto.username());
    }

    @ParameterizedTest
    @InstancioSource
    void shouldConvertObjectFromJson(UserDto userDto) {
        String json = JsonConverter.toJson(userDto);

        UserDto actualUserDto = JsonConverter.fromJson(json, UserDto.class);

        assertThat(actualUserDto).isEqualTo(userDto);
    }

    @ParameterizedTest
    @InstancioSource
    void shouldConvertParametrizedObjectFromJson(List<UserDto> userDtos) {
        String json = JsonConverter.toJson(userDtos);

        List<UserDto> actualUserDtos = JsonConverter.fromJson(json, new TypeToken<>() {});

        assertThat(actualUserDtos).containsExactlyElementsOf(userDtos);
    }
}