package com.evbackend.model.chargestation;

import com.evbackend.model.users.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
@Table(name = "review")
@Component
public class Review {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Schema(description = "Unique identifier for the review")
    UUID reviewId;

    @Schema(description = "Rating 1-5", example="2")
    @Column(nullable = false)
    Integer rating;

    @Schema(description = "Review Feedback", example = "Amazing experience 5/5")
    @Column(nullable = false)
    String feedback;

    @Builder.Default
    @Column(name = "createdAt")
    @CreationTimestamp
    LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "createdBy")
    User createdBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chargeStationId")
    ChargeStation chargeStationId;

}
