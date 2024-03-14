package com.iablonski.websecfluxapp.service.impl;

import com.iablonski.websecfluxapp.entity.User;
import com.iablonski.websecfluxapp.entity.UserRole;
import com.iablonski.websecfluxapp.respositiry.UserRepository;
import com.iablonski.websecfluxapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<User> registerUser(User user) {
        return userRepository.save(user.toBuilder()
                .password(passwordEncoder.encode(user.getPassword()))
                .role(UserRole.USER)
                .activated(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()).doOnSuccess(u ->
                log.info("IN registerUser - user: {} created", u)
        );
    }

    @Override
    public Mono<User> getUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Mono<User> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
