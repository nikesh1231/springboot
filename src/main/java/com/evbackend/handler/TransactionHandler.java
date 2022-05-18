package com.evbackend.handler;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.evbackend.model.ApiResponse;
import com.evbackend.model.AppConstants;
import com.evbackend.model.Transaction;
import com.evbackend.model.dto.TransactionResponseDTO;
import com.evbackend.model.dto.TransactionTimeRequest;
import com.evbackend.security.JWTUtil;
import com.evbackend.service.TransactionService;
import com.evbackend.util.DateUtil;

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
public class TransactionHandler {

	@Autowired
	private JWTUtil jwtUtil;

	private final TransactionService transactionService;

	@Operation(summary = "All Transactions", description = "Retrieve all transactions", parameters = {
			@Parameter(name = "accountId", in = ParameterIn.QUERY, description = "Optional accountId filter", required = false, content = @Content(schema = @Schema(implementation = String.class))),
			@Parameter(name = "chargeStationId", in = ParameterIn.QUERY, description = "Optional chargeStationID filter", required = false, content = @Content(schema = @Schema(implementation = String.class))),
			@Parameter(name = "chargeSiteID", in = ParameterIn.QUERY, description = "Optional chargeSiteID filter", required = false, content = @Content(schema = @Schema(implementation = String.class))) }, responses = {
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionResponseDTO.class)))) })
	public Mono<ServerResponse> getAllTransactions(ServerRequest req) {
		UUID accountId = null;
		UUID chargeStation = null;
		UUID chargeSite = null;
		try {
			accountId = !ObjectUtils.isEmpty(req.queryParam(AppConstants.ACCOUNT_ID))
					? getUuid(req.queryParam(AppConstants.ACCOUNT_ID))
					: null;
			chargeStation = !ObjectUtils.isEmpty(req.queryParam(AppConstants.CHARGE_STATION_ID))
					? getUuid(req.queryParam(AppConstants.CHARGE_STATION_ID))
					: null;
			chargeSite = !ObjectUtils.isEmpty(req.queryParam(AppConstants.CHARGE_SITE_ID))
					? getUuid(req.queryParam(AppConstants.CHARGE_SITE_ID))
					: null;
		} catch (Exception e) {
			return ServerResponse.badRequest()
					.body(BodyInserters.fromValue(new ApiResponse(400, "Invalid UUID", null)));
		}
		return ServerResponse.ok().body(transactionService.getAllTransactions(accountId, chargeStation, chargeSite),
				Transaction.class);
	}

	@Operation(summary = "All Transactions For User", description = "Retrieve all transactions", parameters = {
			@Parameter(name = "accountId", in = ParameterIn.QUERY, description = "Optional accountId filter", required = false, content = @Content(schema = @Schema(implementation = String.class))),
			@Parameter(name = "chargeStationId", in = ParameterIn.QUERY, description = "Optional chargeStationID filter", required = false, content = @Content(schema = @Schema(implementation = String.class))),
			@Parameter(name = "chargeSiteID", in = ParameterIn.QUERY, description = "Optional chargeSiteID filter", required = false, content = @Content(schema = @Schema(implementation = String.class))) }, responses = {
					@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionResponseDTO.class)))) })
	public Mono<ServerResponse> getAllTransactionsForUser(ServerRequest req) {
		String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
		UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));
		UUID accountId = null;
		UUID chargeStation = null;
		UUID chargeSite = null;
		try {
			accountId = !ObjectUtils.isEmpty(req.queryParam(AppConstants.ACCOUNT_ID))
					? getUuid(req.queryParam(AppConstants.ACCOUNT_ID))
					: null;
			chargeStation = !ObjectUtils.isEmpty(req.queryParam(AppConstants.CHARGE_STATION_ID))
					? getUuid(req.queryParam(AppConstants.CHARGE_STATION_ID))
					: null;
			chargeSite = !ObjectUtils.isEmpty(req.queryParam(AppConstants.CHARGE_SITE_ID))
					? getUuid(req.queryParam(AppConstants.CHARGE_SITE_ID))
					: null;
		} catch (Exception e) {
			return ServerResponse.badRequest()
					.body(BodyInserters.fromValue(new ApiResponse(400, "Invalid UUID", null)));
		}
		return ServerResponse.ok().body(
				transactionService.getAllTransactions(userId, accountId, chargeStation, chargeSite), Transaction.class);
	}

	@Operation(summary = "Transactions between time period", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = TransactionTimeRequest.class))), description = "Retrieve the transactions between given time period", responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionResponseDTO.class)))) })
	public Mono<ServerResponse> getTransactionsByTimePeriod(ServerRequest req) {
		Mono<TransactionTimeRequest> request = req.bodyToMono(TransactionTimeRequest.class);
		return request
				.flatMap(a -> ServerResponse.ok()
						.body(transactionService.getTransactionsByTimePeriod(DateUtil.getDate((a.getStartDate())),
								DateUtil.getDate(a.getEndDate())), Transaction.class))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	@Operation(summary = "Transactions between time period for user", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = TransactionTimeRequest.class))), description = "Retrieve the transactions between given time period", responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionResponseDTO.class)))) })
	public Mono<ServerResponse> getTransactionsByTimePeriodForUser(ServerRequest req) {
		Mono<TransactionTimeRequest> request = req.bodyToMono(TransactionTimeRequest.class);
		String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
		UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));
		return request.flatMap(a -> ServerResponse.ok()
				.body(transactionService.getTransactionsByTimePeriod(userId, DateUtil.getDate((a.getStartDate())),
						DateUtil.getDate(a.getEndDate())), Transaction.class))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	@Operation(summary = "All Transactions By Vehicle Id", description = "Get Transactions by Vehicle", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))), responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation") })
	public Mono<ServerResponse> getTransactionsByVehicleId(ServerRequest req) {
		String vehicleId = req.pathVariable("id");
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(transactionService.getTransactionsByVehicleId(vehicleId), TransactionResponseDTO.class)
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	@Operation(summary = "Transactions between time period by vehicle id", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = TransactionTimeRequest.class))), description = "Retrieve the transactions between given time period and by Vehicle Id", responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionResponseDTO.class)))) })
	public Mono<ServerResponse> getTransactionsByVehicleIdAndTimePeriod(ServerRequest req) {
		String vehicleId = req.pathVariable("id");
		Mono<TransactionTimeRequest> request = req.bodyToMono(TransactionTimeRequest.class);
		return request.flatMap(a -> ServerResponse.ok()
				.body(transactionService.getTransactionsByVehicleIdAndTimePeriod(vehicleId,
						DateUtil.getDate((a.getStartDate())), DateUtil.getDate(a.getEndDate())), Transaction.class))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	private UUID getUuid(Optional<String> request) {
		if (request.isPresent()) {
			return UUID.fromString(request.get());
		}
		return null;
	}

}
