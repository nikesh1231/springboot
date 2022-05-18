package com.evbackend.model.chargestation;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import com.evbackend.enumerate.ConnectorStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
@Table(name = "connector")
@Component
public class Connector {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "connectorId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    @Schema(description = "Unique identifier for the connector")
    UUID connectorId;

    @Schema(description = "Status of the connector", example="available")
    @Column(nullable = false)
    ConnectorStatus connectorStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "connectorTypeId")
    ConnectorType connectorType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chargeStationId")
    ChargeStation chargeStation;


}
