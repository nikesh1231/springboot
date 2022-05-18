package com.evbackend.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import com.evbackend.exception.InvalidTokenRequestException;
import com.evbackend.model.AppConstants;
import com.evbackend.model.ErrorConstants;
import com.evbackend.model.users.User;
import com.evbackend.service.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

@Component
public class JWTUtil {

	/*
	 * @Value("${springbootwebfluxjjwt.jjwt.secret}") private String secret;
	 * 
	 * @Value("${springbootwebfluxjjwt.jjwt.expiration}") private String
	 * expirationTime;
	 */
	
	@Autowired
	private TokenService tokenService;
	
	private Key key;

	private final String secret = "ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave64ByteLength";
	private final String expirationTime = "14400";

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
	}

	public Claims getAllClaimsFromToken(String token) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (Exception e) {
			throw new InvalidTokenRequestException(AppConstants.BEARER, token, ErrorConstants.TOKEN_PARSE_EXCEPTION);
		}
	}

	public String getUsernameFromToken(String token) {
		return getAllClaimsFromToken(token).getSubject();
	}

	public Date getExpirationDateFromToken(String token) {
		return getAllClaimsFromToken(token).getExpiration();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, user.getUserId().toString());
	}
	
	public String generateRefreshToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateRefreshToken(claims, user.getUserId().toString());
	}

	private String doGenerateToken(Map<String, Object> claims, String username) {
		Long expirationTimeLong = Long.parseLong(expirationTime); // in second
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
		String token = Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(createdDate)
				.setExpiration(expirationDate).signWith(key).compact();
		try {
	    Mono<String> tokenRes = tokenService.saveToken(token, true);	
	    tokenRes.subscribe(i -> System.out.println("Token Id = "+i), 
	    	      error -> System.err.println("Error: " + error));
		} catch (Exception e) {
			System.out.println("Saving token failed = "+e.toString());
		}
		return token;
	}
	
	private String doGenerateRefreshToken(Map<String, Object> claims, String username) {
		Long expirationTimeLong = Long.parseLong(expirationTime); // in second
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000);
		String token = Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(createdDate).setId(UUID.randomUUID().toString())
				.setExpiration(expirationDate).signWith(key).compact();
		Mono<String> tokenRes = tokenService.saveToken(token, false);	
	    tokenRes.subscribe(i -> System.out.println("Token Id = "+i), 
	    	      error -> System.err.println("Error: " + error));
		return token;
	}

	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}

	/*
	 * public String doGenerateRefreshToken(Map<String, Object> claims, String
	 * username) { Long expirationTimeLong = Long.parseLong(expirationTime); // in
	 * second final Date createdDate = new Date(); final Date expirationDate = new
	 * Date(createdDate.getTime() + 30000); return
	 * Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(
	 * createdDate) .setExpiration(expirationDate).signWith(key).compact(); }
	 */

}