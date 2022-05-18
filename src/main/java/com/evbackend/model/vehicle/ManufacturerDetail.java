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
public class ManufacturerDetail {

    @Schema(description = "Unique identifier for vehicle manufacturer")
    UUID manufacturerId;

    @Schema(description = "Manufacturer name")
    String manufacturerName;




}
