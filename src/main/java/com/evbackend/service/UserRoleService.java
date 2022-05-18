package com.evbackend.service;

import static io.smallrye.mutiny.converters.uni.UniReactorConverters.toMono;

import java.util.List;

import org.springframework.stereotype.Service;

import com.evbackend.model.users.UserRole;
import com.evbackend.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public Mono<List<UserRole>> getAllUserRoles() {
        return this.userRoleRepository.getAllUserRoles().convert().with(toMono());

    }

}
