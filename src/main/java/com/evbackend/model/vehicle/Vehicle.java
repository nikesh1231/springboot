package com.evbackend.model.vehicle;

import com.evbackend.enumerate.VehicleColor;
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
@Table(name = "vehicle")
@Component
public class Vehicle {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "vehicleId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Schema(description = "Unique identifier for the vehicle")
    UUID vehicleId;

    @Schema(description = "Vehicle's registration number", example = "XXY092")
    @Column(nullable = false)
    String vehicleRegistration;

    @Schema(description = "Is the vehicle still active")
    @Column(nullable = false)
    boolean activeVehicle;

    @Schema(description = "Vehicle Color")
    VehicleColor color;

    @Schema(description = "Vehicle Identificaiton Number")
    String vin;

    @Builder.Default
    @Column(name = "createdAt")
    @CreationTimestamp
    LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    User userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicleVersionId")
    VehicleVersion vehicleVersionId;
}
