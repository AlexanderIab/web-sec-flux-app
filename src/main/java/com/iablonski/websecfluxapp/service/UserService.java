package com.iablonski.websecfluxapp.service;

import com.iablonski.websecfluxapp.entity.User;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<User> registerUser(User user);
    Mono<User> getUserById(UUID userId);
    Mono<User> getUserByUsername(String username);
}
