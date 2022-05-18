package com.evbackend.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class DeregisterVehicleCommand {

    @Schema(description = "Vehicle Id to deregister")
    UUID vehicleId;

}
