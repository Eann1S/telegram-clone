package test_util.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UrlConstant {

    public final String USER_URL = "/api/v1/user";
    public final String USER_URL_WITH_VARIABLE = "/api/v1/user/{variable}";
    public final String USERS_URL = "/api/v1/users";
    public final String UPDATE_USER_URL = "/api/v1/user/{id}/update";
}
