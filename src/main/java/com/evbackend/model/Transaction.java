package com.evbackend.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.evbackend.model.chargestation.ChargeStation;
import com.evbackend.model.chargestation.Connector;
import com.evbackend.model.users.User;
import com.evbackend.model.vehicle.Vehicle;
import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
@Table(name = "transaction")
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "transactionId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Schema(description = "Unique identifier for the transaction")
    UUID transactionId;

    @Column(unique = true)
    Integer transactionIdentifier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", updatable = false)
    @JsonBackReference
    User userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicleId")
    Vehicle vehicleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "connectorId")
    Connector connectorId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chargeStationId")
    ChargeStation chargeStationId;

    LocalDateTime startTimeOfCharge;

    LocalDateTime endTimeOfCharge;

    LocalDateTime startPlugTime;

    LocalDateTime endPlugTime;

    Double energy;

    Double cost;

    Boolean paid;
}
