package com.example.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {

    private final AuthenticationGatewayFilter authenticationGatewayFilter;

    @Autowired
    public AuthenticationGatewayFilterFactory(AuthenticationGatewayFilter authenticationGatewayFilter) {
        super(Config.class);
        this.authenticationGatewayFilter = authenticationGatewayFilter;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return authenticationGatewayFilter;
    }

    static class Config {
    }
}
