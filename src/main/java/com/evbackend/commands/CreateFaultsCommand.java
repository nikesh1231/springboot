package com.evbackend.commands;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateFaultsCommand implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8921164533121839065L;

	@NonNull
	@Schema(description = "Fault time", example = "2022-05-13T00:00:00.000")
	LocalDateTime faultTime;

	@NonNull
	@Schema(description = "Fault Message", example = "String")
	String faultMessage;

	@NonNull
	List<UUID> connectors;

}
