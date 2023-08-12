package util;

import com.example.entity.User;
import org.instancio.Instancio;

import static org.instancio.Select.field;

public class TestUserCreator {

    private TestUserCreator() {
    }

    public static User createUserWithAllFields(String id, String username, String email, String phoneNumber) {
        return User.builder()
                .id(id)
                .username(username)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }

    public static User createUserWithId(String id) {
        return Instancio.of(User.class)
                .set(field(User::getId), id)
                .create();
    }

    public static User createUserWithEmail(String email) {
        return Instancio.of(User.class)
                .set(field(User::getEmail), email)
                .create();
    }

    public static User createUserWithPhoneNumber(String phoneNumber) {
        return Instancio.of(User.class)
                .set(field(User::getPhoneNumber), phoneNumber)
                .create();
    }
}
