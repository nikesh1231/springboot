package com.evbackend.repository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.evbackend.model.AppConstants;
import com.evbackend.model.Transaction;
import com.evbackend.model.chargestation.ChargeSite;
import com.evbackend.model.chargestation.ChargeStation;
import com.evbackend.model.users.Account;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {

	private final Mutiny.SessionFactory sessionFactory;

    public Uni<List<Transaction>> getAllTransactions(UUID accountUuid, UUID chargeStation, UUID chargeSite) {
        List<Predicate> predList = new LinkedList<Predicate>();
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class);
        Root<Transaction> root = query.from(Transaction.class);
        filterTransactions(query, cb, predList, root, accountUuid, chargeStation, chargeSite);
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<List<Transaction>> getAllTransactions(UUID userId,UUID accountUuid, UUID chargeStation, UUID chargeSite) {
        List<Predicate> predList = new LinkedList<Predicate>();
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class);
        Root<Transaction> root = query.from(Transaction.class);
        predList.add(cb.equal(root.get(AppConstants.USER_ID), userId.toString()));
        filterTransactions(query, cb, predList, root, accountUuid, chargeStation, chargeSite);
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<List<Transaction>> getTransactionsByTimePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class);
        Root<Transaction> root = query.from(Transaction.class);
        query.where(cb.between(root.get(AppConstants.STARTTIMEOFCHARGE), startDate, endDate));
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<List<Transaction>> getTransactionsByTimePeriod(UUID userId, LocalDateTime startDate, LocalDateTime endDate) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class);
        Root<Transaction> root = query.from(Transaction.class);
        query.where(cb.between(root.get(AppConstants.STARTTIMEOFCHARGE), startDate, endDate), cb.equal(root.get(AppConstants.USER_ID), userId.toString()));
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    private void filterTransactions(CriteriaQuery<Transaction> query, CriteriaBuilder cb,  List<Predicate> predList, Root<Transaction> root,
            UUID accountUuid,
            UUID chargeStation, UUID chargeSite) {
        Join<Transaction, ChargeStation> ch = root.join(AppConstants.CHARGE_STATION_ID, JoinType.INNER);
        Join<ChargeStation, ChargeSite> c = ch.join(AppConstants.CHARGE_SITE, JoinType.INNER);
        Join<ChargeSite, Account> a = c.join(AppConstants.ACCOUNT_ID, JoinType.INNER);
        
        if (!ObjectUtils.isEmpty(accountUuid)) {
            predList.add(cb.equal(a.get(AppConstants.ACCOUNT_ID), accountUuid));
        }
        if (!ObjectUtils.isEmpty(chargeStation)) {
            predList.add(cb.equal(ch.get(AppConstants.CHARGE_STATION_ID), chargeStation));
        }
        if (!ObjectUtils.isEmpty(chargeSite)) {
            predList.add(cb.equal(c.get(AppConstants.SITE_ID), chargeSite));
        }
        Predicate[] predArray = new Predicate[predList.size()];
        predList.toArray(predArray);
        query.where(predArray);
    }
    

	public Uni<List<Transaction>> getTransactionsByVehicleId(String vehicleId) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class);
		Root<Transaction> root = query.from(Transaction.class);
		query.where(cb.equal(root.get("vehicleId"), vehicleId));
		return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
	}

	public Uni<List<Transaction>> getTransactionsByVehicleIdAndTimePeriod(String vehicleId, LocalDateTime startDate,
			LocalDateTime endDate) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Transaction> query = cb.createQuery(Transaction.class);
		Root<Transaction> root = query.from(Transaction.class);
		query.where(cb.between(root.get(AppConstants.STARTTIMEOFCHARGE), startDate, endDate),
				cb.equal(root.get("vehicleId"), vehicleId.toString()));
		return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
	}
    
}
