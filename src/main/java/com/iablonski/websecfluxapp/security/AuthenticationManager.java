package com.iablonski.websecfluxapp.security;

import com.iablonski.websecfluxapp.entity.User;
import com.iablonski.websecfluxapp.exception.UnauthorizedException;
import com.iablonski.websecfluxapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserService userService;
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(customPrincipal.getId())
                .filter(User::isActivated)
                .switchIfEmpty(Mono.error(new UnauthorizedException("User disabled")))
                .map(user -> authentication);
    }
}
