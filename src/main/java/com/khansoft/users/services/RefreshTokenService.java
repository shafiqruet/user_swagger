package com.khansoft.users.services;


import com.khansoft.users.entities.RefreshToken;
import com.khansoft.users.exception.TokenCreationException;
import com.khansoft.users.exception.TokenDeletionException;

import java.util.Optional;

/**
 * @author Rayhan
 */

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String email) throws TokenDeletionException, TokenCreationException;

    Optional<RefreshToken> findByToken(String token);

    RefreshToken verifyExpiration(RefreshToken token);
}
