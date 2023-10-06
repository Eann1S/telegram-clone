package test_util;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestControllerUtil {

    public static String getResponseContentWithExpectedStatus(ResultActions resultActions, HttpStatus status) throws Exception {
        expectStatus(resultActions, status);
        return getContentAsStringFrom(resultActions);
    }

    private static void expectStatus(ResultActions resultActions, HttpStatus status) throws Exception {
        resultActions.andExpect(status().is(status.value()));
    }

    private static String getContentAsStringFrom(ResultActions resultActions) throws UnsupportedEncodingException {
        return resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }
}
