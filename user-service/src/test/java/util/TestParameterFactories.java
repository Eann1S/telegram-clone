package util;

import com.example.dto.response.UserDto;
import com.example.entity.User;

import java.util.stream.Stream;

import static util.TestUserCreator.createUserWithAllFields;

public class TestParameterFactories {

    private TestParameterFactories() {
    }

    public static Stream<UserDto> userDtoFactory() {
        return Stream.of(
                UserDto.of("1", "username", "email", "123")
        );
    }

    public static Stream<User> userFactory() {
        return Stream.of(
                createUserWithAllFields("1", "username", "email", "123")
        );
    }
}
