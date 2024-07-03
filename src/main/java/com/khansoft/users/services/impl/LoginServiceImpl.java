package com.khansoft.users.services.impl;

import com.khansoft.users.config.security.JwtTokenProvider;
import com.khansoft.users.config.security.TwoFactorAuthService;
import com.khansoft.users.entities.RefreshToken;
import com.khansoft.users.entities.Users;
import com.khansoft.users.entities.request.LoginRequest;
import com.khansoft.users.exception.TokenCreationException;
import com.khansoft.users.exception.TokenDeletionException;
import com.khansoft.users.services.LoginService;
import com.khansoft.users.services.UsersService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsersService usersService;
    private final TwoFactorAuthService twoFactorAuthService;
    private final RefreshTokenServiceImpl refreshTokenService;

    public LoginServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UsersService usersService, TwoFactorAuthService twoFactorAuthService, RefreshTokenServiceImpl refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.usersService = usersService;
        this.twoFactorAuthService = twoFactorAuthService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public ResponseEntity<Map<String, String>> login(LoginRequest loginRequest) {
        Optional<Users> userOptional = Optional.ofNullable(usersService.findByUsernameOrEmailOrPhone(loginRequest.getIdentifier()));
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or phone number"));
        }

        Users user = userOptional.get();

        if (!user.isAccountNonLocked()) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(Map.of("message", "Your account has been locked due to multiple failed login attempts. Please contact support."));
        }

        try {
            Authentication authentication = authenticateUser(loginRequest, user);
            SecurityContext securityContext = createSecurityContext(authentication);
            SecurityContextHolder.setContext(securityContext);

            if (user.isMfaActivated()) {
                boolean is2FAVerified = twoFactorAuthService.verifyCode(user.getMfaSecret(), loginRequest.getTwoFactorCode());
                if (!is2FAVerified) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "Invalid 2FA code"));
                }
            }

            String identifier = loginRequest.getIdentifier();
            RefreshToken refreshToken = generateRefreshToken(identifier);
            Map<String, String> jwtTokenData = generateJwtTokenData(authentication);
            return prepareLoginResponse(jwtTokenData, refreshToken.getToken());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid email or phone number"));
        }
    }

    private Authentication authenticateUser(LoginRequest loginRequest, Users user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), loginRequest.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }

    private SecurityContext createSecurityContext(Authentication authentication) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        return securityContext;
    }

    private RefreshToken generateRefreshToken(String identifier) {
        try {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(identifier);
            logger.info("Refresh token created for user: {}", identifier);
            return refreshToken;
        } catch (TokenDeletionException e) {
            logger.error("Failed to delete existing refresh token for user: {}", identifier, e);
            throw new TokenDeletionException("Failed to delete existing refresh token", e);
        } catch (TokenCreationException e) {
            logger.error("Failed to create new refresh token for user: {}", identifier, e);
            throw new TokenCreationException("Failed to create new refresh token", e);
        }
    }

    private Map<String, String> generateJwtTokenData(Authentication authentication) {
        String identifier = authentication.getName();
        String token = tokenProvider.generateToken(identifier, authentication);
        Map<String, String> jwtTokenData = new HashMap<>();
        jwtTokenData.put("token", token);
        return jwtTokenData;
    }

    private ResponseEntity<Map<String, String>> prepareLoginResponse(Map<String, String> jwtTokenData, String refreshToken) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful!");
        response.put("accessToken", jwtTokenData.get("token"));
        response.put("refreshToken", refreshToken);
        logger.info("Login response prepared");
        return ResponseEntity.ok(response);
    }
}