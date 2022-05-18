package com.evbackend.model.chargestation;

import com.evbackend.enumerate.ConnectorStatus;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectorDetails {

    ConnectorStatus connectorStatus;

    String connectorTypeName;

    Double powerRatingKW;
}
