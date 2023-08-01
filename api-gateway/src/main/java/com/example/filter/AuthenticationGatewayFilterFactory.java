package com.example.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final AuthenticationGatewayFilter authenticationServiceClient;

    @Autowired
    public AuthenticationGatewayFilterFactory(AuthenticationGatewayFilter authenticationServiceClient) {
        super(Config.class);
        this.authenticationServiceClient = authenticationServiceClient;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return authenticationServiceClient;
    }

    static class Config {
    }
}
