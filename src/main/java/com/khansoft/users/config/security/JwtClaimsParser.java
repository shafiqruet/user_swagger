package com.khansoft.users.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtClaimsParser {

    private Jwt<?, ?> jwtObject;

    public JwtClaimsParser(String jwt, String secretToken) {
        this.jwtObject = parseJwt(jwt, secretToken);
    }

    private Jwt<?, ?> parseJwt(String jwtString, String secretToken) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(secretToken.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();

        return jwtParser.parse(jwtString);
    }

    public Collection<? extends GrantedAuthority> getUserAuthorities() {
        Map<String, List<String>> rolesWithAuthorities = ((Claims) jwtObject.getBody()).get("rolesWithAuthorities", Map.class);

        if (rolesWithAuthorities == null) {
            return Collections.emptyList();
        }

        return rolesWithAuthorities.entrySet().stream()
                .flatMap(entry -> {
                    String role = entry.getKey(); // No need to add ROLE_ prefix since it's already included
                    List<String> authorities = entry.getValue();
                    return Stream.concat(Stream.of(role), authorities.stream())
                            .map(SimpleGrantedAuthority::new);
                })
                .collect(Collectors.toList());
    }

    public String getJwtSubject() {
        return ((Claims) jwtObject.getBody()).getSubject();
    }
}