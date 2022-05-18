package com.evbackend.controller;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.evbackend.handler.TransactionHandler;

@Configuration
public class TransactionController {

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> allTransactionsRouterFunction(TransactionHandler handler) {
        return route().GET("/api/transactions", accept(APPLICATION_JSON), handler::getAllTransactions,
                ops -> ops.beanClass(TransactionHandler.class).beanMethod("getAllTransactions")).build();
    }
    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> allTransactionsForUserRouterFunction(TransactionHandler handler) {
        return route().GET("/api/transactions/user", accept(APPLICATION_JSON), handler::getAllTransactionsForUser,
                ops -> ops.beanClass(TransactionHandler.class).beanMethod("getAllTransactionsForUser")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> getTransactionsByTimePeriod(TransactionHandler handler) {
        return route().POST("/api/transactions", accept(APPLICATION_JSON), handler::getTransactionsByTimePeriod,
                ops -> ops.beanClass(TransactionHandler.class).beanMethod("getTransactionsByTimePeriod")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> getTransactionsByTimePeriodForUser(TransactionHandler handler) {
        return route().POST("/api/transactions/user", accept(APPLICATION_JSON), handler::getTransactionsByTimePeriodForUser,
                ops -> ops.beanClass(TransactionHandler.class).beanMethod("getTransactionsByTimePeriodForUser")).build();
    }
    
    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> getTransactionsByVehicleId(TransactionHandler handler) {
        return route().GET("/api/transactions/vehicle/{id}", accept(APPLICATION_JSON), handler::getTransactionsByVehicleId, ops -> ops.beanClass(TransactionHandler.class).beanMethod("getTransactionsByVehicleId")).build();
    }
    
    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> getTransactionsByVehicleIdAndTimePeriod(TransactionHandler handler) {
        return route().POST("/api/transactions/vehicle/{id}", accept(APPLICATION_JSON), handler::getTransactionsByVehicleIdAndTimePeriod, ops -> ops.beanClass(TransactionHandler.class).beanMethod("getTransactionsByVehicleIdAndTimePeriod")).build();
    }
    
}
