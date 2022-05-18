package com.evbackend.commands;

import com.evbackend.enumerate.VehicleColor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class RegisterVehicleCommand {


    @Schema(description = "Vehicle Version to Register")
    UUID vehicleVersionId;

    @Schema(description = "Vehicle Registration number")
    String vehicleRegistration;

    @Schema(description = "VIN - Vehicle Identification number")
    String vin;

    @Schema(description = "Vehicle Color")
    VehicleColor color;

}
