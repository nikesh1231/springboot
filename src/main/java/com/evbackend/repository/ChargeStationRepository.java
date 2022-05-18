package com.evbackend.repository;

import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.evbackend.model.FavoriteChargeStation;
import com.evbackend.model.chargestation.ChargeStation;
import com.evbackend.model.chargestation.Connector;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import io.smallrye.mutiny.Uni;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "ChargeStation")
@Component
@RequiredArgsConstructor
public class ChargeStationRepository {

	private final Mutiny.SessionFactory sessionFactory;

	public Uni<List<ChargeStation>> getAllChargeStations() {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		CriteriaQuery<ChargeStation> query = cb.createQuery(ChargeStation.class);
		Root<ChargeStation> root = query.from(ChargeStation.class);
		return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
	}
	
	public Uni<ChargeStation> createOrUpdateChargeStation(ChargeStation chargeStation) {
		if (chargeStation.getChargeStationId() == null) {
			return this.sessionFactory.withSession(
					session -> session.persist(chargeStation).chain(session::flush).replaceWith(chargeStation));
        } else {
            return this.sessionFactory.withSession(session -> session.merge(chargeStation).onItem().call(session::flush));
        }
	}

	public Uni<ChargeStation> getChargeStationById(UUID chargeStationId) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		// create query
		CriteriaQuery<ChargeStation> query = cb.createQuery(ChargeStation.class);
		// set the root class
		Root<ChargeStation> root = query.from(ChargeStation.class);
		query.where(cb.equal(root.get("chargeStationId"), chargeStationId.toString()));

		return this.sessionFactory.withSession(session -> session.createQuery(query).getSingleResultOrNull()).onItem()
				.ifNull().switchTo(Uni.createFrom().item(ChargeStation.builder().chargeStationId(chargeStationId).build()));
	}

	public Uni<List<Connector>> getConnectorsForChargeStation(UUID chargeStationId) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Connector> query = cb.createQuery(Connector.class);
		ParameterExpression<UUID> p = cb.parameter(UUID.class);

		Root<Connector> root = query.from(Connector.class);
		query.where(cb.equal(root.get("chargeStationId"), chargeStationId.toString()));
		return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
	}

	public Uni<ChargeStation> partialUpdateChargeStationById(ChargeStation chargeStation) {
		return this.sessionFactory.withSession(session -> session.merge(chargeStation).onItem().call(session::flush));
	}

	public Uni<FavoriteChargeStation> addFavoriteChargeStation(FavoriteChargeStation favChargeStation) {
		return this.sessionFactory.withSession(
				session -> session.persist(favChargeStation).chain(session::flush).replaceWith(favChargeStation));
	}

	public Uni<Integer> removeFavoriteChargeStation(FavoriteChargeStation favChargeStation) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		// create delete
		CriteriaDelete<FavoriteChargeStation> delete = cb.createCriteriaDelete(FavoriteChargeStation.class);
		// set the root class
		Root<FavoriteChargeStation> root = delete.from(FavoriteChargeStation.class);
		// set where clause
		Predicate predicateForUserId = cb.equal(root.get("user_id"), favChargeStation.getUserId());
		Predicate predicateForStationId = cb.equal(root.get("charge_station_id"), favChargeStation.getChargeStationId());
		Predicate finalPredicate = cb.and(predicateForUserId, predicateForStationId);
		delete.where(finalPredicate);
		return this.sessionFactory.withTransaction((session, tx) -> session.createQuery(delete).executeUpdate());
	}

	public Uni<List<FavoriteChargeStation>> getFavorites(String user_id) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		// create query
		CriteriaQuery<FavoriteChargeStation> query = cb.createQuery(FavoriteChargeStation.class);
		// set the root class
		Root<FavoriteChargeStation> root = query.from(FavoriteChargeStation.class);
		query.where(cb.equal(root.get("user_id"), user_id));
		return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
	}

	public Uni<List<ChargeStation>> getAllChargeStations(Double latitude, Double longitude) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		CriteriaQuery<ChargeStation> query = cb.createQuery(ChargeStation.class);
		Root<ChargeStation> root = query.from(ChargeStation.class);
		return this.sessionFactory.withSession(
				session -> session.createNativeQuery(
						"SELECT cs.*, 111.111 * DEGREES(ACOS(LEAST(1.0, COS(RADIANS(a.latitude))* COS(RADIANS(?1)) * COS(RADIANS(a.longitude - ?2)) + SIN(RADIANS(a.latitude)) * SIN(RADIANS(?1))))) AS distance FROM	chargestation cs join connector c on c.chargeStationId = cs.chargeStationId JOIN chargesite ch on	ch.siteId = cs.chargeSiteId JOIN address a on a.addressId = ch.addressId WHERE c.connectorStatus = 0 ORDER by distance ASC",
						ChargeStation.class).setParameter(1, latitude).setParameter(2, longitude)
						.getResultList());
	}
}
