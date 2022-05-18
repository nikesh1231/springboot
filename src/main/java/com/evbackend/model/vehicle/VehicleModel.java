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
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
@Table(name = "vehiclemodel")
@Component
public class VehicleModel {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "vehicleModelId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Schema(description = "Vehicle Manufacturer ID")
    UUID vehicleModelId;


    @Schema(description = "Vehicle Manufacturer Name", example = "Audi")
    @Column(nullable = false)
    String name;

    @Builder.Default
    @Column(name = "createdAt")
    @CreationTimestamp
    LocalDateTime createdAt = LocalDateTime.now();

    // Removed Bi Directional constraint
    /*
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicleVersionId")
    Set<VehicleVersion> vehicleVersions;

     */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicleManufacturerId")
    VehicleManufacturer vehicleManufacturerId;

}
