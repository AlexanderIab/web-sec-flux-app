package com.iablonski.websecfluxapp.controller;

import com.iablonski.websecfluxapp.dto.AuthRequestDTO;
import com.iablonski.websecfluxapp.dto.AuthResponseDTO;
import com.iablonski.websecfluxapp.dto.UserDTO;
import com.iablonski.websecfluxapp.entity.User;
import com.iablonski.websecfluxapp.mapper.UserMapper;
import com.iablonski.websecfluxapp.security.CustomPrincipal;
import com.iablonski.websecfluxapp.security.SecurityService;
import com.iablonski.websecfluxapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public Mono<UserDTO> register(@RequestBody UserDTO userDTO) {
        User user = userMapper.toUser(userDTO);
        return userService.registerUser(user).map(userMapper::toUserDTO);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {
        return securityService.authenticate(authRequestDTO.username(), authRequestDTO.password())
                .flatMap(tokenDetails -> Mono.just(AuthResponseDTO.builder()
                        .userId(tokenDetails.getUserId())
                        .token(tokenDetails.getToken())
                        .issuedAt(tokenDetails.getIssuedAt())
                        .expiresAt(tokenDetails.getExpiresAt())
                        .build()));
    }

    @GetMapping("/info")
    public Mono<UserDTO> getUserInfo(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(customPrincipal.getId()).map(userMapper::toUserDTO);
    }
}
