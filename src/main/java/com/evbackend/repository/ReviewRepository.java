package com.evbackend.repository;

import com.evbackend.model.chargestation.Connector;
import com.evbackend.model.chargestation.Review;
import io.smallrye.mutiny.Uni;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Tag(name="Review")
@Component
@RequiredArgsConstructor
public class ReviewRepository {

    private final Mutiny.SessionFactory sessionFactory;

    public Uni<List<Review>> getReviewsForChargeStation(UUID chargeStationId) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Review> query = cb.createQuery(Review.class);
        ParameterExpression<UUID> p = cb.parameter(UUID.class);

        Root<Review> root = query.from(Review.class);
        query.where(cb.equal(root.get("chargeStationId"), chargeStationId.toString()));
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }



    public Uni<Review> create(Review review) {
        return this.sessionFactory.withSession(session -> session.merge(review).onItem().call(session::flush));
    }

}
