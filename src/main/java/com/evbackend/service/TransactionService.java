package com.evbackend.service;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.evbackend.model.Transaction;
import com.evbackend.model.dto.TransactionResponseDTO;
import com.evbackend.model.vehicle.Vehicle;
import com.evbackend.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;


	public Mono<List<TransactionResponseDTO>> getTransactionsByTimePeriod(UUID uuid, LocalDateTime startDate,
			LocalDateTime endDate) {
		System.out.println("Start Date = "+startDate.toString()+"\nEnd Date = "+endDate.toString());
		return this.transactionRepository.getTransactionsByTimePeriod(uuid, startDate, endDate).map(this::toList)
				.convert().with(toMono());
	}

	public Mono<List<TransactionResponseDTO>> getTransactionsByTimePeriod(LocalDateTime startDate,
			LocalDateTime endDate) {
		return this.transactionRepository.getTransactionsByTimePeriod(startDate, endDate).map(this::toList).convert()
				.with(toMono());
	}
    public Mono<List<TransactionResponseDTO>> getAllTransactions(UUID accountUuid, UUID chargeStation,
            UUID chargeSite) {
        return this.transactionRepository.getAllTransactions(accountUuid, chargeStation, chargeSite).map(this::toList)
                .convert().with(toMono());
    }

    public Mono<List<TransactionResponseDTO>> getAllTransactions(UUID userId, UUID accountUuid, UUID chargeStation,
            UUID chargeSite) {
        return this.transactionRepository.getAllTransactions(userId, accountUuid, chargeStation, chargeSite)
                .map(this::toList).convert().with(toMono());
    }

	private List<TransactionResponseDTO> toList(List<Transaction> list) {
		return list.stream().map(TransactionResponseDTO::new).collect(Collectors.toList());
	}

	public Mono<List<TransactionResponseDTO>> getTransactionsByVehicleId(String vehicleId) {
		/*
		 * Vehicle vehicle = new Vehicle();
		 * vehicle.setVehicleId(UUID.fromString(vehicleId));
		 */
		return this.transactionRepository.getTransactionsByVehicleId(vehicleId).map(this::toList).convert()
				.with(toMono());
	}

	public Mono<List<TransactionResponseDTO>> getTransactionsByVehicleIdAndTimePeriod(String vehicleId, LocalDateTime startDate, LocalDateTime endDate) {
		return this.transactionRepository.getTransactionsByVehicleIdAndTimePeriod(vehicleId, startDate, endDate).map(this::toList).convert()
				.with(toMono());
	}
	
}
