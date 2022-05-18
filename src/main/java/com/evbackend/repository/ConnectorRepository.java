package com.evbackend.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import com.evbackend.model.chargestation.ChargeSite;
import com.evbackend.model.chargestation.ChargeStation;
import com.evbackend.model.chargestation.Connector;
import com.evbackend.model.users.Account;

import io.smallrye.mutiny.Uni;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Connector")
@Component
@RequiredArgsConstructor
public class ConnectorRepository {

	private final Mutiny.SessionFactory sessionFactory;
	
	public Uni<List<Connector>> getConnectorsByChargeStationId(ChargeStation chargeStation) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		// create query
		CriteriaQuery<Connector> query = cb.createQuery(Connector.class);
		// set the root class
		Root<Connector> root = query.from(Connector.class);
		query.where(cb.equal(root.get("chargeStation"), chargeStation));

		return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
	}

	public Uni<List<Connector>> getConnectorsByChargeSiteId(ChargeStation chargeStation) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		// create query
		CriteriaQuery<Connector> query = cb.createQuery(Connector.class);
		// set the root class
		Root<Connector> root = query.from(Connector.class);
		query.where(cb.equal(root.get("chargeStation").get("chargeSite"), chargeStation.getChargeSite()));

		return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
	}

	public Uni<List<Connector>> getConnectorsByAccountId(ChargeStation chargeStation) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		// create query
		CriteriaQuery<Connector> query = cb.createQuery(Connector.class);
		// set the root class
		Root<Connector> root = query.from(Connector.class);
		query.where(cb.equal(root.get("chargeStation").get("chargeSite").get("accountId"), chargeStation.getChargeSite().getAccountId()));

		return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
	}
	
}
