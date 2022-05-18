package com.evbackend.mapping;

import com.evbackend.model.address.Address;
import com.evbackend.model.address.AddressDetails;

public class AddressMapping {

    public static AddressDetails mapAddressToDetails(Address address) {
        return AddressDetails.builder()
                .streetAddress(address.getStreetAddress())
                .subAdministrativeArea(address.getSubAdministrativeArea())
                .longitude(address.getLongitude())
                .latitude(address.getLatitude())
                .administrativeArea(address.getAdministrativeArea())
                .postCode(address.getLocality())
                .locality(address.getLocality())
                .build();
    }

}
