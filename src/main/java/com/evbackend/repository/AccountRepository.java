package com.evbackend.repository;


import com.evbackend.model.users.Account;
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

@Tag(name="Account")
@Component
@RequiredArgsConstructor
public class AccountRepository {

    private final Mutiny.SessionFactory sessionFactory;

    public Uni<List<Account>> getAllAccounts() {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Account> query = cb.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<Account> getAccount(UUID accountId) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Account> query = cb.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);
        query.where(cb.equal(root.get("accountId"), accountId));

        return this.sessionFactory.withSession(session -> session.createQuery(query).getSingleResultOrNull())
                .onItem()
                .ifNull()
                .switchTo(
                        Uni.createFrom().item(Account.builder().build())
                );
    }

    public Uni<Account> create(Account account) {
        if (account.getAccountId() == null) {
            return this.sessionFactory.withSession(session ->
                    session.persist(account)
                            .chain(session::flush)
                            .replaceWith(account)
            );
        } else {
            return this.sessionFactory.withSession(session -> session.merge(account).onItem().call(session::flush));
        }
    }
}
