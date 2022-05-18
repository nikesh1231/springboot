package com.evbackend.controller;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.evbackend.handler.VehicleHandler;

@Configuration
public class VehicleController {

    // manufacturer id
    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> manufacturer(VehicleHandler handler) {
        return route().GET("/api/vehicle/manufacturer/{id}", accept(APPLICATION_JSON), handler::getManufacturer, ops -> ops.beanClass(VehicleHandler.class).beanMethod("getManufacturer")).build();
    }

    // all vehicles for user
    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> allActiveVehiclesForUser(VehicleHandler handler) {
        return route().GET("/api/vehicle/active", accept(APPLICATION_JSON), handler::getActiveVehiclesForUser, ops -> ops.beanClass(VehicleHandler.class).beanMethod("getVehiclesForUser")).build();
    }

    // all manufacturers
    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> allManufacturers(VehicleHandler handler) {
        return route().GET("/api/vehicle/manufacturer", accept(APPLICATION_JSON), handler::getAllManufacturers, ops -> ops.beanClass(VehicleHandler.class).beanMethod("getAllManufacturers")).build();
    }

    // All models
    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> allModelsForManufacturer(VehicleHandler handler) {
        return route().GET("/api/vehicle/manufacturer/{id}/model", accept(APPLICATION_JSON), handler::getModelsForManufacturer, ops -> ops.beanClass(VehicleHandler.class).beanMethod("getModelsForManufacturer")).build();
    }

    // Model
    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> modelsForManufacturer(VehicleHandler handler) {
        return route().GET("/api/vehicle/model/{id}", accept(APPLICATION_JSON), handler::getModel, ops -> ops.beanClass(VehicleHandler.class).beanMethod("getModel")).build();
    }


    // Years for model
    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> yearsForModel(VehicleHandler handler) {
        return route().GET("/api/vehicle/model/{id}/year", accept(APPLICATION_JSON), handler::getYearsForModel, ops -> ops.beanClass(VehicleHandler.class).beanMethod("getYearsForModel")).build();
    }

    // Version for year
    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> versionsForModelAndYear(VehicleHandler handler) {
        return route().GET("/api/vehicle/model/{model_id}/year/{year}/version", accept(APPLICATION_JSON), handler::getVersionForModelAndYear, ops -> ops.beanClass(VehicleHandler.class).beanMethod("getVersionForModelAndYear")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> version(VehicleHandler handler) {
        return route().GET("/api/vehicle/version/{id}", accept(APPLICATION_JSON), handler::getVersion, ops -> ops.beanClass(VehicleHandler.class).beanMethod("getVersion")).build();
    }

    //
    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> registerVehicle(VehicleHandler handler) {
        return route().POST("/api/vehicle/register", accept(APPLICATION_JSON), handler::register, ops -> ops.beanClass(VehicleHandler.class).beanMethod("register")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> deregisterVehicle(VehicleHandler handler) {
        return route().GET("/api/vehicle/deregister", accept(APPLICATION_JSON), handler::deregister, ops -> ops.beanClass(VehicleHandler.class).beanMethod("getAllRoles")).build();
    }


}
