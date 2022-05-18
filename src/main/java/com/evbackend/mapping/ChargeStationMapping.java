package com.evbackend.mapping;

import com.evbackend.model.address.Address;
import com.evbackend.model.address.AddressDetails;
import com.evbackend.model.chargestation.*;
import com.evbackend.model.users.User;
import com.evbackend.model.users.UserItem;

import java.util.List;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

public class ChargeStationMapping {


    public static ChargeStationDetails createChargeStationDetails(
            ChargeStation chargeStation,
            List<Review> reviews,
            List<Connector> connectors
    ) {
        ChargeSite cs = chargeStation.getChargeSite();
        Address address = cs.getAddress();
        AddressDetails addressDetails = AddressMapping.mapAddressToDetails(address);
        List<ConnectorDetails> connectorDetails = connectors.stream().map(z -> ConnectorDetails.builder()
                .connectorStatus(z.getConnectorStatus())
                .powerRatingKW(z.getConnectorType().getPowerRatingKW())
                .connectorTypeName(z.getConnectorType().getName()).build()).collect(Collectors.toList());

        List<ReviewDetails> reviewDetails = reviews
                .stream().map(z -> {
                    User k = z.getCreatedBy();
                    UserItem userItem = UserItem.builder()
                            .userId(k.getUserId())
                            .userName(k.getUserName())
                            .firstName(k.getFirstName())
                            .lastName(k.getLastName())
                            .build();
                    ReviewDetails rd = ReviewDetails.builder()
                            .reviewId(z.getReviewId())
                            .feedback(z.getFeedback())
                            .rating(z.getRating())
                            .userItem(userItem)
                            .createdAt(z.getCreatedAt().toEpochSecond(UTC)).build();
                    return rd;
                }).collect(Collectors.toList());

        return ChargeStationDetails.builder()
                .address(addressDetails)
                .name(chargeStation.getName())
                .id(chargeStation.getChargeStationId())
                .reviews(reviewDetails)
                .connectors(connectorDetails).build();
    }

}
