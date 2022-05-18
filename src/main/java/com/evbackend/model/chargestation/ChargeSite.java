package com.evbackend.model.chargestation;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import com.evbackend.model.users.Account;
import com.evbackend.model.users.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import com.evbackend.model.address.Address;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
@Table(name = "chargesite")
@Component
public class ChargeSite {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "siteId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	UUID siteId;

	@Column(nullable = false)
	String siteName;

	@Column(nullable = false)
	Boolean activeSite;

	@Builder.Default
	@Column(name = "createdAt")
	@CreationTimestamp
	LocalDateTime createdAt = LocalDateTime.now();

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "addressId", referencedColumnName = "addressId")
	@JsonManagedReference
	Address address;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "createdBy")
	User createdBy;

	// Removed Bi Directional constraint
	/*
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "chargeStationId")
	Set<ChargeStation> chargeStations;
	*/

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accountId")
	Account accountId;

}
