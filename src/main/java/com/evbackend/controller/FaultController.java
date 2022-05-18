package com.evbackend.controller;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.evbackend.handler.FaultHandler;

@Configuration
public class FaultController {

	@RouterOperation
	@Bean
	public RouterFunction<ServerResponse> getFaultsFilter(FaultHandler handler) {
		return route().GET("/api/faults", accept(APPLICATION_JSON), handler::getFaultsFilter, ops -> ops.beanClass(FaultHandler.class).beanMethod("getFaultsFilter")).build();
	}
	
}
