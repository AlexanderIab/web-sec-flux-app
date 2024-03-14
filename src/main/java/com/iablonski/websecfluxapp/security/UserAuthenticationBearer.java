package com.iablonski.websecfluxapp.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public class UserAuthenticationBearer {
    public static Mono<Authentication> create(JWTHandler.VerificationResult verificationResult) {
        Claims claims = verificationResult.claims;
        String subject = claims.getSubject();
        String role = claims.get("role", String.class);
        String username = claims.get("username", String.class);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        UUID principalId = UUID.fromString(subject);
        CustomPrincipal customPrincipal = new CustomPrincipal(principalId, username);
        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(customPrincipal, null, authorities));
    }
}
