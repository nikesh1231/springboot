package com.evbackend.handler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class HealthHandler {

    @Operation(
            summary = "Health",
            description = "Used to check the health of the service",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = { @ApiResponse(responseCode = "200",
                    description = "successful operation")})
    public Mono<ServerResponse> getHealth(ServerRequest req) {
        return ServerResponse.ok().build();
    }

}
