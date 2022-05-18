package com.evbackend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import com.evbackend.model.chargestation.Connector;
import com.evbackend.model.faults.Faults;

import io.smallrye.mutiny.Uni;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Faults")
@Component
@RequiredArgsConstructor
public class FaultRepository {

	private final Mutiny.SessionFactory sessionFactory;

	public Uni<List<Faults>> getAllFaults() {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Faults> query = cb.createQuery(Faults.class);
		Root<Faults> root = query.from(Faults.class);
		return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
	}

	public Uni<List<Faults>> getFaultsByConnectors(List<Connector> connectors) {
		List<UUID> connectorIds = new ArrayList<UUID>();
		connectors.forEach(connector -> {
			connectorIds.add(connector.getConnectorId());
		});
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Faults> query = cb.createQuery(Faults.class);
		Root<Faults> root = query.from(Faults.class);
		query.select(root).where(cb.in(root.get("connector")).value(connectors));
		return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
	}

}
