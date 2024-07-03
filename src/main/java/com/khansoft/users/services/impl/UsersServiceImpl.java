package com.khansoft.users.services.impl;

import com.khansoft.users.config.security.TwoFactorAuthService;
import com.khansoft.users.entities.Role;
import com.khansoft.users.entities.Users;
import com.khansoft.users.entities.request.AdminUserRequest;
import com.khansoft.users.entities.request.RoleRequest;
import com.khansoft.users.entities.request.UserRegister;
import com.khansoft.users.entities.response.UserResponse;
import com.khansoft.users.repositories.RoleRepository;
import com.khansoft.users.repositories.UsersRepository;
import com.khansoft.users.services.UsersService;
import com.khansoft.users.util.UserPrinciple;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Service
@Slf4j
public class UsersServiceImpl implements UsersService {
    private static final Logger LOGGER = LogManager.getLogger(UsersServiceImpl.class);
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment environment;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final TwoFactorAuthService twoFactorAuthService;

    public UsersServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder passwordEncoder, Environment environment, ModelMapper modelMapper, RoleRepository roleRepository, TwoFactorAuthService twoFactorAuthService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.twoFactorAuthService = twoFactorAuthService;
    }

    @Override
    public UserResponse createUser(UserRegister userDetails) {
        Users user = modelMapper.map(userDetails, Users.class);
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        LOGGER.info("Saving user: {}", user);
        usersRepository.save(user);
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmailOrPhone(identifier);
        if (user == null) {
            throw new UsernameNotFoundException(identifier);
        }
        return UserPrinciple.create(user);
    }

    @Override
    public UserResponse getUserDetailsByEmail(String email) {
        Users user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse getUserById(Long id) {
        Optional<Users> user = usersRepository.findById(id);
        return user.map(value -> modelMapper.map(value, UserResponse.class))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserResponse getUserDetailsByUserName(String identifier) {
        Users user = usersRepository.findByEmailOrPhone(identifier);
        if (user == null) {
            throw new UsernameNotFoundException(identifier);
        }
        return modelMapper.map(user, UserResponse.class);
    }

    
    @Override
    public Users findByUsername(String identifier) {
        return usersRepository.findByEmailOrPhone(identifier);
    }

    public String generate2FASecretKey() {
        return twoFactorAuthService.generateSecretKey();
    }

    public void store2FASecretKey(AdminUserRequest adminUserRequest, String secretKey) {
        Users user = usersRepository.findByEmailOrPhone(adminUserRequest.getEmail());
        if (user != null) {
            user.setMfaSecret(secretKey);
            user.setMfaActivated(adminUserRequest.isMfaActivated());
            LOGGER.info("Saving user: {}", user);
            usersRepository.save(user);
        }
    }

    public String get2FASecretKey(String identifier) {
        Users user = usersRepository.findByEmailOrPhone(identifier);
        return user != null ? user.getMfaSecret() : null;
    }

    @Override
    public Users findByUsernameOrEmailOrPhone(String identifier) {
        return usersRepository.findByEmailOrPhone(identifier);
    }

    @Override
    public void increaseFailedAttempts(Users user) {
        int newFailedAttempts = user.getFailedAttempts() + 1;
        user.setFailedAttempts(newFailedAttempts);
        if (newFailedAttempts >= 5) {
            lockAccount(user);
        }
        usersRepository.save(user);
    }

    @Override
    public void resetFailedAttempts(Users user) {
        user.setFailedAttempts(0);
        usersRepository.save(user);
    }

    @Override
    public void lockAccount(Users user) {
        user.setAccountNonLocked(false);
        user.setLockedAt(new Date());
        usersRepository.save(user);
    }
}