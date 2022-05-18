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
public class VehicleVersionItem {

    @Schema(description = "Vehicle Version ID")
    UUID vehicleVersionId;

    @Schema(description = "Vehicle Version Name")
    String vehicleVersionName;

}
