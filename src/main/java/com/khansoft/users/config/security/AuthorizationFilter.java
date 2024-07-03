package com.khansoft.users.config.security;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.khansoft.users.services.impl.UsersServiceImpl;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.Collection;

/**
 * @author rayhan
 */
@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final Environment environment;
    private final UsersServiceImpl usersServiceImpl;
    @Value("${token.secret}")
    private String secretKey;

    public AuthorizationFilter(AuthenticationManager authenticationManager, Environment environment, UsersServiceImpl usersServiceImpl, String secretKey) {
        super(authenticationManager);
        this.environment = environment;
        this.usersServiceImpl = usersServiceImpl;
        this.secretKey = secretKey;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);

        if (token != null && validateToken(token)) {
            JwtClaimsParser jwtClaimsParser = new JwtClaimsParser(token, secretKey);
            String username = jwtClaimsParser.getJwtSubject();

            UserDetails userDetails = usersServiceImpl.loadUserByUsername(username);
            Collection<? extends GrantedAuthority> authorities = jwtClaimsParser.getUserAuthorities();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            parseJwt(token);
            return true; // Add your token validation logic here if needed
        } catch (Exception e) {
            return false;
        }
    }

    private Jwt<?, ?> parseJwt(String token) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        SecretKey key = Keys.hmacShaKeyFor(secretKeyBytes);
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        return jwtParser.parse(token);
    }
}

