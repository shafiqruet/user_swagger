/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khansoft.users.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.khansoft.users.services.UsersService;
import com.khansoft.users.services.impl.UsersServiceImpl;

/**
 * @author rayhan
 */

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class WebSecurity {
    private final Environment environment;
    private final UsersService usersService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsersServiceImpl usersServiceImpl;
    @Value("${token.secret}")
    private String secretKey;

    public WebSecurity(UsersService usersService, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env, UsersServiceImpl usersServiceImpl) {
        this.usersService = usersService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = env;
        this.usersServiceImpl = usersServiceImpl;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        //http.addFilterBefore(new IpAddressFilter(environment), SecurityContextHolderFilter.class);

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(usersService).passwordEncoder(bCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
         http.csrf().disable()
         
         .authorizeRequests()
         .requestMatchers(new AntPathRequestMatcher("/v1/api/users/**")).permitAll()
         .requestMatchers(new AntPathRequestMatcher("/users/swagger-ui/**")).permitAll()
         .requestMatchers(new AntPathRequestMatcher("/check", "GET")).permitAll();
         http.addFilter(new AuthorizationFilter(authenticationManager, environment, usersServiceImpl, secretKey))
                .authenticationManager(authenticationManager)
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();

    }

}
