package com.evbackend.service;


import com.evbackend.commands.DeregisterVehicleCommand;
import com.evbackend.commands.RegisterVehicleCommand;
import com.evbackend.model.users.User;
import com.evbackend.model.vehicle.Vehicle;
import com.evbackend.model.vehicle.VehicleManufacturer;
import com.evbackend.model.vehicle.VehicleModel;
import com.evbackend.model.vehicle.VehicleVersion;
import com.evbackend.repository.UserRepository;
import com.evbackend.repository.VehicleRepository;
import com.evbackend.security.PBKDF2Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;


    @Autowired
    PBKDF2Encoder passwordEncoder;

    public Mono<List<Vehicle>> getVehiclesForUser(UUID userId) {
        return this.vehicleRepository.getVehiclesForUser(userId).convert().with(toMono());
    }

    public Mono<List<Vehicle>> getActiveVehiclesForUser(UUID userId) {
        return this.vehicleRepository.getActiveVehiclesForUser(userId).convert().with(toMono());
    }

    public Mono<List<VehicleManufacturer>> getVehicleManufacturers() {
        return this.vehicleRepository.getVehicleManufacturers().convert().with(toMono());
    }

    public Mono<VehicleManufacturer> getVehicleManufacturer(UUID uuid) {
        return this.vehicleRepository.getVehicleManufacturer(uuid).convert().with(toMono());

    }

    public Mono<List<VehicleModel>> getModelsForManufacturer(UUID manufacturerUUID) {
        return this.vehicleRepository.getModelsForManufacturer(manufacturerUUID).convert().with(toMono());
    }

    public Mono<VehicleModel> getVehicleModel(UUID modelUUID) {
        return this.vehicleRepository.getVehicleModel(modelUUID).convert().with(toMono());
    }

    public Mono<String> registerVehicle(UUID userId, RegisterVehicleCommand registerVehicleCommand) {
        Mono<User> user = userRepository.getUserByUserId(userId).convert().with(toMono());
        Mono<VehicleVersion> vehicleVersion = vehicleRepository.getVehicleVersion(registerVehicleCommand.getVehicleVersionId()).convert().with(toMono());
        Mono<Vehicle> vehicle = Mono.zip(user, vehicleVersion).flatMap(u -> {
                    return vehicleRepository.registerVehicle(
                            Vehicle.builder()
                                    .activeVehicle(true)
                                    .vehicleRegistration(registerVehicleCommand.getVehicleRegistration())
                                    .vehicleVersionId(u.getT2())
                                    .vin(registerVehicleCommand.getVin())
                                    .color(registerVehicleCommand.getColor())
                                    .userId(u.getT1()).build()

                    ).convert().with(toMono());

                }
        );
        return vehicle.map(v -> v.getVehicleId().toString());
    }

    public Mono<String> deregisterVehicle(DeregisterVehicleCommand deregisterVehicleCommand) {
        Mono<Vehicle> vehicle = vehicleRepository.getVehicle(deregisterVehicleCommand.getVehicleId()).convert().with(toMono());

        vehicle.flatMap(u -> {
            return vehicleRepository.deregisterVehicle(Vehicle.builder()
                            .activeVehicle(false)
                            .createdAt(u.getCreatedAt())
                            .vehicleRegistration(u.getVehicleRegistration())
                            .vehicleVersionId(u.getVehicleVersionId())
                            .userId(u.getUserId())
                            .vehicleId(u.getVehicleId())
                    .vehicleId(u.getVehicleId()
                            ).build()

                    ).convert().with(toMono());
        });
        return vehicle.map(v -> v.getVehicleId().toString());
    }

    public Mono<VehicleVersion> getVehicleVersion(UUID versionUUID) {
        return this.vehicleRepository.getVehicleVersion(versionUUID).convert().with(toMono());
    }

    public Mono<List<VehicleVersion>> getVehicleVersionsForModelAndYear(String year,
                                                                        UUID modelID) {
        return this.vehicleRepository.getVehicleVersionsForModelAndYear(year, modelID).convert().with(toMono());
    }

    public Mono<List<String>> getYearsForVehicleModel(UUID modelID) {
        return this.vehicleRepository.getYearsForModel(modelID).convert().with(toMono());
    }

}
