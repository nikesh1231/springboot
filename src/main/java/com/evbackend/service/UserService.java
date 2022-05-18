package com.evbackend.service;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evbackend.commands.CreateAddressCommand;
import com.evbackend.commands.CreateUserCommand;
import com.evbackend.commands.UpdateUserCommand;
import com.evbackend.model.address.Address;
import com.evbackend.model.address.AddressDetails;
import com.evbackend.model.users.User;
import com.evbackend.model.users.UserDetails;
import com.evbackend.model.address.Address;
import com.evbackend.model.users.User;
import com.evbackend.repository.UserRepository;
import com.evbackend.security.PBKDF2Encoder;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	PBKDF2Encoder passwordEncoder;

	@Autowired
	ObjectMapper objectMapper;

	public Mono<List<User>> getAllUsers() {
		return this.userRepository.getAllUsers().convert().with(toMono());
	}

	public Mono<List<User>> getAllActiveUsers() {
		return this.userRepository.getAllActiveUsers().convert().with(toMono());
	}

	public Mono<String> adminCreate(CreateUserCommand user) {

		CreateAddressCommand addressCommand = user.getAddress();

		Address address = Address.builder().streetAddress(addressCommand.getStreetAddress())
				.postCode(addressCommand.getPostCode()).longitude(addressCommand.getLongitude())
				.latitude(addressCommand.getLatitude()).administrativeArea(addressCommand.getAdministrativeArea())
				.subAdministrativeArea(addressCommand.getSubAdministrativeArea()).country(addressCommand.getCountry())
				.locality(addressCommand.getLocality()).build();

		String encodedPassword = this.passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		Mono<User> createdUser = this.userRepository.adminCreate(User.builder().userName(user.getUserName())
				.firstName(user.getFirstName()).lastName(user.getLastName()).password(user.getPassword())
				.phoneNumber(user.getPhoneNumber()).email(user.getEmail()).address(address).build()).convert()
				.with(toMono());
		return createdUser.map(u -> u.getUserId().toString());

	}

	public Mono<String> signup(CreateUserCommand user) {

		CreateAddressCommand addressCommand = user.getAddress();

		Address address = Address.builder().streetAddress(addressCommand.getStreetAddress())
				.postCode(addressCommand.getPostCode()).longitude(addressCommand.getLongitude())
				.latitude(addressCommand.getLatitude()).administrativeArea(addressCommand.getAdministrativeArea())
				.subAdministrativeArea(addressCommand.getSubAdministrativeArea()).country(addressCommand.getCountry())
				.locality(addressCommand.getLocality()).build();

		String encodedPassword = this.passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		Mono<User> createdUser = this.userRepository.adminCreate(User.builder().userName(user.getUserName())
				.firstName(user.getFirstName()).lastName(user.getLastName()).password(user.getPassword())
				.phoneNumber(user.getPhoneNumber()).email(user.getEmail()).address(address).build()).convert()
				.with(toMono());
		return createdUser.map(u -> u.getUserId().toString());
	}

	public Mono<User> findUserByUserName(String userName) {
		return this.userRepository.getUserByUserName(userName).convert().with(toMono());
	}

	public Mono<User> findUserByUserId(UUID userId) {
		return this.userRepository.getUserByUserId(userId).convert().with(toMono());
	}

	public Mono<User> findUserByUserNameForUpdateToken(String username) {
		return this.userRepository.getUserByUserName(username).convert().with(toMono());
	}

	public Mono<UserDetails> partialUpdateUserById(UUID id,  Map<String, Object> updates) {


		return this.findUserByUserId(id).flatMap(a -> {
			User.UserBuilder u = User.builder().userName(a.getUserName())
					.firstName(a.getFirstName())
					.userId(a.getUserId())
					.lastName(a.getLastName())
					.email(a.getEmail())
					.phoneNumber(a.getPhoneNumber())
					.password(a.getPassword())
					.activeUser(a.isActiveUser())
					.createdAt(a.getCreatedAt())
					.userName(a.getUserName())
					.address(a.getAddress());

			for (Map.Entry<String, Object> update : updates.entrySet()) {
				if (update.getKey().equals("userName")) {
					u.userName((String) update.getValue());
				}
				if (update.getKey().equals("firstName")) {
					u.firstName((String) update.getValue());
				}
				if (update.getKey().equals("lastName")) {
					u.lastName((String) update.getValue());
				}
				if (update.getKey().equals("phoneNumber")) {
					u.phoneNumber((String) update.getValue());
				}
				if (update.getKey().equals("email")) {
					u.email((String) update.getValue());
				}
				if (update.getKey().equals("address")) {
					Address address = a.getAddress();
					Address.AddressBuilder newAddress = Address.builder().country(address.getCountry())
							.latitude(address.getLatitude())
							.longitude(address.getLongitude())
							.locality(address.getLocality())
							.streetAddress(address.getStreetAddress())
							.country(address.getCountry())
							.postCode(address.getPostCode())
							.administrativeArea(address.getAdministrativeArea())
							.subAdministrativeArea(address.getSubAdministrativeArea());

					for (Map.Entry<String, Object> addressUpdate : updates.entrySet()) {
						if (addressUpdate.getKey().equals("administrativeArea")) {
							newAddress.administrativeArea((String) addressUpdate.getValue());
						}
						if (addressUpdate.getKey().equals("subAdministrativeAreaArea")) {
							newAddress.subAdministrativeArea((String) addressUpdate.getValue());
						}
						if (addressUpdate.getKey().equals("latitude")) {
							newAddress.latitude((Double) addressUpdate.getValue());
						}
						if (addressUpdate.getKey().equals("longitude")) {
							newAddress.longitude((Double) addressUpdate.getValue());
						}
						if (addressUpdate.getKey().equals("locality")) {
							newAddress.locality((String) addressUpdate.getValue());
						}
						if (addressUpdate.getKey().equals("postcode")) {
							newAddress.postCode((String) addressUpdate.getValue());
						}
						if (addressUpdate.getKey().equals("streetAddress")) {
							newAddress.streetAddress((String) addressUpdate.getValue());
						}
						if (addressUpdate.getKey().equals("country")) {
							newAddress.country((String) addressUpdate.getValue());
						}
					}
					u.address(newAddress.build());
				}


			}
			Mono<User> createdUser = this.userRepository.adminCreate(u.build())
					.convert().with(toMono());
			Mono<UserDetails> userMono = createdUser.map(
					v -> UserDetails.builder().email(v.getEmail()).firstName(v.getFirstName()).lastName(v.getLastName())
							.phoneNumber(v.getPhoneNumber()).userId(v.getUserId()).userName(v.getUserName())
							.address(AddressDetails.builder().administrativeArea(v.getAddress().getAdministrativeArea())
									.country(v.getAddress().getCountry()).latitude(v.getAddress().getLatitude())
									.longitude(v.getAddress().getLongitude()).locality(v.getAddress().getLocality())
									.postCode(v.getAddress().getPostCode()).streetAddress(v.getAddress().getStreetAddress())
									.subAdministrativeArea(v.getAddress().getSubAdministrativeArea()).build())
							.build());
			return userMono;

		});
	}



	public Mono<User> fullUpdateUserById(UUID id, UpdateUserCommand user) {
		CreateAddressCommand addressCommand = user.getAddress();

		Address address = Address.builder().streetAddress(addressCommand.getStreetAddress())
				.postCode(addressCommand.getPostCode()).longitude(addressCommand.getLongitude())
				.latitude(addressCommand.getLatitude()).administrativeArea(addressCommand.getAdministrativeArea())
				.subAdministrativeArea(addressCommand.getSubAdministrativeArea()).country(addressCommand.getCountry())
				.locality(addressCommand.getLocality()).build();

		String encodedPassword = this.passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		Mono<User> createdUser = this.userRepository.adminCreate(User.builder().userId(id).userName(user.getUserName())
				.firstName(user.getFirstName()).lastName(user.getLastName()).password(user.getPassword())
				.phoneNumber(user.getPhoneNumber()).email(user.getEmail()).address(address).userId(id).build())
				.convert().with(toMono());

		return createdUser.map(u -> u);
	}


	public Mono<User> getUserById(UUID userId) {
		return this.userRepository.getUserById(userId).convert().with(toMono());
	}

	public Mono<User> inactivateUserByUserId(String userId) {
		return this.userRepository.inactivateUserByUserId(userId).convert().with(toMono());
	}

}
