package com.evbackend.handler;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.evbackend.commands.CreateAccountCommand;
import com.evbackend.model.users.Account;
import com.evbackend.model.users.User;
import com.evbackend.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AccountHandler {

    private final AccountService accountService;

    @Operation(
            summary = "All Accounts",
            description = "Retrieve all accounts",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Account.class))))})
    public Mono<ServerResponse> getAllAccounts(ServerRequest req) {
        return ServerResponse.ok().body(accountService.getAllAccounts(), User.class);
    }


    @Operation(
            summary = "Create Account",
            description = "Creates account to manage user CSO, technical etc",
            requestBody = @RequestBody(content =@Content(schema = @Schema(implementation = CreateAccountCommand.class) )),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(implementation = Account.class, description = "Returns account")))})
    public Mono<ServerResponse> create(ServerRequest req) {
        return req.bodyToMono(CreateAccountCommand.class)
                .flatMap(p -> ServerResponse.ok().body(accountService.create(p), Account.class));
    }

}
