package com.iablonski.websecfluxapp.security;

import com.iablonski.websecfluxapp.entity.User;
import com.iablonski.websecfluxapp.exception.AuthException;
import com.iablonski.websecfluxapp.service.UserService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSec;
    @Value("${jwt.issuer}")
    private String issuer;
    private static final String ALGORITHM = "HmacSHA256";

    private TokenDetails generateToken(User user) {
        Map<String, Object> claims = new HashMap<>() {{
            put("role", user.getRole());
            put("username", user.getUsername());
        }};

        return generateToken(claims, user.getId().toString());
    }

    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        long expirationTimeInMillis = expirationInSec * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);

        return generateToken(expirationDate, claims, subject);
    }

    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        String token = Jwts.builder()
                .claims(claims)
                .issuer(issuer)
                .subject(subject)
                .issuedAt(createdDate)
                .id(UUID.randomUUID().toString())
                .expiration(expirationDate)
                .signWith(generateSecretKey(secret))
                .compact();

        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();
    }

    private SecretKey generateSecretKey(String secret) {
        byte[] decodedKey = Base64.getEncoder().encode(secret.getBytes());
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }


    public Mono<TokenDetails> authenticate(String username, String password) {
        return userService.getUserByUsername(username)
                .flatMap(user -> {
                    if (!user.isActivated()) {
                        return Mono.error(new AuthException("Account disabled", "IABLONSKI_USER_ACCOUNT_DISABLED") {
                        });
                    }
                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new AuthException("Invalid password", "IABLONSKI_INVALID_PASSWORD"));
                    }
                    return Mono.just(generateToken(user).toBuilder()
                            .userId(user.getId())
                            .build());
                })
                .switchIfEmpty(Mono.error(new AuthException("Invalid username", "IABLONSKI_INVALID_USERNAME")));
    }
}