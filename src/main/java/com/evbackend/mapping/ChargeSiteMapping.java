package com.evbackend.mapping;

import com.evbackend.model.chargestation.ChargeSite;
import com.evbackend.model.chargestation.ChargeSiteAll;

import static java.time.ZoneOffset.UTC;

public class ChargeSiteMapping {

    public static ChargeSiteAll mapChargeSiteAll(ChargeSite chargeSite) {
        return ChargeSiteAll.builder()
                .userItem(UserMapping.mapUser(chargeSite.getCreatedBy()))
                .addressDetails(AddressMapping.mapAddressToDetails(chargeSite.getAddress()))
                .siteId(chargeSite.getSiteId())
                .siteName(chargeSite.getSiteName())
                .createdAt(chargeSite.getCreatedAt().toEpochSecond(UTC))
                .activeSite(chargeSite.getActiveSite())
                .build();
    }

}
