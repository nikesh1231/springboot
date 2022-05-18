package com.evbackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
	
	 private AuthenticationManager authenticationManager;
    
    private SecurityContextRepository securityContextRepository;

	// TODO: Add CORS
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http
        .authorizeExchange()
		.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .pathMatchers("/", "/manufacturer/**", "/static/**","/manifest.json", "/favicon.ico",  "/api/login", "/api/users/signup", "/health", "/webjars/**", "/v3/api-docs/**").permitAll()
        .anyExchange().authenticated()
        .and().cors().disable()
        .exceptionHandling()
        .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
            swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        })).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
            swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        }))
//        .and().logout()
//        .logoutUrl("/api/auth").requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/api/auth"))
        .and()
        .csrf().disable()
        .authenticationManager(authenticationManager)
        .securityContextRepository(securityContextRepository)

        .build();
	}
	
	/*
	 * @Bean public SecurityWebFilterChain http(ServerHttpSecurity http) {
	 * DelegatingServerLogoutHandler logoutHandler = new
	 * DelegatingServerLogoutHandler(new WebSessionServerLogoutHandler(), new
	 * SecurityContextServerLogoutHandler()); http.authorizeExchange((exchange) ->
	 * exchange.anyExchange().authenticated()).logout((logout) ->
	 * logout.logoutHandler(logoutHandler)); return http.build(); }
	 */

}