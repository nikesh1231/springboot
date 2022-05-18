package com.evbackend.model.vehicle;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
@Table(name = "vehiclemanufacturer")
@Component
public class VehicleManufacturer {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "vehicleManufacturerId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Schema(description = "Vehicle Manufacturer ID")
    UUID vehicleManufacturerId;


    @Schema(description = "Vehicle Manufacturer Name", example = "Audi")
    @Column(nullable = false)
    String name;

    @Schema(description = "Manufacturer Image url", example = "http://example.com/audi/png")
    @Column(nullable = false)
    String manufacturerImageUrl;

    // Removed Bi Directional constraint
/*
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicleModelId")
    Set<VehicleModel> vehicleModels;

 */
}
