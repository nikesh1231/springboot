package com.evbackend.model.chargestation;

import java.util.UUID;

import org.springframework.lang.NonNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargeSiteItem {

    @NonNull
    @Schema(description = "Unique identifier for site")
    UUID siteId;

    @NonNull
    @Schema(description = "Site name", example ="Site X")
    String siteName;
}
