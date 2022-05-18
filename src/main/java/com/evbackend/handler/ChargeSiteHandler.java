package com.evbackend.handler;

import static java.time.ZoneOffset.UTC;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.evbackend.commands.CreateChargeSiteCommand;
import com.evbackend.commands.FullUpdateChargeSiteCommand;
import com.evbackend.mapping.AddressMapping;
import com.evbackend.mapping.ChargeSiteMapping;
import com.evbackend.mapping.UserMapping;
import com.evbackend.model.ApiResponse;
import com.evbackend.model.AppConstants;
import com.evbackend.model.chargestation.ChargeSite;
import com.evbackend.model.chargestation.ChargeSiteAll;
import com.evbackend.model.chargestation.ChargeSiteItem;
import com.evbackend.security.JWTUtil;
import com.evbackend.service.ChargeSiteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ChargeSiteHandler {

    private final ChargeSiteService chargeSiteService;

    @Autowired
    private JWTUtil jwtUtil;


    @Operation(
            summary = "All Charge Sites",
            description = "Retrieve all charge station",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChargeSiteItem.class))))})
    public Mono<ServerResponse> getAllChargeSites(ServerRequest req) {
        return chargeSiteService
                .getAllChargeSites()
                .flatMap( c ->
                        {
                            List<ChargeSiteItem> chargeItems = c.stream().map(i -> ChargeSiteItem.builder()
                                    .siteName(i.getSiteName())
                                    .siteId(i.getSiteId()).build())
                            .collect(Collectors.toList());
                            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(chargeItems));
                        }
                );
    }

    @Operation(
            summary = "Complete Charge Site",
            description = "Retrieve all charge station with complete info",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChargeSiteAll.class))))})
    public Mono<ServerResponse> getAllChargeSitesAll(ServerRequest req) {
        return chargeSiteService
                .getAllChargeSites()
                .flatMap( c ->
                        {
                            List<ChargeSiteAll> chargeItems = c.stream().map(i -> ChargeSiteAll.builder()
                                            .siteName(i.getSiteName())
                                            .activeSite(i.getActiveSite())
                                            .createdAt(i.getCreatedAt().toEpochSecond(UTC))
                                            .userItem(UserMapping.mapUser(i.getCreatedBy()))
                                            .addressDetails(AddressMapping.mapAddressToDetails(i.getAddress()))
                                            .siteId(i.getSiteId()).build())
                                    .collect(Collectors.toList());
                            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(chargeItems));
                        }
                );
    }



    @Operation(
            summary = "Create Charge Site",
            description = "Create a new charge site",
            requestBody = @RequestBody(content =@Content(schema = @Schema(implementation = CreateChargeSiteCommand.class) )),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201",
                    description = "successful operation",
                    content = @Content(schema = @Schema(description = "Identifier for charge site",implementation = String.class)))})
    public Mono<ServerResponse> createChargeSite(ServerRequest req) {
        String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));

        return req.bodyToMono(CreateChargeSiteCommand.class)
                .flatMap(s -> {
                    return chargeSiteService.createChargeSite(userId, s).flatMap(p -> ServerResponse.created(URI.create("/api/chargesite" + p)).build());

                })
                .switchIfEmpty(ServerResponse.badRequest()
                        .body(BodyInserters.fromObject(new ApiResponse(400, AppConstants.ERROR_MESSAGE_EMPTY_REQUEST_BODY, null))));
    }

    @Operation(
            summary = "Get Charge Site",
            description = "Retrieve charge site",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(implementation = ChargeSite.class)))})
    public Mono<ServerResponse> getChargeSite(ServerRequest req) {
        String uuidString = req.pathVariable("id");
        UUID uuid = UUID.fromString(uuidString);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(chargeSiteService.getChargeSiteById(uuid)));
    }

    @Operation(
            summary = "Full Update Charge Site",
            description = "Create a new charge site",
            requestBody = @RequestBody(content =@Content(schema = @Schema(implementation = FullUpdateChargeSiteCommand.class) )),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(description = "Identifier for charge site",implementation = String.class)))})
    public Mono<ServerResponse> fullUpdateChargeSite(ServerRequest req) {
        String uuidString = req.pathVariable("id");
        String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));

        return req.bodyToMono(FullUpdateChargeSiteCommand.class)
                .flatMap(s -> {
                    return chargeSiteService.fullUpdateChargeSiteById(UUID.fromString(uuidString), s).flatMap(p -> ServerResponse.created(URI.create("/api/chargesite" + p)).build());

                });
    }
    
	@Operation(summary = "Charge Site Filter", description = "Charge Site Filter", 
			parameters = @Parameter(name = "accountId", in = ParameterIn.QUERY, description = "Optional AccountId filter", required = false, 
			content = @Content(schema = @Schema(implementation = String.class))), responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(description = "Identifier for charge site", implementation = ChargeSiteAll.class))) })
	public Mono<ServerResponse> chargeSitesFilter(ServerRequest req) {
		Optional<String> uuidString = req.queryParam("accountId");
		if (uuidString.isEmpty()) {
			return chargeSiteService.getAllChargeSites().flatMap(c -> {
				List<ChargeSiteAll> chargeItems = c.stream()
						.map(i -> ChargeSiteMapping.mapChargeSiteAll(i))
						.collect(Collectors.toList());
				return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(chargeItems));
			});
		} else {
			return chargeSiteService.getChargeSiteByAccountId(UUID.fromString(uuidString.get())).flatMap(c -> {
				List<ChargeSiteAll> chargeItems = c.stream()
						.map(i -> ChargeSiteMapping.mapChargeSiteAll(i))
						.collect(Collectors.toList());
				if(!chargeItems.isEmpty()) {
					return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
							.body(BodyInserters.fromValue(chargeItems));
				} else {
					return ServerResponse.badRequest().syncBody("Account Id is invalid");
				}
			});
		}
	}
    

}
