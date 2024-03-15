package com.iablonski.websecfluxapp.security;

import com.iablonski.websecfluxapp.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

public class JWTHandler {

    private final String secret;
    private static final String ALGORITHM = "HmacSHA256";

    public JWTHandler(String secret) {
        this.secret = secret;
    }

    public Mono<VerificationResult> check(String accessToken){

        return Mono.just(verify(accessToken))
                .onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
    }

    private VerificationResult verify(String token){
        Claims claims = getClaimsFromToken(token);
        final Date expirationDate = claims.getExpiration();

        if(expirationDate.before(new Date())){
            throw new RuntimeException("Token expired");
        }

        return new VerificationResult(claims, token);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(generateSecretKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey generateSecretKey(String secret) {
        byte[] decodedKey = Base64.getEncoder().encode(secret.getBytes());
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }

    public static class VerificationResult{
        public Claims claims;
        public String token;

        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }
}
