package com.evbackend.controller;

import com.evbackend.handler.AccountHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class AccountController {

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> allAccountsRouterFunction(AccountHandler handler) {
        return route().GET("/api/accounts", accept(APPLICATION_JSON), handler::getAllAccounts, ops -> ops.beanClass(AccountHandler.class).beanMethod("getAllAccounts")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> createAccountRouterFunction(AccountHandler handler) {
        return route().POST("/api/accounts/create", accept(APPLICATION_JSON), handler::create, ops -> ops.beanClass(AccountHandler.class).beanMethod("create")).build();
    }

}
