package com.evbackend.controller;

import com.evbackend.handler.UserRoleHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class UserRolesController {


    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> allUserRoles(UserRoleHandler handler) {
        return route().GET("/api/userroles", accept(APPLICATION_JSON), handler::getAllRoles, ops -> ops.beanClass(UserRoleHandler.class).beanMethod("getAllRoles")).build();
    }
}
