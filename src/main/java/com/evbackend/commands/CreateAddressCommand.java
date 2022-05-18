package com.evbackend.commands;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateAddressCommand {

    @NonNull
    @Schema(description = "Street address", example ="123 Fake St")
    String streetAddress;

    @NonNull
    @Schema(description = "Post code or zip code", example ="95014")
    String postCode;

    @NonNull
    @Schema(description = "City or town", example ="Boston")
    String locality;

    @NonNull
    @Schema(description = "State or province", example ="California")
    String administrativeArea;

    @Nullable
    @Schema(description = "County", example ="Albany County")
    String subAdministrativeArea;

    @NonNull
    @Schema(description = "Country as ISO 3166 country code", example ="DE")
    String country;

    @NonNull
    @Schema(description = "Latitude", example ="38.8951")
    Double latitude;

    @NonNull
    @Schema(description = "Longitude", example ="-77.0364")
    Double longitude;
}
