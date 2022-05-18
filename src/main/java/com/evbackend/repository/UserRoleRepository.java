package com.evbackend.repository;

import com.evbackend.model.users.UserRole;
import io.smallrye.mutiny.Uni;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Tag(name="UserRoles")
@Component
@RequiredArgsConstructor
public class UserRoleRepository {

    private final Mutiny.SessionFactory sessionFactory;

    public Uni<List<UserRole>> getAllUserRoles() {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<UserRole> query = cb.createQuery(UserRole.class);
        Root<UserRole> root = query.from(UserRole.class);
        return this.sessionFactory.withSession(session -> session.createQuery(query).getResultList());
    }

}
