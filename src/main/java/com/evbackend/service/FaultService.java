package com.evbackend.service;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.evbackend.model.chargestation.Connector;
import com.evbackend.model.faults.Faults;
import com.evbackend.repository.FaultRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FaultService {

	private final FaultRepository faultRepository;
	private final ConnectorService connectorService;

	public Mono<List<Faults>> getAllFaults() {
		return this.faultRepository.getAllFaults().convert().with(toMono());
	}

	public Mono<List<Faults>> getFaultsByIdAndParamType(UUID id, String paramType) {
		Mono<List<Faults>> faults = null;
		if (paramType.equals("chargeStationId")) {
			faults = this.getFaultsByChargeStation(id);
		} else if (paramType.equals("chargeSiteId")) {
			faults = this.getFaultsByChargeSite(id);
		} else if (paramType.equals("accountId")) {
			faults = this.getFaultsByAccountId(id);
		}
		return faults;
	}

	private Mono<List<Faults>> getFaultsByAccountId(UUID id) {
		return connectorService.getConnectorsByAccountId(id).flatMap(c -> {
			List<Connector> connectors = c.stream()
					.map(i -> Connector.builder().connectorId(i.getConnectorId()).build()).collect(Collectors.toList());
			return faultRepository.getFaultsByConnectors(connectors).convert().with(toMono());
		});
	}

	private Mono<List<Faults>> getFaultsByChargeSite(UUID id) {
		return connectorService.getConnectorsByChargeSiteId(id).flatMap(c -> {
			List<Connector> connectors = c.stream()
					.map(i -> Connector.builder().connectorId(i.getConnectorId()).build()).collect(Collectors.toList());
			return faultRepository.getFaultsByConnectors(connectors).convert().with(toMono());
		});
	}

	private Mono<List<Faults>> getFaultsByChargeStation(UUID id) {
		return connectorService.getConnectorsByChargeStationId(id).flatMap(c -> {
			List<Connector> connectors = c.stream()
					.map(i -> Connector.builder().connectorId(i.getConnectorId()).build()).collect(Collectors.toList());
			return faultRepository.getFaultsByConnectors(connectors).convert().with(toMono());
		});
	}

}
