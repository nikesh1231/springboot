package com.evbackend.model.faults;

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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import com.evbackend.model.chargestation.Connector;

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
@Table(name = "faults")
@Component
public class Faults {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "faultId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	@Schema(description = "Unique identifier for the account")
	UUID faultId;

	@Schema(description = "Fault time", example = "2022-05-13T00:00:00.000")
	@Column(nullable = false)
	LocalDateTime faultTime;

	@Schema(description = "Fault Message", example = "String")
	@Column(nullable = false)
	String faultMessage;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "connectorId")
	Connector connector;

}
