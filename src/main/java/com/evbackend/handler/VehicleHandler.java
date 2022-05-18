package com.evbackend.handler;

import static java.time.ZoneOffset.UTC;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.evbackend.commands.DeregisterVehicleCommand;
import com.evbackend.commands.RegisterVehicleCommand;
import com.evbackend.model.ApiResponse;
import com.evbackend.model.vehicle.ManufacturerDetail;
import com.evbackend.model.vehicle.ManufacturerItem;
import com.evbackend.model.vehicle.ModelDetail;
import com.evbackend.model.vehicle.ModelItem;
import com.evbackend.model.vehicle.VehicleItem;
import com.evbackend.model.vehicle.VehicleManufacturer;
import com.evbackend.model.vehicle.VehicleModel;
import com.evbackend.model.vehicle.VehicleVersionDetail;
import com.evbackend.model.vehicle.VehicleVersionItem;
import com.evbackend.security.JWTUtil;
import com.evbackend.service.VehicleService;

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
public class VehicleHandler {

    private final VehicleService vehicleService;

    @Autowired
    private JWTUtil jwtUtil;

    //TODO: Add all vehicles for users

    @Operation(
            summary = "All Active Vehicles For User",
            description = "Retrieve all active vehicles for logged in user - not deregistered",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = VehicleItem.class))))})
    public Mono<ServerResponse> getActiveVehiclesForUser(ServerRequest req) {
        String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));
        return vehicleService.getActiveVehiclesForUser(userId)
                .flatMap(vehicles ->  {
                    List<VehicleItem> vehicleItems = vehicles.stream().map(
                        v -> VehicleItem.builder()
                                .vehicleId(v.getVehicleId())
                                .vehicleRegistration(v.getVehicleRegistration())
                                .vin(v.getVin())
                                .color(v.getColor())
                                .vehicleVersionId(v.getVehicleVersionId().getVehicleVersionId())
                                .createdAt(v.getCreatedAt().toEpochSecond(UTC)).build()
                        ).collect(Collectors.toList());
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(vehicleItems));
                });
    }


    @Operation(
            summary = "All Manufacturers",
            description = "Retrieve all manufacturers",
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ManufacturerItem.class))))})
    public Mono<ServerResponse> getAllManufacturers(ServerRequest req) {
        return vehicleService.getVehicleManufacturers()
                .flatMap(manufacturers ->{
                    List<ManufacturerItem> manufacturerItems = manufacturers.stream().map(
                    manufacturer -> ManufacturerItem.builder()
                            .manufacturerId(manufacturer.getVehicleManufacturerId())
                            .urlManufacturerLogo(manufacturer.getManufacturerImageUrl())
                            .manufacturerName(manufacturer.getName()).build()
                    ).collect(Collectors.toList());

                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(manufacturerItems));
                    }
                );
    }

    @Operation(
            summary = "Find Manufacturer",
            description = "Retrieve manufacturer",
            parameters = @Parameter(name = "id", description = "Vehicle Manufacturer ID", required = true,  in = ParameterIn.PATH, schema =  @Schema(implementation = UUID.class)),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ManufacturerDetail.class))))})
    public Mono<ServerResponse> getManufacturer(ServerRequest req) {
        String uuidString = req.pathVariable("id");

        return vehicleService.getVehicleManufacturer(UUID.fromString(uuidString))
                .flatMap(v -> {
                            if (v.getVehicleManufacturerId() == null) {
                                return ServerResponse.badRequest().body(BodyInserters.fromObject(new ApiResponse(400, "Manufacturer id does not exists", null)));
                            } else {
                                ManufacturerDetail manufacturerDetail = ManufacturerDetail.builder()
                                        .manufacturerId(v.getVehicleManufacturerId())
                                        .manufacturerName(v.getName()).build();
                                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(manufacturerDetail));
                            }
                        }

                );
    }

    @Operation(
            summary = "All models for Manufacturer",
            description = "Retrieve all models for manufacturer",
            parameters = @Parameter(name = "id", description = "Vehicle Manufacturer ID",required = true,  in = ParameterIn.PATH, schema =  @Schema(implementation = UUID.class)),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ModelItem.class))))})
    public Mono<ServerResponse> getModelsForManufacturer(ServerRequest req) {

        String uuidString = req.pathVariable("id");

        Mono<VehicleManufacturer> vehicleManufacturer = vehicleService.getVehicleManufacturer(UUID.fromString(uuidString));


        return vehicleManufacturer.flatMap(vm -> {
           if (vm.getVehicleManufacturerId() == null) {
               return ServerResponse.badRequest().body(BodyInserters.fromObject(new ApiResponse(400, "Manufacturer id does not exists", null)));
           } else {
               return vehicleService.getModelsForManufacturer(UUID.fromString(uuidString))
                       .flatMap(m -> {
                           List<ModelItem> modelItems = m.stream().map(
                                   model -> ModelItem.builder()
                                           .modelId(model.getVehicleModelId())
                                           .modelName(model.getName()).build()
                           ).collect(Collectors.toList());
                                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(modelItems));
                               }
                       );
           }
        });

    }

    @Operation(
            summary = "Find Model",
            description = "Retrieve model",
            parameters = @Parameter(name ="id", description = "Vehicle model", required = true,  in = ParameterIn.PATH, schema =  @Schema(implementation = UUID.class)),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(implementation = ModelDetail.class)))})
    public Mono<ServerResponse> getModel(ServerRequest req) {
        String uuidString = req.pathVariable("id");
        return vehicleService.getVehicleModel(UUID.fromString(uuidString))
                .flatMap(v -> {
                            if (v.getVehicleModelId() == null) {
                                return ServerResponse.badRequest().body(BodyInserters.fromObject(new ApiResponse(400, "Model id does not exists", null)));
                            } else {
                                ModelDetail modelDetail =ModelDetail.builder()
                                        .modelId(v.getVehicleModelId())
                                        .modelName(v.getName()).build();
                                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(modelDetail));
                            }
                        }
                );
    }

    @Operation(
            summary = "Years for model",
            description = "Find years for given model",
            parameters = @Parameter(name ="id", description = "Vehicle model", required = true, schema =  @Schema(implementation = UUID.class)),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class))))})
    public Mono<ServerResponse> getYearsForModel(ServerRequest req) {
        String uuidString = req.pathVariable("id");
        Mono<VehicleModel> vehicleModel = vehicleService.getVehicleModel(UUID.fromString(uuidString));
        // TODO: Check vehicle model exists

        return vehicleModel.flatMap(v -> {
            if (v.getVehicleModelId() == null) {
                return ServerResponse.badRequest().body(BodyInserters.fromObject(new ApiResponse(400, "Manufacturer id does not exists", null)));
            } else {
                return vehicleService.getYearsForVehicleModel(UUID.fromString(uuidString))
                        .flatMap(m -> {
                                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(m));
                                }
                        );
            }
        });

    }

    @Operation(
            summary = "Find Vehicle Versions",
            description = "Find Vehicle Version for Given Model and Year",
            parameters = @Parameter(name = "model_id", description = "Vehicle model", required = true,  in = ParameterIn.PATH, schema =  @Schema(implementation = UUID.class)),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = VehicleVersionItem.class))))})
    public Mono<ServerResponse> getVersionForModelAndYear(ServerRequest req) {
        String uuidString = req.pathVariable("model_id");
        String yearString = req.pathVariable("year");


        return vehicleService.getVehicleVersionsForModelAndYear(yearString, UUID.fromString(uuidString))
                .flatMap(v -> {
                    List<VehicleVersionItem> vehicleVersions = v.stream().map(
                            vv -> VehicleVersionItem.builder()
                                    .vehicleVersionId(vv.getVehicleVersionId())
                                    .vehicleVersionName(vv.getName()).build()
                    ).collect(Collectors.toList());
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(vehicleVersions));
                });

    }

    @Operation(
            summary = "Get vehicle version",
            description = "Retrieve vehicle version",
            parameters = @Parameter(name = "id", description = "Vehicle version", required = true, in = ParameterIn.PATH, schema =  @Schema(implementation = UUID.class)),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = @Content(schema = @Schema(implementation = VehicleVersionDetail.class)))})
    public Mono<ServerResponse> getVersion(ServerRequest req) {
        String uuidString = req.pathVariable("id");

        return vehicleService.getVehicleVersion(UUID.fromString(uuidString))
                .flatMap(v -> {
                            if (v.getVehicleVersionId() == null) {
                                return ServerResponse.badRequest().body(BodyInserters.fromObject(new ApiResponse(400, "Model id does not exists", null)));
                            } else {
                                VehicleVersionDetail vehicleVersionDetail = VehicleVersionDetail.builder()
                                        .vehicleVersionId(v.getVehicleVersionId())
                                        .vehicleVersionName(v.getName())
                                        .vehicleManufacturerName(v.getVehicleModelId().getVehicleManufacturerId().getName())
                                        .vehicleModelName(v.getVehicleModelId().getName())
                                        .year(v.getYear()).build();
                                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(vehicleVersionDetail));
                            }
                        }
                );
    }

    @Operation(
            summary = "Register Vehicle",
            description = "Register vehicle for user",
            requestBody = @RequestBody(content =@Content(schema = @Schema(implementation = RegisterVehicleCommand.class) )),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation")})
    public Mono<ServerResponse> register(ServerRequest req) {
        String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));

        //TODO: Check if vehicle version exists

        return req.bodyToMono(RegisterVehicleCommand.class)
                .flatMap(r -> vehicleService.registerVehicle(userId, r))
                .flatMap(p -> ServerResponse.created(URI.create("/api/vehicle/register" + p)).build());
    }

    @Operation(
            summary = "Deregister Vehicle",
            description = "Deregister vehicle for user",
            requestBody = @RequestBody(content =@Content(schema = @Schema(implementation = DeregisterVehicleCommand.class) )),
            responses = { @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "successful operation")})
    public Mono<ServerResponse> deregister(ServerRequest req) {
        String token = req.headers().firstHeader(HttpHeaders.AUTHORIZATION).substring(7);
        UUID userId = UUID.fromString(jwtUtil.getUsernameFromToken(token));

        //TODO: Check if vehicle exists

        //TODO: Validate vehicle is registered with this user

        return req.bodyToMono(DeregisterVehicleCommand.class)
                .flatMap(r -> vehicleService.deregisterVehicle(r))
                .flatMap(p -> ServerResponse.created(URI.create("/api/vehicle/deregister" + p)).build());
    }
    

}
