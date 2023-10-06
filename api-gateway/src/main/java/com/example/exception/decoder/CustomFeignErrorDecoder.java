package com.example.exception.decoder;

import com.example.dto.error.ErrorDto;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.example.json.JsonConverter.fromJson;

@Slf4j
public class CustomFeignErrorDecoder implements ErrorDecoder {

    @SneakyThrows
    @Override
    public Exception decode(String s, Response response) {
        return new ResponseStatusException(
                HttpStatusCode.valueOf(response.status()),
                getErrorMessageFromResponse(response)
        );
    }

    private String getErrorMessageFromResponse(Response response) throws IOException {
        String body = getBodyFromResponseAsString(response);
        ErrorDto errorDto = fromJson(body, ErrorDto.class);
        return errorDto.message();
    }

    private String getBodyFromResponseAsString(Response response) throws IOException {
        InputStream bodyAsStream = response.body().asInputStream();
        return StreamUtils.copyToString(bodyAsStream, StandardCharsets.UTF_8);
    }
}
