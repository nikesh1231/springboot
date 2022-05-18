package com.evbackend.model.chargestation;

import com.evbackend.model.address.AddressDetails;
import com.evbackend.model.users.UserItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargeSiteAll {

    @NonNull
    @Schema(description = "Unique identifier for site")
    UUID siteId;

    @NonNull
    @Schema(description = "Site name")
    String siteName;

    @NonNull
    @Schema(description = "Is teh site still active")
    Boolean activeSite;

    @NonNull
    @Schema(description = "Time site created in epoch millisecond UTC")
    Long createdAt;

    UserItem userItem;

    AddressDetails addressDetails;

}
