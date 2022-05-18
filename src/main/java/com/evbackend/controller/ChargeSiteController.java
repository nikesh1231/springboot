package com.evbackend.controller;

import com.evbackend.handler.ChargeSiteHandler;
import com.evbackend.service.ChargeSiteService;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@RestController
@RequestMapping("/api/")
public class ChargeSiteController {

    @Autowired
    private ChargeSiteService chargeSiteService;


    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> getAllChargeSites(ChargeSiteHandler handler) {
        return route().GET("/api/chargesite", accept(APPLICATION_JSON), handler::getAllChargeSites, ops -> ops.beanClass(ChargeSiteHandler.class).beanMethod("getAllChargeSites")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> getAllChargeSitesAll(ChargeSiteHandler handler) {
        return route().GET("/api/chargesite/all", accept(APPLICATION_JSON), handler::chargeSitesFilter, ops -> ops.beanClass(ChargeSiteHandler.class).beanMethod("chargeSitesFilter")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> getAllCharge(ChargeSiteHandler handler) {
        return route().GET("/api/chargesite/{id}", accept(APPLICATION_JSON), handler::getChargeSite, ops -> ops.beanClass(ChargeSiteHandler.class).beanMethod("getChargeSite")).build();
    }

    @RouterOperation
    @Bean
    @ResponseStatus(HttpStatus.CREATED)
    public RouterFunction<ServerResponse> createChargeSite(ChargeSiteHandler handler) {
        return route().POST("/api/chargesite", accept(APPLICATION_JSON), handler::createChargeSite, ops -> ops.beanClass(ChargeSiteHandler.class).beanMethod("createChargeSite")).build();
    }

    @RouterOperation
    @Bean
    public RouterFunction<ServerResponse> fullUpdateChargeSite(ChargeSiteHandler handler) {
        return route().PUT("/api/chargesite", accept(APPLICATION_JSON), handler::fullUpdateChargeSite, ops -> ops.beanClass(ChargeSiteHandler.class).beanMethod("fullUpdateChargeSite")).build();
    }


    /*
    @PatchMapping("chargesite/{id}")
    public ResponseEntity<ChargeSite> partialUpdateChargeSiteById(@PathVariable UUID id,
                                                                  @RequestBody Map<Object, Object> fields) {
        ChargeSite chargeSite = chargeSiteService.partialUpdateChargeSiteById(id, fields).toProcessor()
                .block(Duration.ofMillis(1000));
        return new ResponseEntity<ChargeSite>(chargeSite, HttpStatus.OK);
    }*/

}
