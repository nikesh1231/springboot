package com.evbackend.model.address;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "addressId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Schema(description = "Unique identifier for the address")
    UUID addressId;

	/*
	 * @OneToOne(mappedBy = "address") User user;
	 */

    @Column(nullable = false)
    @Schema(description = "Street address", example ="123 Fake St")
    String streetAddress;

    @Column(nullable = false)
    @Schema(description = "Post code or zip code", example ="95014")
    String postCode;

    // https://stackoverflow.com/questions/13370221/persistentobjectexception-detached-entity-passed-to-persist-thrown-by-jpa-and-h
    @Schema(description = "City or town", example ="Boston")
    String locality;

    @Schema(description = "State or province", example ="California")
    String administrativeArea;

    @Column(nullable = true)
    @Schema(description = "County", example ="California")
    String subAdministrativeArea;

    @Schema(description = "Country as ISO 3166 country code", example ="DE")
    String country;

    @Column(nullable = false)
    @Schema(description = "Latitude", example ="38.8951")
    Double latitude;

    @Column(nullable = false)
    @Schema(description = "Longitude", example ="-77.0364")
    Double longitude;

}