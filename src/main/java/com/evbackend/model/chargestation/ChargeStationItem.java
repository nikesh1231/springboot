package com.evbackend.model.chargestation;

import com.evbackend.model.address.AddressDetails;
import lombok.*;

import java.util.UUID;

import org.springframework.util.ObjectUtils;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargeStationItem {

	private UUID stationId;
	private String stationName;
	private UUID siteId;
	private String siteName;
	private AddressDetails addressDetails;

	public ChargeStationItem(ChargeStation chargeStation) {
    	this.stationId = chargeStation.getChargeStationId();
    	this.stationName = chargeStation.getName();
    	
    	if(!ObjectUtils.isEmpty(chargeStation.getChargeSite().getSiteId())) {
    		this.siteId = chargeStation.getChargeSite().getSiteId();
    		this.siteName = chargeStation.getChargeSite().getSiteName();
    		if(!ObjectUtils.isEmpty(chargeStation.getChargeSite().getAddress().getAddressId())) {
    			this.addressDetails.setAdministrativeArea(chargeStation.getChargeSite().getAddress().getAdministrativeArea());
                this.addressDetails.setCountry(chargeStation.getChargeSite().getAddress().getCountry());
                this.addressDetails.setLatitude(chargeStation.getChargeSite().getAddress().getLatitude());
                this.addressDetails.setLocality(chargeStation.getChargeSite().getAddress().getLocality());
                this.addressDetails.setLongitude(chargeStation.getChargeSite().getAddress().getLongitude());
                this.addressDetails.setPostCode(chargeStation.getChargeSite().getAddress().getPostCode());
                this.addressDetails.setStreetAddress(chargeStation.getChargeSite().getAddress().getStreetAddress());
                this.addressDetails.setSubAdministrativeArea(chargeStation.getChargeSite().getAddress().getSubAdministrativeArea());
    		}
    	}
	}

}
