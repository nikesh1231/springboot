package com.evbackend.repository;

import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import com.evbackend.model.address.Address;
import io.smallrye.mutiny.Uni;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name="Address")
@Component
@RequiredArgsConstructor
public class AddressRepository {

    private final Mutiny.SessionFactory sessionFactory;

    public Uni<Address> create(Address address) {
        return this.sessionFactory.withSession(session ->
                session.persist(address)
                        .chain(session::flush)
                        .replaceWith(address)
        );
    }


}
