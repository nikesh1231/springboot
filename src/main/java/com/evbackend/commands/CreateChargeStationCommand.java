package com.evbackend.commands;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateChargeStationCommand implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8016623456222777104L;

	@NonNull
	String name;

	@NonNull
	UUID chargerStationModelId;

	@NonNull
	String ip;

	@NonNull
	String host;

	@NonNull
	String password;

	@NonNull
	String version;

	@NonNull
	UUID siteId;

	@NonNull
	List<UUID> connectorTypes;

}
