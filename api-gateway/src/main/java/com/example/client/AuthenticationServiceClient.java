package com.example.client;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@Component
@ReactiveFeignClient(name = "${services.authentication-service.name}")
public interface AuthenticationServiceClient {

    @GetMapping("/authorized-account/id/{jwt}")
    Mono<String> getIdOfAuthorizedAccount(@PathVariable String jwt);
}
