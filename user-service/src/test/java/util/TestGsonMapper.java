package util;

import com.example.dto.response.UserDto;
import com.google.gson.Gson;

public class TestGsonMapper {

    private static final Gson gson = new Gson();

    public static String mapUserDtoToJson(UserDto userDto) {
        return gson.toJson(userDto);
    }
}
