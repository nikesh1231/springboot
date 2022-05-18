package com.evbackend.controller;


import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import com.evbackend.commands.CreateUserCommand;
import com.evbackend.model.users.User;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.evbackend.handler.UserHandler;
import com.evbackend.service.UserService;

import java.time.Duration;
import java.util.UUID;

@Configuration
@RestController
public class UserController {
	
	@Autowired
	private UserService userService;

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> allUserRouterFunction(UserHandler handler) {
        return route().GET("/api/users", accept(APPLICATION_JSON), handler::getAllUsers, ops -> ops.beanClass(UserHandler.class).beanMethod("getAllUsers")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler handler) {
        return route().GET("/api/users/details", accept(APPLICATION_JSON), handler::getUser, ops -> ops.beanClass(UserHandler.class).beanMethod("getUser")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> allActiveUserRouterFunction(UserHandler handler) {
        return route().GET("/api/users/activeusers", accept(APPLICATION_JSON), handler::getAllActiveUsers, ops -> ops.beanClass(UserHandler.class).beanMethod("getAllActiveUsers")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> createUserAdminRouterFunction(UserHandler handler) {
        return route().POST("/api/users/admincreate", accept(APPLICATION_JSON), handler::adminCreate, ops -> ops.beanClass(UserHandler.class).beanMethod("adminCreate")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> userSignUpRouterFunction(UserHandler handler) {
        return route().POST("/api/users/signup", accept(APPLICATION_JSON), handler::signup, ops -> ops.beanClass(UserHandler.class).beanMethod("signup")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> userPartialUpdateRouterFunction(UserHandler handler) {
        return route().PATCH("/api/users/{id}", accept(APPLICATION_JSON), handler::partialUpdateUserById, ops -> ops.beanClass(UserHandler.class).beanMethod("partialUpdateUserById")).build();
    }


    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> fullUpdateUserById(UserHandler handler) {
        return route().PUT("/api/users/{id}", accept(APPLICATION_JSON), handler::fullUpdateUserById, ops -> ops.beanClass(UserHandler.class).beanMethod("fullUpdateUserById")).build();

    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> inactivateUser(UserHandler handler) {
        return route().PATCH("/api/users/deactivate/{id}", accept(APPLICATION_JSON), handler::inactivateUser
                ,ops -> ops.beanClass(UserHandler.class).beanMethod("inactivateUser")).build();
    }

}
