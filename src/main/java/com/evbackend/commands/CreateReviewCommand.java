package com.evbackend.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateReviewCommand {

    @Schema(description = "Rating 1-5", example="2")
    Integer rating;

    @Schema(description = "Review Feedback", example = "Amazing experience 5/5")
    String feedback;

    @Schema(description = "Charge Station ID")
    UUID chargeStationId;
}
