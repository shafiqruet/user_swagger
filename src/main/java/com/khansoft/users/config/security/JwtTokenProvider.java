package com.khansoft.users.config.security;

import com.khansoft.users.entities.Authority;
import com.khansoft.users.entities.Role;
import com.khansoft.users.entities.response.UserResponse;
import com.khansoft.users.services.impl.UsersServiceImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final UsersServiceImpl usersServiceImpl;
    private final String tokenSecret;
    private final Long expirationInMillis; // Use Long for expiration time
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Autowired
    public JwtTokenProvider(UsersServiceImpl usersServiceImpl, @Value("${token.secret}") String secret, @Value("${token.expiration_time}") Long expirationInMillis) {
        this.usersServiceImpl = usersServiceImpl;
        this.tokenSecret = secret;
        this.expirationInMillis = expirationInMillis;
    }

    public String generateToken(String userName, Authentication authentication) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName, authentication);
    }

    private String createToken(Map<String, Object> claims, String userName, Authentication auth) {
        UserResponse userDetails = usersServiceImpl.getUserDetailsByUserName(userName);
        Instant now = Instant.now();
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, "HmacSHA512");

        Map<String, List<String>> rolesWithAuthorities = new HashMap<>();
        for (Role role : userDetails.getRoles()) {
            List<String> authorities = role.getAuthorities().stream()
                    .map(Authority::getName)
                    .collect(Collectors.toList());
            rolesWithAuthorities.put(role.getName(), authorities);
        }

        claims.put("rolesWithAuthorities", rolesWithAuthorities);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUserName())
                .setExpiration(Date.from(now.plusMillis(expirationInMillis)))
                .setIssuedAt(Date.from(now))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
}
