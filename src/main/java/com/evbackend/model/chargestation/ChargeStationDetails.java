package com.evbackend.model.chargestation;

import java.util.List;
import java.util.UUID;

import com.evbackend.model.address.AddressDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargeStationDetails {

    UUID id;

    String name;

    AddressDetails address;

    List<ReviewDetails> reviews;

    List<ConnectorDetails> connectors;

}
