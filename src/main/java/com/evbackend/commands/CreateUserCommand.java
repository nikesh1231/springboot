package com.evbackend.commands;

import java.io.Serializable;

import org.springframework.lang.NonNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateUserCommand implements Serializable {


    @NonNull
    @Schema(description = "Unique identifier the user chooses to login", example="sally01")
    String userName;

	@NonNull
    @Schema(description = "User's first name", example = "Sally")
    String firstName;

    @NonNull
    @Schema(description = "User's surname", example = "Smith")
    String lastName;

    @NonNull
    @Schema(description = "Password for user - currently no verification", example = "password0101!")
    String password;


    @NonNull
    @Schema(description = "User phone number - not unique", example = "063 132 233")
    String phoneNumber;

    @NonNull
    @Schema(description = "Unique email - not unique", example= "sally@example.com")
    String email;

    @NonNull
    CreateAddressCommand address;

}
