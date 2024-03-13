package com.iablonski.websecfluxapp.respositiry;

import com.iablonski.websecfluxapp.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepository extends R2dbcRepository<User, UUID> {
    Mono<User> findUserByUsername(String username);
}
