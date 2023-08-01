package com.example.config;

import com.example.exception.CustomFeignErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

@PropertySource({
        "classpath:${envTarget:errors}.properties",
})
@Configuration
public class AppConfig {

    @Bean
    public HttpMessageConverters httpMessageConverters() {
        return new HttpMessageConverters(new GsonHttpMessageConverter());
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }
}
