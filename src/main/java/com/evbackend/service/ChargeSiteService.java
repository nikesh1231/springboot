package com.evbackend.service;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

import java.util.List;
import java.util.UUID;

import com.evbackend.commands.FullUpdateChargeSiteCommand;
import com.evbackend.model.chargestation.ChargeSite;
import com.evbackend.model.users.Account;
import com.evbackend.model.users.User;
import com.evbackend.repository.AccountRepository;
import com.evbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evbackend.commands.CreateAddressCommand;
import com.evbackend.commands.CreateChargeSiteCommand;
import com.evbackend.model.address.Address;
import com.evbackend.repository.ChargeSiteRepository;
import com.evbackend.security.PBKDF2Encoder;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChargeSiteService {

	@Autowired
	PBKDF2Encoder passwordEncoder;

	private final ChargeSiteRepository chargeSiteRepository;
	private final UserRepository userRepository;

	private final AccountRepository accountRepository;

	public Mono<List<ChargeSite>> getAllChargeSites() {
		return this.chargeSiteRepository.getAllChargeSites().convert()
				.with(toMono());
	}

	// TODO: Link user id to created by

	public Mono<String> createChargeSite(UUID userId, CreateChargeSiteCommand chargeSiteCommand) {

		CreateAddressCommand addressCommand = chargeSiteCommand.getAddress();

		Address address = Address.builder().streetAddress(addressCommand.getStreetAddress())
				.postCode(addressCommand.getPostCode())
				.longitude(addressCommand.getLongitude())
				.latitude(addressCommand.getLatitude())
				.administrativeArea(addressCommand.getAdministrativeArea())
				.subAdministrativeArea(addressCommand.getSubAdministrativeArea())
				.country(addressCommand.getCountry())
				.locality(addressCommand.getLocality()).build();

		Mono<User> user = userRepository.getUserByUserId(userId).convert().with(toMono());
		Mono<Account> account = accountRepository.getAccount(chargeSiteCommand.getAccountId()).convert().with(toMono());


		Mono<ChargeSite> createdChargeSite = Mono.zip(user, account).flatMap(u -> this.chargeSiteRepository
				.createOrUpdateChargeSite(ChargeSite.builder()
						.activeSite(true)
						.address(address)
						.siteName(chargeSiteCommand.getSiteName())
						.accountId(u.getT2())
						.createdBy(u.getT1()).build())
				.convert().with(toMono()));

		return createdChargeSite.map(u -> u.getSiteId().toString());
	}

	public Mono<ChargeSite> fullUpdateChargeSiteById(UUID siteId, FullUpdateChargeSiteCommand chargeSiteCommand) {
		CreateAddressCommand addressCommand = chargeSiteCommand.getAddress();

		Address address = Address.builder()
				.streetAddress(addressCommand.getStreetAddress())
				.postCode(addressCommand.getPostCode())
				.longitude(addressCommand.getLongitude())
				.latitude(addressCommand.getLatitude())
				.administrativeArea(addressCommand.getAdministrativeArea())
				.subAdministrativeArea(addressCommand.getSubAdministrativeArea())
				.country(addressCommand.getCountry())
				.locality(addressCommand.getLocality()).build();

		Mono<Account> account = accountRepository.getAccount(chargeSiteCommand.getAccountId()).convert().with(toMono());

		Mono<ChargeSite> createdChargeSite = account.flatMap(u -> this.chargeSiteRepository
				.createOrUpdateChargeSite(ChargeSite.builder()
						.activeSite(chargeSiteCommand.isActiveSite())
						.address(address)
						.accountId(u)
						.siteName(chargeSiteCommand.getSiteName())
						.siteId(siteId).build())
				.convert().with(toMono()));

		return createdChargeSite.map(u -> u);
	}


	public Mono<ChargeSite> getChargeSiteById(UUID siteId) {
		return this.chargeSiteRepository.getChargeSiteById(siteId).convert().with(toMono());
	}
	
	public Mono<List<ChargeSite>> getChargeSiteByAccountId(UUID accountId) {
		Account account = new Account();
		account.setAccountId(accountId);
		return this.chargeSiteRepository.getChargeSiteByAccountId(account).convert().with(toMono());
	}

}
