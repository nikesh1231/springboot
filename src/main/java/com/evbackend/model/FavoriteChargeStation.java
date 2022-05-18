package com.evbackend.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;

import com.evbackend.model.chargestation.ChargeStation;
import com.evbackend.model.users.User;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
@Table(name = "favoritechargestation")
@Component
public class FavoriteChargeStation implements Serializable {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
	@Type(type = "uuid-char")
	UUID id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	User userId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "chargeStationId")
	ChargeStation chargeStationId;
	
}
