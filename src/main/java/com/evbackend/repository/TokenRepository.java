package com.evbackend.repository;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import com.evbackend.model.Token;

import io.smallrye.mutiny.Uni;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Tag(name = "Token")
@Component
@RequiredArgsConstructor
public class TokenRepository {

	private final Mutiny.SessionFactory sessionFactory;

	public Uni<Token> saveToken(Token token, Boolean isFromSave) {
		return this.sessionFactory
				.withSession(session -> session.persist(token).chain(session::flush).replaceWith(token));
	}

	public Uni<Token> checkIfTokenExists(String token) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder(); // create query
		CriteriaQuery<Token> query = cb.createQuery(Token.class); // set the root
		Root<Token> root = query.from(Token.class);
		query.where(cb.equal(root.get("token"), token));
		return this.sessionFactory.withSession(session -> session.createQuery(query).getSingleResultOrNull()).onItem()
				.ifNull().switchTo(Uni.createFrom().item(Token.builder().token(token).build()));
	}

	public Uni<Token> findToken(String token) {
		CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        // create query
        CriteriaQuery<Token> query = cb.createQuery(Token.class);
        // set the root class
        Root<Token> root = query.from(Token.class);
        query.where(cb.equal(root.get("token"), token));

        return this.sessionFactory.withSession(session -> session.find(Token.class, token))
                .onItem().ifNull().failWith(new Throwable("Token not found!!!!!!!!!!!!"));
	}

	public Uni<Void> deleteToken(String token) {
		Mono<Token> tokenResponse = this.findToken(token).convert().with(toMono());
		tokenResponse.subscribe(u -> System.out.println("Delete Token Id"),
				error -> System.err.println("Error = "+error));
		Token tokenModel = new Token();
		tokenModel.setToken(token);
		return this.sessionFactory.withTransaction((s,t) -> s.find(Token.class, token).chain(s::remove));
	}

}
