package com.evbackend.handler;

import com.evbackend.model.users.UserRole;
import com.evbackend.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserRoleHandler {

    private final UserRoleService userRoleService;

    @Operation(
            summary = "All User Roles",
            description = "Find all User Roles",
            security = @SecurityRequirement(name = "bearerAuth"),
            responses = { @ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserRole.class))))})
    public Mono<ServerResponse> getAllRoles(ServerRequest req) {
        return ServerResponse.ok().body(userRoleService.getAllUserRoles(), UserRole.class);
    }


}
