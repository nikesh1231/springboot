package com.evbackend.repository;


import java.util.List;
import java.util.UUID;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.query.criteria.internal.CriteriaUpdateImpl;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import com.evbackend.model.AppConstants;
import com.evbackend.model.users.User;

import io.smallrye.mutiny.Uni;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="Users")
@Component
@RequiredArgsConstructor
public class UserRepository{

    private final Mutiny.SessionFactory sessionFactory;

    public Uni<List<User>> getAllUsers() {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<List<User>> getAllActiveUsers() {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.where(cb.equal(root.get(AppConstants.ACTIVE_USER), Boolean.TRUE));
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

    public Uni<User> adminCreate(User user) {
        if (user.getUserId() == null) {
            return this.sessionFactory.withSession(session ->
                    session.persist(user)
                            .chain(session::flush)
                            .replaceWith(user)
            );
        } else {
            return this.sessionFactory.withSession(session -> session.merge(user).onItem().call(session::flush));
        }
    }

    public Uni<User> getUserByUserId(UUID userId) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        // create query
        CriteriaQuery<User> query = cb.createQuery(User.class);
        // set the root class
        Root<User> root = query.from(User.class);
        query.where(cb.equal(root.get("firstName"), "Sally"));

        return this.sessionFactory.withSession(session -> session.createQuery(query).getSingleResultOrNull())
                .onItem()
                .ifNull()
                .switchTo(
                        Uni.createFrom().item(User.builder().build())
                );
    }

	public Uni<User> getUserByUserName(String username) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        // create query
        CriteriaQuery<User> query = cb.createQuery(User.class);
        // set the root class
        Root<User> root = query.from(User.class);
        query.where(cb.equal(root.get("userName"), username));

        return this.sessionFactory.withSession(session -> session.createQuery(query).getSingleResultOrNull())
                        .onItem()
                        .ifNull()
                        .switchTo(
                        Uni.createFrom().item(User.builder().userName(username).build())
                );
	}
    public Uni<User> inactivateUserByUserId(String userId){
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaUpdateImpl<User> update = (CriteriaUpdateImpl<User>) cb.createCriteriaUpdate(User.class);
        Root<User> root = update.from(User.class);
        update.set("activeUser", false);
        update.where(cb.equal(root.get("userId"), userId));
        return this.sessionFactory.withSession(session -> session.createQuery(update).getSingleResultOrNull())
                .onItem()
                .ifNull()
                .switchTo(
                        Uni.createFrom().item(User.builder().build())
                );
    }

	public Uni<User> getUserById(UUID userId) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
		// create query
		CriteriaQuery<User> query = cb.createQuery(User.class);
		// set the root class
		Root<User> root = query.from(User.class);
		query.where(cb.equal(root.get("userId"), userId));

		return this.sessionFactory.withSession(session -> session.createQuery(query).getSingleResultOrNull()).onItem()
				.ifNull().switchTo(Uni.createFrom().item(User.builder().build()));
	}

}
