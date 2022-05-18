package com.evbackend.model.vehicle;

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
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
@Table(name = "vehicleversion")
@Component
public class VehicleVersion {


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "vehicleVersionId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Schema(description = "Vehicle Version ID")
    UUID vehicleVersionId;

    @Schema(description = "Vehicle Year", example = "2009")
    @Column(nullable = false)
    String year;

    @Schema(description = "Vehicle Version Name", example = "Audi")
    @Column(nullable = false)
    String name;

    @Builder.Default
    @Column(name = "createdAt")
    @CreationTimestamp
    LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicleModelId")
    VehicleModel vehicleModelId;

    // Removed Bi Directional constraint
    /*
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicleId")
    Set<Vehicle> vehicles;

     */
}
