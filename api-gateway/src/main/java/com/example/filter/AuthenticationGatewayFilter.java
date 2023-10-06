package com.example.filter;

import com.example.client.AuthenticationServiceClient;
import com.example.exception.MissingAuthorizationHeaderException;
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

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
@Slf4j
public class AuthenticationGatewayFilter implements GatewayFilter {

    private final AuthenticationServiceClient authenticationServiceClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String jwt = getJwtFromRequest(exchange.getRequest());
        return authenticationServiceClient.getIdOfAuthorizedAccount(jwt)
                .flatMap(userId -> {
                    addUserIdHeaderToExchange(exchange, userId);
                    return chain.filter(exchange);
                });
    }

    private void addUserIdHeaderToExchange(ServerWebExchange exchange, String userId) {
        exchange.getRequest()
                .mutate()
                .headers(headers -> headers.add("User-Id", userId))
                .build();
    }

    private String getJwtFromRequest(ServerHttpRequest request) {
        String authorizationHeader = getAuthorizationHeaderFromRequest(request);
        return getJwtFromHeader(authorizationHeader);
    }

    private String getAuthorizationHeaderFromRequest(ServerHttpRequest request) {
        Map<String, String> headers = getHeadersOfRequest(request);
        String authorizationHeader = headers.get(AUTHORIZATION);
        if (authorizationHeader == null) {
            throw new MissingAuthorizationHeaderException();
        }
        return authorizationHeader;
    }

    private Map<String, String> getHeadersOfRequest(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        return headers.toSingleValueMap();
    }

    private String getJwtFromHeader(String header) {
        return header.split(" ")[1].trim();
    }
}
