package com.evbackend.handler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.evbackend.model.dto.FaultsDTO;
import com.evbackend.service.FaultService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FaultHandler {

	private final FaultService faultService;

	@Operation(summary = "Get Faults", description = "Retrieve faults", parameters = {
			@Parameter(name = "paramType", in = ParameterIn.QUERY, required = false),
			@Parameter(name = "id", in = ParameterIn.QUERY, required = false) }, responses = {
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = FaultsDTO.class)))) })
	public Mono<ServerResponse> getFaultsFilter(ServerRequest req) {
		Optional<String> id = req.queryParam("id");
		Optional<String> paramType = req.queryParam("paramType");
		if (id.isEmpty()) {
			return faultService.getAllFaults().flatMap(c -> {
				List<FaultsDTO> faults = c.stream()
						.map(i -> FaultsDTO.builder().name(i.getConnector().getChargeStation().getName())
								.connectorId(i.getConnector().getConnectorId())
								.chargeStationId(i.getConnector().getChargeStation().getChargeStationId()).build())
						.collect(Collectors.toList());
				return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(faults));
			});
		} else
			return faultService.getFaultsByIdAndParamType(UUID.fromString(id.get()), paramType.get().toString())
					.flatMap(c -> {
						List<FaultsDTO> faults = c.stream()
								.map(i -> FaultsDTO.builder().name(i.getConnector().getChargeStation().getName())
										.connectorId(i.getConnector().getConnectorId())
										.chargeStationId(i.getConnector().getChargeStation().getChargeStationId())
										.build())
								.collect(Collectors.toList());
						return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
								.body(BodyInserters.fromValue(faults));
					});
	}

}
