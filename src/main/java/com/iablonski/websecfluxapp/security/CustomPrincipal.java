package com.iablonski.websecfluxapp.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomPrincipal implements Principal {
    private UUID id;
    private String name;
}
