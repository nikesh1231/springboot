package com.evbackend.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import com.evbackend.mapping.UserMapping;
import com.evbackend.model.chargestation.ChargeSiteAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.evbackend.commands.CreateUserCommand;
import com.evbackend.commands.UpdateUserCommand;
import com.evbackend.configuration.SecurityConfiguration;
import com.evbackend.model.ApiResponse;
import com.evbackend.model.AuthenticationRequest;
import com.evbackend.model.AuthenticationResponse;
import com.evbackend.model.address.Address;
import com.evbackend.model.address.AddressDetails;
import com.evbackend.model.users.User;
import com.evbackend.model.users.UserDetails;
import com.evbackend.security.JWTUtil;
import com.evbackend.security.PBKDF2Encoder;
import com.evbackend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

	private final UserService userService;

	@Autowired
	private PBKDF2Encoder passwordEncoder;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private SecurityConfiguration securityConfiguration;


    // TODO: Move this example
    private static final String example = Map.of("key1","value1", "key2", "value2").toString();


    @Operation(
            summary = "All Users",
            description = "Retrieve all users",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDetails.class))))})
    public Mono<ServerResponse> getAllUsers(ServerRequest req) {
        return userService.getAllUsers().flatMap(users ->
                {
                    List<UserDetails> userDetails = users.stream().map(u -> {
                        return UserMapping.mapUserDetail(u);
                            }).collect(Collectors.toList());

                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(userDetails));
                }

                );
    }

    @Operation(
            summary = "All Active Users",
            description = "Retrieve all active users",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDetails.class))))})
    public Mono<ServerResponse> getAllActiveUsers(ServerRequest req) {
        return userService.getAllActiveUsers().flatMap(users ->
                {
                    List<UserDetails> userDetails = users.stream().map(u -> {
                        return UserMapping.mapUserDetail(u);
                    }).collect(Collectors.toList());

                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(userDetails));
                }

        );
    }

    @Operation(
            summary = "Get Current User Details",
            description = "Retrieve user details",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDetails.class))))})
    public Mono<ServerResponse> getUser(ServerRequest req) {

        String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));

        return userService.findUserByUserId(userId).flatMap(
                u -> {
                    if (u.getUserId() == null) {
                        return ServerResponse.badRequest().body(BodyInserters.fromObject(new ApiResponse(400, "Username does not exists", null)));
                    } else {
                        Address address = u.getAddress();
                        AddressDetails addressDetails = AddressDetails.builder()
                                .streetAddress(address.getStreetAddress())
                                .locality(address.getLocality())
                                .latitude(address.getLatitude())
                                .longitude(address.getLongitude())
                                .postCode(address.getPostCode())
                                .country(address.getCountry())
                                .administrativeArea(address.getAdministrativeArea())
                                .subAdministrativeArea(address.getSubAdministrativeArea())
                                .build();
                        UserDetails userDetails = UserDetails.builder()
                                .userId(u.getUserId())
                                .userName(u.getUserName())
                                .firstName(u.getFirstName())
                                .lastName(u.getLastName())
                                .phoneNumber(u.getPhoneNumber())
                                .address(addressDetails).build();
                        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(userDetails));
                    }
                }
        );
    }

	@Operation(summary = "Admin Create", description = "Allows admin to create any user", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = CreateUserCommand.class))), responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UUID.class, description = "Returns user identifier"))) })
	public Mono<ServerResponse> adminCreate(ServerRequest req) {
		return req.bodyToMono(CreateUserCommand.class).flatMap(user -> {
			return userService.findUserByUserName(user.getUserName()).flatMap(lookupUser -> {
				if (lookupUser.getUserId() != null) {
					return ServerResponse.badRequest()
							.body(BodyInserters.fromObject(new ApiResponse(400, "Username already exists", null)));
				} else {
					return userService.adminCreate(user)
							.flatMap(p -> ServerResponse.created(URI.create("/api/users/admincreate/" + p)).build());
				}

			});
		});
	}

    @Operation(
            summary = "Signup",
            description = "Allows pay as you go user to sign up",
            requestBody = @RequestBody(content =@Content(schema = @Schema(implementation = CreateUserCommand.class) )),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(implementation = UUID.class, description = "Returns user identifier")))})
    public Mono<ServerResponse> signup(ServerRequest req) {
        return req.bodyToMono(CreateUserCommand.class)
                .flatMap(user -> {
                    return userService.findUserByUserName(user.getUserName()).flatMap(lookupUser -> {
                       if (lookupUser.getUserId() != null) {
                           return ServerResponse.badRequest().body(BodyInserters.fromObject(new ApiResponse(400, "Username already exists", null)));
                       } else {
                           return userService.signup(user).flatMap(p -> ServerResponse.created(URI.create("/api/users/signup/" + p)).build());
                       }

                    });
                });
    }



    @Operation(
            summary = "Management Login",
            description = "Provide user name and password to login via the management app with basic authentication",
            security = @SecurityRequirement(name = "basicAuth"),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class, description = "Provides a JWT Token")))})
    public Mono<ServerResponse> loginManagementApp(ServerRequest request) {
        String authorizationBasic = request.headers().firstHeader("Authorization");
        String pair=new String(Base64.getDecoder().decode(authorizationBasic.substring(6)));
        String userName=pair.split(":")[0];
        String password=pair.split(":")[1];

        return userService.findUserByUserName(userName).flatMap(user -> {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return ServerResponse.ok().contentType(APPLICATION_JSON).body(BodyInserters.fromObject(new AuthenticationResponse(jwtUtil.generateToken(user))));
            } else {
                return ServerResponse.badRequest().body(BodyInserters.fromObject(new ApiResponse(400, "Invalid credentials", null)));
            }
        }).switchIfEmpty(ServerResponse.badRequest().body(BodyInserters.fromObject(new ApiResponse(400, "User does not exist", null))));
    }

    @Operation(
            summary = "Login",
            description = "Provide user name and password to login via basic authentication",
            security = @SecurityRequirement(name = "basicAuth"),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(implementation = AuthenticationResponse.class, description = "Provides a JWT Token")))})
    public Mono<ServerResponse> login(ServerRequest request) {
        String authorizationBasic = request.headers().firstHeader("Authorization");
        String pair=new String(Base64.getDecoder().decode(authorizationBasic.substring(6)));
        String userName=pair.split(":")[0];
        String password=pair.split(":")[1];

        return userService.findUserByUserName(userName).flatMap(user -> {
                if (passwordEncoder.matches(password, user.getPassword())) {
                	return ServerResponse.ok().contentType(APPLICATION_JSON).body(BodyInserters.fromObject(new AuthenticationResponse(jwtUtil.generateToken(user))));
                } else {
                    return ServerResponse.badRequest().body(BodyInserters.fromObject(new ApiResponse(400, "Invalid credentials", null)));
                }
            }).switchIfEmpty(ServerResponse.badRequest().body(BodyInserters.fromObject(new ApiResponse(400, "User does not exist", null))));
    }

    @Operation(
            summary = "Refresh Token",
            description = "Refresh token before expiry",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(implementation = String.class, description = "Provides a JWT Token")))})
	public Mono<ServerResponse> updateToken(ServerRequest request) {
		String authToken = request.headers().header(HttpHeaders.AUTHORIZATION).toString().substring(7, request.headers().header(HttpHeaders.AUTHORIZATION).toString().length() - 1);
		Mono<AuthenticationRequest> loginRequest = request.bodyToMono(AuthenticationRequest.class);
		return loginRequest
				.flatMap(login -> userService.findUserByUserNameForUpdateToken(login.getUserName())
				.flatMap(user -> {
					return ServerResponse.ok().contentType(APPLICATION_JSON)
							.body(BodyInserters.fromObject(new AuthenticationResponse(jwtUtil.generateRefreshToken(user))));
				}).switchIfEmpty(ServerResponse.badRequest()
						.body(BodyInserters.fromObject(new ApiResponse(400, "Unknown error occurred 1", null)))));
	}




	@Operation(summary = "Deactivate a user", description = "Inactivate a user based on userId", responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation") })
	public Mono<ServerResponse> inactivateUser(ServerRequest request) {
		String userId = request.pathVariable("id");

		return userService.inactivateUserByUserId(userId).flatMap(user -> {
			return ServerResponse.ok().contentType(APPLICATION_JSON).build();
		}).switchIfEmpty(ServerResponse.badRequest()
				.body(BodyInserters.fromObject(new ApiResponse(400, "Unknown error occurred 1", null))));
	}

    @Operation(summary = "Update User", description = "Update User By Id - fields are optional. If not specified then not updated.", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation =  UpdateUserCommand.class))), responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UserDetails.class, description = "Returns user identifier"))) })
    public Mono<ServerResponse> partialUpdateUserById(ServerRequest req) {
        String uuidString = req.pathVariable("id");
        if (uuidString.isEmpty()) {
            return ServerResponse.badRequest()
                    .body(BodyInserters.fromObject(new ApiResponse(400, "id cannot be null", null)));
        } else {
            return req.bodyToMono(Map.class).flatMap(u ->
                    {
                        Mono<UserDetails> userDetails = userService.partialUpdateUserById(UUID.fromString(uuidString), u);
                        return userDetails.flatMap(k -> {
                            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(k));
                        });


                    });
        }
    }

    @Operation(summary = "Full Update User", description = "Full Update User By Id", requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UpdateUserCommand.class))), responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = UserDetails.class, description = "Returns user identifier"))) })
    public Mono<ServerResponse> fullUpdateUserById(ServerRequest req) {
        String uuidString = req.pathVariable("id");
        if (uuidString.isEmpty()) {
            return ServerResponse.badRequest()
                    .body(BodyInserters.fromObject(new ApiResponse(400, "id cannot be null", null)));
        } else {
            return req.bodyToMono(UpdateUserCommand.class)
                    .flatMap(a -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(userService.fullUpdateUserById(UUID.fromString(uuidString), a))))
                    .switchIfEmpty(ServerResponse.notFound().build());
        }
    }

}