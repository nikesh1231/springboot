package com.evbackend.model.users;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import com.evbackend.model.address.Address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
@Table(name = "user")
@Component
public class User {


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Schema(description = "Unique identifier for the user - internal")
    UUID userId;

    @Schema(description = "Unique identifier the user chooses to login", example="sally01")
    @Column(nullable = false)
    String userName;

    @Schema(description = "User's first name", example = "Sally")
    @Column(nullable = false)
    String firstName;

    @Schema(description = "User's surname", example = "Smith")
    @Column(nullable = false)
    String lastName;

    @Schema(description = "Password for user - currently no verificaiton", example = "password0101!")
    @Column(nullable = false)
    String password;

    @Schema(description = "Is the user still active")
    @Column(nullable = false)
    boolean activeUser;

    @Schema(description = "User phone number - not unique", example = "063 132 233")
    @Column(nullable = false)
    String phoneNumber;

    @Schema(description = "Unique email - not unique", example= "sally@example.com")
    @Column(nullable = false)
    String email;

    @Builder.Default
    @Column(name = "createdAt")
    @CreationTimestamp
    LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updatedAt")
    @CreationTimestamp
    LocalDateTime updatedAt = LocalDateTime.now();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "addressId", referencedColumnName = "addressId")
//    @JsonManagedReference
    Address address;

    // Removed Bi Directional constraint
    /*
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "createdBy")
    Set<ChargeStation> chargeStations;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "createdBy")
    Set<ChargeSite> chargeSite;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "createdBy")
    Set<Review> reviews;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    Set<Vehicle> vehicles;

     */

}
