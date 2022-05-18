package com.evbackend.controller;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.evbackend.handler.ChargeStationHandler;

@Configuration
public class ChargeStationController {


	@RouterOperation
	@Bean
	public RouterFunction<ServerResponse> getAllChargeStations(ChargeStationHandler handler) {
		return route().GET("/api/chargestation", accept(APPLICATION_JSON), handler::getAllChargeStations, ops -> ops.beanClass(ChargeStationHandler.class).beanMethod("getAllChargeStations")).build();
	}

	@RouterOperation
	@Bean
	public RouterFunction<ServerResponse> getChargeStation(ChargeStationHandler handler) {
		return route().GET("/api/chargestation/{id}", accept(APPLICATION_JSON), handler::getChargeStation, ops -> ops.beanClass(ChargeStationHandler.class).beanMethod("getChargeStation")).build();
	}

	@RouterOperation
	@Bean
	public RouterFunction<ServerResponse> getChargeStationDetail(ChargeStationHandler handler) {
		return route().GET("/api/chargestation/{id}/details", accept(APPLICATION_JSON), handler::getChargeStationDetail, ops -> ops.beanClass(ChargeStationHandler.class).beanMethod("getChargeStationDetail")).build();
	}

	@RouterOperation
	@Bean
	public RouterFunction<ServerResponse> createChargeStation(ChargeStationHandler handler) {
		return route().POST("/api/chargestation", accept(APPLICATION_JSON), handler::createChargeStation, ops -> ops.beanClass(ChargeStationHandler.class).beanMethod("createChargeStation")).build();
	}

	@RouterOperation
	@Bean
	public RouterFunction<ServerResponse> fullUpdateChargeStationById(ChargeStationHandler handler) {
		return route().PUT("/api/chargestation/{id}", accept(APPLICATION_JSON), handler::fullUpdateChargeStation, ops -> ops.beanClass(ChargeStationHandler.class).beanMethod("fullUpdateChargeStation")).build();
	}

	@RouterOperation
	@Bean
	public RouterFunction<ServerResponse> createReview(ChargeStationHandler handler) {
		return route().POST("/api/chargestation/review", accept(APPLICATION_JSON), handler::createChargeStationReview, ops -> ops.beanClass(ChargeStationHandler.class).beanMethod("createChargeStationReview")).build();
	}

	@RouterOperation
	@Bean
	public RouterFunction<ServerResponse> addFavourite(ChargeStationHandler handler) {
		return route().POST("/api/chargestation/addfavorite", accept(APPLICATION_JSON), handler::addFavouriteChargeStation, ops -> ops.beanClass(ChargeStationHandler.class).beanMethod("addFavouriteChargeStation")).build();
	}

	@RouterOperation
	@Bean
	public RouterFunction<ServerResponse> removeFavourite(ChargeStationHandler handler) {
		return route().POST("/api/chargestation/removefavorite", accept(APPLICATION_JSON), handler::removeFavouriteChargeStation, ops -> ops.beanClass(ChargeStationHandler.class).beanMethod("removeFavouriteChargeStation")).build();
	}

	@RouterOperation
	@Bean
	public RouterFunction<ServerResponse> getFavourites(ChargeStationHandler handler) {
		return route().POST("/api/chargestation/favorites", accept(APPLICATION_JSON), handler::favouriteChargeStations, ops -> ops.beanClass(ChargeStationHandler.class).beanMethod("favouriteChargeStations")).build();
	}

}
