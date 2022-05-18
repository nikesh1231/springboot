package com.evbackend.model.chargestation;

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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import com.evbackend.model.users.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
@Table(name = "chargestation")
@Component
public class ChargeStation {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "chargeStationId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	UUID chargeStationId;

	@Column(nullable = true)
	String name;

	@Column(nullable = true)
	String ip;

	@Column(nullable = true)
	String gateway;

	@Column(nullable = true)
	String mac;

	@Column(nullable = true)
	String mask;

	@Column(nullable = true)
	String dns;

	@Column(nullable = true)
	String host;

	@Column(nullable = false)
	String password;

	@Column(nullable = true)
	String version;

	@Builder.Default
	@Column(name = "createdAt")
	@CreationTimestamp
	LocalDateTime createdAt = LocalDateTime.now();

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "createdBy")
	User createdBy;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "chargeSiteId")
	ChargeSite chargeSite;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "chargeStationModelId")
	ChargeStationModel chargeStationModel;


}
