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
public class ModelItem {

    @Schema(description = "Unique identifier for vehicle model")
    UUID modelId;

    @Schema(description = "Model name")
    String modelName;

}
