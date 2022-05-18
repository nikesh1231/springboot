package com.evbackend.model.users;


import java.util.UUID;

import org.springframework.util.ObjectUtils;

import com.evbackend.model.address.AddressDetails;

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
public class UserDetails {

    @Schema(description = "Unique identifier fr the user")
    UUID userId;

    @Schema(description = "Unique identifier the user chooses to login", example="sally01")
    String userName;

    @Schema(description = "User's first name", example = "Sally")
    String firstName;

    @Schema(description = "User's surname", example = "Smith")
    String lastName;

    @Schema(description = "User phone number - not unique", example = "063 132 233")
    String phoneNumber;

    @Schema(description = "Unique email - not unique", example= "sally@example.com")
    String email;

    AddressDetails address;
    
    public UserDetails(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.lastName = user.getLastName();
        this.firstName = user.getFirstName();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();

        if (!ObjectUtils.isEmpty(user.getAddress().getAddressId())) {
            this.address.setAdministrativeArea(user.getAddress().getAdministrativeArea());
            this.address.setCountry(user.getAddress().getCountry());
            this.address.setLatitude(user.getAddress().getLatitude());
            this.address.setLocality(user.getAddress().getLocality());
            this.address.setLongitude(user.getAddress().getLongitude());
            this.address.setPostCode(user.getAddress().getPostCode());
            this.address.setStreetAddress(user.getAddress().getStreetAddress());
            this.address.setSubAdministrativeArea(user.getAddress().getSubAdministrativeArea());
        }
    }

}
