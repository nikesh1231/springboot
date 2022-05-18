package com.evbackend.handler;


import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.evbackend.commands.CreateChargeStationCommand;
import com.evbackend.commands.CreateReviewCommand;
import com.evbackend.commands.FavoriteChargeStationCommand;
import com.evbackend.mapping.ChargeStationMapping;
import com.evbackend.model.ApiResponse;
import com.evbackend.model.AppConstants;
import com.evbackend.model.FavoriteChargeStation;
import com.evbackend.model.address.AddressDetails;
import com.evbackend.model.chargestation.ChargeStation;
import com.evbackend.model.chargestation.ChargeStationDetails;
import com.evbackend.model.chargestation.ChargeStationItem;
import com.evbackend.model.chargestation.Connector;
import com.evbackend.model.chargestation.Review;
import com.evbackend.security.JWTUtil;
import com.evbackend.service.ChargeSiteService;
import com.evbackend.service.ChargeStationService;
import com.evbackend.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.Parameter;

@Component
@RequiredArgsConstructor
public class ChargeStationHandler {

    private final ChargeStationService chargeStationService;

    private final ChargeSiteService chargeSiteService;

    private final ReviewService reviewService;

    @Autowired
    private JWTUtil jwtUtil;
    @Operation(
            summary = "All Charge Stations",
            description = "Retrieve all charge station",
			        parameters = { @Parameter(name = "latitude", in = ParameterIn.QUERY, description = "Optional latitude filter", required = false, content = @Content(schema = @Schema(implementation = String.class))),
                                    @Parameter(name = "longitude", in = ParameterIn.QUERY, description = "Optional longitude filter", required = false, content = @Content(schema = @Schema(implementation = String.class))) },
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChargeStationItem.class))))})
    public Mono<ServerResponse> getAllChargeStations(ServerRequest req) {
            Optional<String> latitudeReq = req.queryParam(AppConstants.LATITUDE);
            Optional<String> longitudeReq = req.queryParam(AppConstants.LONGITUDE);
            if (latitudeReq.isEmpty() ^ longitudeReq.isEmpty()) {
                    return ServerResponse.badRequest().body(
                                    BodyInserters.fromValue(new ApiResponse(400,
                                                    "Latitude and longitude both must be required", null)));
            }
            Double latitude = null;
            Double longitude = null;
            try {
                    latitude = latitudeReq.isPresent() ? Double.valueOf(latitudeReq.get()) : null;
                    longitude = longitudeReq.isPresent() ? Double.valueOf(longitudeReq.get()) : null;
            } catch (Exception e) {
                    return ServerResponse.badRequest().body(
                                    BodyInserters.fromValue(new ApiResponse(400,
                                                    "Number formate Exception, Please provide long type value", null)));
            }

        return chargeStationService
                .getAllChargeStations(latitude, longitude)
                .flatMap( c ->
                        {
                            List<ChargeStationItem> chargeItems = c.stream().map(i -> ChargeStationItem.builder()
                                    .stationName(i.getName())
                                    .stationId(i.getChargeStationId())
                                    .siteName(i.getChargeSite().getSiteName())
                                    .addressDetails(AddressDetails.builder()
                                            .longitude(i.getChargeSite().getAddress().getLongitude())
                                            .latitude(i.getChargeSite().getAddress().getLatitude())
                                            .country(i.getChargeSite().getAddress().getCountry())
                                            .locality(i.getChargeSite().getAddress().getCountry())
                                            .subAdministrativeArea(i.getChargeSite().getAddress().getSubAdministrativeArea())
                                            .administrativeArea(i.getChargeSite().getAddress().getAdministrativeArea())
                                            .postCode(i.getChargeSite().getAddress().getPostCode())
                                            .streetAddress(i.getChargeSite().getAddress().getStreetAddress())
                                            .build())
                                    .siteId(i.getChargeStationId()).build()
                            ).collect(Collectors.toList());
                            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(chargeItems));
                        }
                        );
    }

    @Operation(
            summary = "Get Charge Station",
            description = "Retrieve charge station",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(implementation = ChargeStation.class)))})
    public Mono<ServerResponse> getChargeStation(ServerRequest req) {
        String uuidString = req.pathVariable("id");
        UUID uuid = UUID.fromString(uuidString);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(chargeStationService.getChargeStationById(uuid)));
    }


    @Operation(
            summary = "Get Charge Station Details",
            description = "Retrieve charge station details",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(implementation = ChargeStationDetails.class)))})
    public Mono<ServerResponse> getChargeStationDetail(ServerRequest req) {
        String uuidString = req.pathVariable("id");
        UUID uuid = UUID.fromString(uuidString);

        Mono<ChargeStation> chargeStation = chargeStationService.getChargeStationById(uuid);
        Mono<List<Review>> chargeStationReviews = reviewService.getReviewsForChargeStation(uuid);
        Mono<List<Connector>> connectors = chargeStationService.getConnectorsForChargeStationId((uuid));

        return Mono.zip(chargeStation, chargeStationReviews, connectors)
                .flatMap(u -> {
                    ChargeStationDetails chargeStationDetails = ChargeStationMapping.createChargeStationDetails(u.getT1(), u.getT2(), u.getT3());
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(chargeStationDetails));
                });
    }

    @Operation(
            summary = "Create Charge Station",
            description = "Create a new charge station",
            requestBody = @RequestBody(content =@Content(schema = @Schema(implementation = CreateChargeStationCommand.class) )),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(description = "Identifier for charge station",implementation = ChargeStationItem.class)))})
    public Mono<ServerResponse> createChargeStation(ServerRequest req) {
        String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));
		return req
				.bodyToMono(CreateChargeStationCommand.class).flatMap(a -> ServerResponse.status(HttpStatus.CREATED)
						.body(chargeStationService.createChargeStation(userId, a), ChargeStationItem.class))
				.switchIfEmpty(ServerResponse.notFound().build());
    }

    @Operation(
            summary = "Create Charge Station Review",
            description = "Create a new charge station review",
            requestBody = @RequestBody(content =@Content(schema = @Schema(implementation = CreateReviewCommand.class) )),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(description = "Identifier for charge station review",implementation = String.class)))})
    public Mono<ServerResponse> createChargeStationReview(ServerRequest req) {
        String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));

        return req.bodyToMono(CreateReviewCommand.class)
                .flatMap(s -> {
                    return reviewService.createReview(userId, s).flatMap(p -> ServerResponse.created(URI.create("/api/chargestations/review" + p)).build());

                });
    }

    @Operation(
            summary = "Full Update Charge Station",
            description = "Updates all fields in a charge station",
            requestBody = @RequestBody(content =@Content(schema = @Schema(implementation = CreateChargeStationCommand.class) )),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation")})
    public Mono<ServerResponse> fullUpdateChargeStation(ServerRequest req) {
        String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));
        String uuidString = req.pathVariable("id");
        return req.bodyToMono(CreateChargeStationCommand.class)
                .flatMap(s -> {
                    return chargeStationService.fullUpdateChargeStationById(userId, UUID.fromString(uuidString), s).flatMap(p -> ServerResponse.ok().build());

                });
    }

    @Operation(
            summary = "Add Favourite Charge Station",
            description = "Add a favourite charge station",
            requestBody = @RequestBody(content =@Content(schema = @Schema(implementation = FavoriteChargeStationCommand.class) )),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation")})
    public Mono<ServerResponse> addFavouriteChargeStation(ServerRequest req) {
        String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));
        //TODO: Check teh station id exists
        return req.bodyToMono(FavoriteChargeStationCommand.class)
                .flatMap(s -> {
                    return chargeStationService.addFavouriteChargeStation(userId, UUID.fromString(s.getChargeStationId())).flatMap(p -> ServerResponse.ok().build());

                });
    }

    @Operation(
            summary = "Remove Favourite Charge Station",
            description = "Remove a favourite charge station",
            requestBody = @RequestBody(content =@Content(schema = @Schema(implementation = FavoriteChargeStationCommand.class) )),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation")})
    public Mono<ServerResponse> removeFavouriteChargeStation(ServerRequest req) {
        String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));

        return req.bodyToMono(FavoriteChargeStationCommand.class)
                .flatMap(s -> {
                    return chargeStationService.removeFavouriteChargeStation(userId, UUID.fromString(s.getChargeStationId())).flatMap(p -> ServerResponse.ok().build());

                });
    }


    @Operation(
            summary = "Favourite Charging Stations",
            description = "Get favourite charging station for logged in user",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChargeStationItem.class))))})
    public Mono<ServerResponse> favouriteChargeStations(ServerRequest req) {
        String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));

        return req.bodyToMono(FavoriteChargeStationCommand.class)
                .flatMap(s -> {
                    Mono<List<FavoriteChargeStation>> favourites = chargeStationService.favouriteChargeStations(userId);
                    return favourites.flatMap(f -> {
                        List<ChargeStationItem> chargeItems = f.stream().map(i -> ChargeStationItem.builder()
                                .stationName(i.getChargeStationId().getName())
                                .stationId(i.getChargeStationId().getChargeStationId())
                                .siteName(i.getChargeStationId().getChargeSite().getSiteName())
                                .addressDetails(AddressDetails.builder()
                                        .longitude(i.getChargeStationId().getChargeSite().getAddress().getLongitude())
                                        .latitude(i.getChargeStationId().getChargeSite().getAddress().getLatitude())
                                        .country(i.getChargeStationId().getChargeSite().getAddress().getCountry())
                                        .locality(i.getChargeStationId().getChargeSite().getAddress().getCountry())
                                        .subAdministrativeArea(i.getChargeStationId().getChargeSite().getAddress().getSubAdministrativeArea())
                                        .administrativeArea(i.getChargeStationId().getChargeSite().getAddress().getAdministrativeArea())
                                        .postCode(i.getChargeStationId().getChargeSite().getAddress().getPostCode())
                                        .streetAddress(i.getChargeStationId().getChargeSite().getAddress().getStreetAddress())
                                        .build())
                                .siteId(i.getChargeStationId().getChargeSite().getSiteId()).build()
                        ).collect(Collectors.toList());
                        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(chargeItems));
                    });

                });
    }

}
