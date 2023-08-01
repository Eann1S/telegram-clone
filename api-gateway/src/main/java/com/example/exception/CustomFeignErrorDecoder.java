package com.example.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;

public class CustomFeignErrorDecoder implements ErrorDecoder {

    @SneakyThrows
    @Override
    public Exception decode(String s, Response response) {
        return new ResponseStatusException(
                HttpStatusCode.valueOf(response.status()),
                StreamUtils.copyToString(response.body().asInputStream(), StandardCharsets.UTF_8)
        );
    }
}
