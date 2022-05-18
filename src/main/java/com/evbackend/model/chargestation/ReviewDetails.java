package com.evbackend.model.chargestation;

import com.evbackend.model.users.UserItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDetails {

    UUID reviewId;

    @Schema(description = "Rating 1-5", example="2")
    Integer rating;

    @Schema(description = "Review Feedback", example = "Amazing experience 5/5")
    String feedback;

    @Schema(description = "Time created epoch seconds. UTC")
    Long createdAt;

    UserItem userItem;

}
