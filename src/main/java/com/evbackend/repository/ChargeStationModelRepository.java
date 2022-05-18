package com.evbackend.repository;

import com.evbackend.model.chargestation.ChargeStationModel;
import io.smallrye.mutiny.Uni;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Tag(name = "ChargeStationModel")
@Component
@RequiredArgsConstructor
public class ChargeStationModelRepository {

    private final Mutiny.SessionFactory sessionFactory;

    public Uni<List<ChargeStationModel>> getAllChargeStationModel() {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<ChargeStationModel> query = cb.createQuery(ChargeStationModel.class);
        Root<ChargeStationModel> root = query.from(ChargeStationModel.class);
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<ChargeStationModel> getChargeStationModelId(UUID userId) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        // create query
        CriteriaQuery<ChargeStationModel> query = cb.createQuery(ChargeStationModel.class);
        // set the root class
        Root<ChargeStationModel> root = query.from(ChargeStationModel.class);
        query.where(cb.equal(root.get("chargeStationModelId"), userId.toString()));

        return this.sessionFactory.withSession(session -> session.createQuery(query).getSingleResultOrNull())
                .onItem()
                .ifNull()
                .switchTo(
                        Uni.createFrom().item(ChargeStationModel.builder().chargeStationModelId(userId).build())
                );
    }

}
