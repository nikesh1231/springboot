package com.evbackend.model.dto;

import java.util.UUID;

import com.evbackend.model.Transaction;
import com.evbackend.model.address.Address;
import com.evbackend.util.DateUtil;

import org.springframework.util.ObjectUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionResponseDTO {

    UUID transactionId;

    Integer transactionIdentifier;

    Long startTimeOfCharge;

    Long endTimeOfCharge;

    Double energy;

    Double cost;

    Boolean paid;

    UUID stationId;

    String stationName;

    Address stationAddress;

    UUID vehicleId;

    UUID vehicleVersion;

    String vehicleYear;

    String vehicleManufacturer;

    public TransactionResponseDTO(Transaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.transactionIdentifier = transaction.getTransactionIdentifier();
        this.startTimeOfCharge = transaction.getStartTimeOfCharge() != null
                ? DateUtil.toSecond(transaction.getStartTimeOfCharge())
                : null;
        this.endTimeOfCharge = transaction.getEndTimeOfCharge() != null
                ? DateUtil.toSecond(transaction.getEndTimeOfCharge())
                : null;
        this.energy = transaction.getEnergy();
        this.cost = transaction.getCost();
        this.paid = transaction.getPaid();

        if (!ObjectUtils.isEmpty(transaction.getChargeStationId())) {
            this.stationId = transaction.getChargeStationId().getChargeStationId();
            this.stationName = transaction.getChargeStationId().getName();
            if (!ObjectUtils.isEmpty(transaction.getChargeStationId().getChargeSite())) {
                this.stationAddress = transaction.getChargeStationId().getChargeSite().getAddress();
            }
        }

        if (!ObjectUtils.isEmpty(transaction.getVehicleId())) {
            this.vehicleId = transaction.getVehicleId().getVehicleId();

            if (!ObjectUtils.isEmpty(transaction.getVehicleId().getVehicleVersionId())) {
                this.vehicleVersion = transaction.getVehicleId().getVehicleVersionId().getVehicleVersionId();
                this.vehicleYear = transaction.getVehicleId().getVehicleVersionId().getYear();

                if (!ObjectUtils.isEmpty(transaction.getVehicleId().getVehicleVersionId().getVehicleModelId())
                        && !ObjectUtils.isEmpty(transaction.getVehicleId().getVehicleVersionId().getVehicleModelId()
                                .getVehicleModelId())
                        && !ObjectUtils.isEmpty(transaction.getVehicleId().getVehicleVersionId().getVehicleModelId()
                                .getVehicleManufacturerId())) {
                    this.vehicleManufacturer = transaction.getVehicleId().getVehicleVersionId().getVehicleModelId()
                            .getVehicleManufacturerId().getName();
                }
            }

        }
    }

}
