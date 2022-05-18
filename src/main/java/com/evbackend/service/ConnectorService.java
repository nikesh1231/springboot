package com.evbackend.service;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.evbackend.model.chargestation.ChargeSite;
import com.evbackend.model.chargestation.ChargeStation;
import com.evbackend.model.chargestation.Connector;
import com.evbackend.model.users.Account;
import com.evbackend.repository.ConnectorRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConnectorService {
	
	private final ConnectorRepository connectorRepository;
	
	public Mono<List<Connector>> getConnectorsByChargeStationId(UUID chargeStationId) {
		ChargeStation chargeStation = new ChargeStation();
		chargeStation.setChargeStationId(chargeStationId);
		return connectorRepository.getConnectorsByChargeStationId(chargeStation).convert().with(toMono()).map(u -> u);
	}

	public Mono<List<Connector>> getConnectorsByChargeSiteId(UUID id) {
		ChargeStation chargeStation = new ChargeStation();
		ChargeSite chargeSite = new ChargeSite();
		chargeSite.setSiteId(id);
		chargeStation.setChargeSite(chargeSite);
		return connectorRepository.getConnectorsByChargeSiteId(chargeStation).convert().with(toMono()).map(u -> u);
	}

	public Mono<List<Connector>> getConnectorsByAccountId(UUID id) {
		ChargeStation chargeStation = new ChargeStation();
		ChargeSite chargeSite = new ChargeSite();
		Account account = new Account();
		account.setAccountId(id);
		chargeSite.setAccountId(account);
		chargeStation.setChargeSite(chargeSite);
		return connectorRepository.getConnectorsByAccountId(chargeStation).convert().with(toMono()).map(u -> u);
	}

}
