package com.evbackend.repository;

import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import com.evbackend.model.chargestation.ChargeSite;
import com.evbackend.model.users.Account;

import io.smallrye.mutiny.Uni;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "ChargeSite")
@Component
@RequiredArgsConstructor
public class ChargeSiteRepository {

	private final Mutiny.SessionFactory sessionFactory;

	public Uni<List<ChargeSite>> getAllChargeSites() {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		CriteriaQuery<ChargeSite> query = cb.createQuery(ChargeSite.class);
		Root<ChargeSite> root = query.from(ChargeSite.class);
		return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
	}

	public Uni<ChargeSite> createOrUpdateChargeSite(ChargeSite ChargeSite) {
		if (ChargeSite.getSiteId() == null) {
			return this.sessionFactory
					.withSession(session -> session.persist(ChargeSite).chain(session::flush).replaceWith(ChargeSite));
		} else {
			return this.sessionFactory.withSession(session -> session.merge(ChargeSite).onItem().call(session::flush));
		}
	}

	public Uni<ChargeSite> getChargeSiteById(UUID chargeSiteId) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		// create query
		CriteriaQuery<ChargeSite> query = cb.createQuery(ChargeSite.class);
		// set the root class
		Root<ChargeSite> root = query.from(ChargeSite.class);
		query.where(cb.equal(root.get("siteId"), chargeSiteId.toString()));

		return this.sessionFactory.withSession(session -> session.createQuery(query)
						.getSingleResultOrNull()).onItem()
				.ifNull().switchTo(Uni.createFrom().item(ChargeSite.builder().siteId(chargeSiteId).build()));
	}

	public Uni<ChargeSite> partialUpdateChargeSiteById(ChargeSite chargeSite) {
		return this.sessionFactory.withSession(session -> session.merge(chargeSite).onItem().call(session::flush));
	}
	
	public Uni<List<ChargeSite>> getChargeSiteByAccountId(Account account) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		// create query
		CriteriaQuery<ChargeSite> query = cb.createQuery(ChargeSite.class);
		// set the root class
		Root<ChargeSite> root = query.from(ChargeSite.class);
		query.where(cb.equal(root.get("accountId"), account.getAccountId().toString()));

		return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
	}

}
