package com.evbackend.commands;

import java.io.Serializable;

import io.micrometer.core.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class FavoriteChargeStationCommand implements Serializable{

	@NonNull
	private String chargeStationId;

}
