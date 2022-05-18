package com.evbackend.mapping;

import com.evbackend.model.address.Address;
import com.evbackend.model.address.AddressDetails;
import com.evbackend.model.users.User;
import com.evbackend.model.users.UserDetails;
import com.evbackend.model.users.UserItem;

public class UserMapping {

    public static UserItem mapUser(User user) {
        return UserItem.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .build();
    }

    public static UserDetails mapUserDetail(User u) {
        Address address = u.getAddress();
        AddressDetails addressDetails = AddressDetails.builder()
                .streetAddress(address.getStreetAddress())
                .locality(address.getLocality())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())
                .postCode(address.getPostCode())
                .country(address.getCountry())
                .administrativeArea(address.getAdministrativeArea())
                .subAdministrativeArea(address.getSubAdministrativeArea())
                .build();
        UserDetails userDetails = UserDetails.builder()
                .userId(u.getUserId())
                .userName(u.getUserName())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .phoneNumber(u.getPhoneNumber())
                .address(addressDetails).build();
        return userDetails;
    }

}
