package com.evbackend.commands;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
@ToString
public class CreateChargeSiteCommand implements  Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1676077655130790265L;

	@NonNull
	String siteName;

	@NonNull
	CreateAddressCommand address;

	@NonNull
	UUID accountId;
}
