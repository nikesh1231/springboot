package com.evbackend.model.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDetails {

    @Schema(description = "Street address", example ="123 Fake St")
    String streetAddress;

    @Schema(description = "Post code or zip code", example ="95014")
    String postCode;

    @Schema(description = "City or town", example ="Boston")
    String locality;

    @Schema(description = "State or province", example ="California")
    String administrativeArea;

    @Schema(description = "County", example ="California")
    String subAdministrativeArea;

    @Schema(description = "Country as ISO 3166 country code", example ="DE")
    String country;

    @NonNull
    @Schema(description = "Latitude", example ="38.8951")
    Double latitude;

    @NonNull
    @Schema(description = "Longitude", example ="-77.0364")
    Double longitude;


}
