package com.evbackend.model.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargeStationDTO {

	private UUID stationId;
	private String model;
	private String ip;
	private String host;
	private String version;

}
