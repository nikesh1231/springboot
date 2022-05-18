package com.evbackend.model.vehicle;


import com.evbackend.enumerate.VehicleColor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleItem {

    @Schema(description = "Unique identifier for the registered vehicle")
    UUID vehicleId;

    @Schema(description = "Unique identifier for the vehicle version")
    UUID vehicleVersionId;

    @Schema(description = "Vehicle's registration number", example = "XXY092")
    String vehicleRegistration;

    @Schema(description = "Date user registered vehicle in system in UTC - epoch second")
    Long createdAt;

    @Schema(description = "Vehicle's identification number (VIN)")
    String vin;

    @Schema(description = "Vehicle's color")
    VehicleColor color;

}
