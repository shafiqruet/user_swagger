package com.khansoft.users.services.impl;

import com.khansoft.users.entities.RefreshToken;
import com.khansoft.users.entities.Users;
import com.khansoft.users.exception.TokenCreationException;
import com.khansoft.users.exception.TokenDeletionException;
import com.khansoft.users.repositories.RefreshTokenRepository;
import com.khansoft.users.repositories.UsersRepository;
import com.khansoft.users.services.RefreshTokenService;

import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UsersRepository usersRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository, UsersRepository usersRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.usersRepository = usersRepository;
    }

    /**
     * Creates a new refresh token for the user with the given identifier (email or phone).
     * Deletes existing refresh tokens, generates a new one with a 10-minute expiry,
     * and returns it. Throws exceptions for deletion or creation errors.
     *
     * @param identifier the identifier of the user (email or phone)
     * @return the created refresh token
     * @throws TokenDeletionException    if error deleting existing tokens
     * @throws TokenCreationException    if error creating a new token
     * @throws UsernameNotFoundException if user not found
     */
    @Transactional
    public RefreshToken createRefreshToken(String identifier) throws TokenDeletionException, TokenCreationException {
        Users user = usersRepository.findByEmailOrPhone(identifier);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with identifier: " + identifier);
        }

        try {
            refreshTokenRepository.deleteByUsers(user);
        } catch (Exception e) {
            throw new TokenDeletionException("Failed to delete existing refresh token for user: " + identifier, e);
        }
        RefreshToken refreshToken = RefreshToken.builder()
                .users(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
                .build();
        try {
            return refreshTokenRepository.save(refreshToken);
        } catch (Exception e) {
            throw new TokenCreationException("Failed to create new refresh token for user: " + identifier, e);
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
}