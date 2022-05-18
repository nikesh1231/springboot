package com.evbackend.service;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evbackend.commands.CreateChargeStationCommand;
import com.evbackend.model.FavoriteChargeStation;
import com.evbackend.model.address.AddressDetails;
import com.evbackend.model.chargestation.ChargeSite;
import com.evbackend.model.chargestation.ChargeStation;
import com.evbackend.model.chargestation.ChargeStationItem;
import com.evbackend.model.chargestation.ChargeStationModel;
import com.evbackend.model.chargestation.Connector;
import com.evbackend.model.users.User;
import com.evbackend.repository.ChargeSiteRepository;
import com.evbackend.repository.ChargeStationModelRepository;
import com.evbackend.repository.ChargeStationRepository;
import com.evbackend.repository.UserRepository;
import com.evbackend.security.PBKDF2Encoder;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChargeStationService {

	@Autowired
	PBKDF2Encoder passwordEncoder;

	private final ChargeStationRepository chargeStationRepository;

	private final ChargeStationModelRepository chargeStationModelRepository;
	private final ChargeSiteRepository chargeSiteRepository;

	private final UserRepository userRepository;

	public Mono<List<ChargeStation>> getAllChargeStations(Double latitude, Double longitude) {
		if (latitude != null && longitude != null) {
			return this.chargeStationRepository.getAllChargeStations(latitude, longitude).convert().with(toMono());
		} else
			return this.chargeStationRepository.getAllChargeStations().convert().with(toMono());
	}

	public Mono<ChargeStationItem> createChargeStation(UUID userId, CreateChargeStationCommand chargeStationCommand) {
		UUID siteID = chargeStationCommand.getSiteId();

		Mono<ChargeSite> chargeSite = chargeSiteRepository.getChargeSiteById(siteID).convert().with(toMono());
		Mono<User> user = userRepository.getUserByUserId(userId).convert().with(toMono());
		Mono<ChargeStationModel> chargeStationModel = chargeStationModelRepository
				.getChargeStationModelId(chargeStationCommand.getChargerStationModelId()).convert().with(toMono());

		String encodedPassword = this.passwordEncoder.encode(chargeStationCommand.getPassword());

		Mono<ChargeStation> createdChargeStation = Mono.zip(chargeSite, user, chargeStationModel)
				.flatMap(t -> this.chargeStationRepository.createOrUpdateChargeStation(ChargeStation.builder()
						.host(chargeStationCommand.getHost()).ip(chargeStationCommand.getIp())
						.chargeStationModel(t.getT3()).password(encodedPassword).chargeSite(t.getT1())
						.version(chargeStationCommand.getVersion()).name(chargeStationCommand.getName()).createdBy(t.getT1().getCreatedBy()).build()).convert().with(toMono()));

		Mono<ChargeStationItem> chargeStationMono = createdChargeStation.map(u -> ChargeStationItem.builder()
				.siteId(u.getChargeSite().getSiteId()).siteName(u.getChargeSite().getSiteName())
				.stationId(u.getChargeStationId()).stationName(u.getName())
				.addressDetails(AddressDetails.builder()
						.administrativeArea(u.getChargeSite().getAddress().getAdministrativeArea())
						.country(u.getChargeSite().getAddress().getCountry())
						.latitude(u.getChargeSite().getAddress().getLatitude())
						.longitude(u.getChargeSite().getAddress().getLongitude())
						.locality(u.getChargeSite().getAddress().getLocality())
						.postCode(u.getChargeSite().getAddress().getPostCode())
						.streetAddress(u.getChargeSite().getAddress().getStreetAddress())
						.subAdministrativeArea(u.getChargeSite().getAddress().getSubAdministrativeArea()).build())
				.build());
		return chargeStationMono.map(u -> u);
	}

	public Mono<ChargeStation> fullUpdateChargeStationById(UUID userId, UUID stationId,
			CreateChargeStationCommand chargeStationCommand) {

		UUID siteID = chargeStationCommand.getSiteId();
		Mono<User> user = userRepository.getUserByUserName(userId.toString()).convert().with(toMono());

		Mono<ChargeSite> chargeSite = chargeSiteRepository.getChargeSiteById(siteID).convert().with(toMono());
		String encodedPassword = this.passwordEncoder.encode(chargeStationCommand.getPassword());

		Mono<ChargeStationModel> chargeStationModel = chargeStationModelRepository
				.getChargeStationModelId(chargeStationCommand.getChargerStationModelId()).convert().with(toMono());

		Mono<ChargeStation> createdChargeStation = Mono.zip(chargeSite, user, chargeStationModel)
				.flatMap(
						t -> this.chargeStationRepository
								.createOrUpdateChargeStation(
										ChargeStation.builder().host(chargeStationCommand.getHost())
												.ip(chargeStationCommand.getIp()).chargeStationModel(t.getT3())
												.password(encodedPassword).version(chargeStationCommand.getVersion())
												.chargeSite(t.getT1()).chargeStationId(stationId).build())
								.convert().with(toMono()));

		return createdChargeStation.map(u -> u);
	}

	public Mono<String> addFavouriteChargeStation(UUID userId, UUID stationId) {
		Mono<User> user = userRepository.getUserByUserId(userId).convert().with(toMono());
		Mono<ChargeStation> chargeStation = chargeStationRepository.getChargeStationById(stationId).convert()
				.with(toMono());
		Mono<FavoriteChargeStation> favouriteChargeStation = Mono.zip(chargeStation, user).flatMap(t -> {
			return chargeStationRepository
					.addFavoriteChargeStation(
							FavoriteChargeStation.builder().chargeStationId(t.getT1()).userId(t.getT2()).build())
					.convert().with(toMono());
		});
		return favouriteChargeStation.map(u -> u.getId().toString());
	}

	public Mono<String> removeFavouriteChargeStation(UUID userId, UUID stationId) {
		Mono<User> user = userRepository.getUserByUserId(userId).convert().with(toMono());
		Mono<ChargeStation> chargeStation = chargeStationRepository.getChargeStationById(stationId).convert()
				.with(toMono());
		Mono<Integer> favouriteChargeStation = Mono.zip(chargeStation, user).flatMap(t -> {
			return chargeStationRepository
					.removeFavoriteChargeStation(
							FavoriteChargeStation.builder().chargeStationId(t.getT1()).userId(t.getT2()).build())
					.convert().with(toMono());
		});
		return favouriteChargeStation.map(u -> u.toString());
	}

	public Mono<ChargeStation> getChargeStationById(UUID stationId) {
		return this.chargeStationRepository.getChargeStationById(stationId).convert().with(toMono());
	}

	public Mono<List<Connector>> getConnectorsForChargeStationId(UUID chargeStationId) {
		return this.chargeStationRepository.getConnectorsForChargeStation(chargeStationId).convert().with(toMono());
	}

	public Mono<List<FavoriteChargeStation>> favouriteChargeStations(UUID stationId) {
		return this.chargeStationRepository.getFavorites(stationId.toString()).convert().with(toMono());
	}

}