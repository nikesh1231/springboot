package com.evbackend.model.vehicle;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VehicleVersionDetail {

    @Schema(description = "Vehicle Version ID")
    UUID vehicleVersionId;

    @Schema(description = "Vehicle Year", example = "2009")
    String year;

    @Schema(description = "Vehicle Version Name")
    String vehicleVersionName;

    @Schema(description = "Vehicle Model Name")
    String vehicleModelName;

    @Schema(description = "Vehicle Manufacturer Name", example = "Audi")
    String vehicleManufacturerName;

}
