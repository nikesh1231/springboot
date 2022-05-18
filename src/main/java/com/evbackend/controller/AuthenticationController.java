package com.evbackend.controller;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.evbackend.handler.UserHandler;

@Configuration
public class AuthenticationController {

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> loginRouterFunction(UserHandler handler) {
        return route().POST("/api/login", accept(APPLICATION_JSON), handler::login, ops -> ops.beanClass(UserHandler.class).beanMethod("login")).build();

    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> managementLoginRouterFunction(UserHandler handler) {
        return route().POST("/api/login/management", accept(APPLICATION_JSON), handler::loginManagementApp, ops -> ops.beanClass(UserHandler.class).beanMethod("loginManagementApp")).build();

    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> refreshTokenRouterFunction(UserHandler handler) {
        return route().POST("/api/auth", accept(APPLICATION_JSON), handler::updateToken, ops -> ops.beanClass(UserHandler.class).beanMethod("updateToken")).build();

    }

}
