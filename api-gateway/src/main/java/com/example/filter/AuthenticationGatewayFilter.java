package com.example.filter;

import com.example.client.AuthenticationServiceClient;
import com.example.exception.EmptyAuthorizationHeaderException;
import com.example.service.MessageGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Slf4j
public class AuthenticationGatewayFilter implements GatewayFilter {
    private final AuthenticationServiceClient authenticationServiceClient;
    private final MessageGenerator messageGenerator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return authenticationServiceClient.getUserEmail(extractJwtFromHeader(
                        getAuthorizationHeaderFromRequest(exchange.getRequest()).orElseThrow(() -> {
                            throw new EmptyAuthorizationHeaderException(UNAUTHORIZED, messageGenerator.generateMessage("error.unauthorized"));
                        })
                ))
                .flatMap(userEmail -> {
                    exchange.getRequest()
                            .mutate()
                            .headers(headers -> headers.add("User-Email", userEmail))
                            .build();
                    return chain.filter(exchange);
                });
    }

    private Optional<String> getAuthorizationHeaderFromRequest(ServerHttpRequest request) {
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        return Optional.ofNullable(headers.get(HttpHeaders.AUTHORIZATION));
    }

    private String extractJwtFromHeader(String header) {
        return header.split(" ")[1].trim();
    }
}
