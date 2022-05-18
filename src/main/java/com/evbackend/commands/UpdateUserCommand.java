package com.evbackend.commands;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UpdateUserCommand implements Serializable {

	@NonNull
	String userName;

	@NonNull
	String firstName;

	@NonNull
	String lastName;

	@NonNull
	String password;

	@NonNull
	String phoneNumber;

	@NonNull
	String email;

	@NonNull
	CreateAddressCommand address;

}
