package com.evbackend.service;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evbackend.model.Token;
import com.evbackend.repository.TokenRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TokenService {

	@Autowired
	private TokenRepository tokenRepository;

	public Mono<String> saveToken(String token, Boolean isFromSave) {
		Mono<Token> tokenCreated = this.tokenRepository.saveToken(Token.builder().token(token).build(), isFromSave)
				.convert().with(toMono());
		return tokenCreated.map(u -> u.getToken().toString());
	}

	public Mono<Token> checkIfTokenExists(String token) {
		return this.tokenRepository.checkIfTokenExists(token).convert().with(toMono());
	}

	public Mono<Token> findToken(String token) {
		return this.tokenRepository.findToken(token).convert().with(toMono());
	}

	public Mono<Void> deleteToken(String token) {
		return this.tokenRepository.deleteToken(token).convert().with(toMono());
	}

}
