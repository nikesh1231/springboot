package com.evbackend.model.chargestation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Builder
@Entity
@Table(name = "connectortype")
@Component
public class ConnectorType {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "connectorTypeId", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    UUID connectorTypeId;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    Double powerRatingKW;

    // Removed Bi Directional constraint
    /*
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "connectorId")
    Set<Connector> connectors;

     */

}
