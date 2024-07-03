package com.khansoft.users;

import com.khansoft.users.entities.Authority;
import com.khansoft.users.entities.Role;
import com.khansoft.users.entities.Users;
import com.khansoft.users.enumeration.Roles;
import com.khansoft.users.repositories.AuthorityRepository;
import com.khansoft.users.repositories.RoleRepository;
import com.khansoft.users.repositories.UsersRepository;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

/**
 * Initial user setup for the application.
 * Inserts data only once during the first-time application startup.
 */
@Component
public class InitialUsersSetup {

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UsersRepository usersRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    @EventListener
    public void onApplicationEvent(ApplicationReadyEvent event) {
        logger.info("Application ready event triggered");

        // Check if the initial setup has already been done
        Users storedAdminUser = usersRepository.findByEmail("admin@gmail.com");
        if (storedAdminUser != null) {
            logger.info("Admin user already exists, skipping initial setup");
            return;
        }

        logger.info("Performing initial user setup");

        Authority readAuthority = createAuthority("READ");
        Authority writeAuthority = createAuthority("WRITE");
        Authority deleteAuthority = createAuthority("DELETE");

        createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority));
        Role roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

        Users adminUser = new Users();
        adminUser.setFirstName("Rayhan");
        adminUser.setLastName("Shuvo");
        adminUser.setEmail("admin@gmail.com");
        adminUser.setPhone("01730464658");
        adminUser.setCountryId(1);
        adminUser.setPassword(bCryptPasswordEncoder.encode("12345678"));
        adminUser.setRoles(Arrays.asList(roleAdmin));

        usersRepository.save(adminUser);
        logger.info("Admin user created with email: {}", adminUser.getEmail());
    }

    @Transactional
    protected Authority createAuthority(String name) {
        Authority authority = authorityRepository.findByName(name);
        if (authority == null) {
            authority = new Authority(name);
            authorityRepository.save(authority);
            logger.info("Created authority: {}", name);
        }
        return authority;
    }

    @Transactional
    protected Role createRole(String name, Collection<Authority> authorities) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name, authorities);
            roleRepository.save(role);
            logger.info("Created role: {}", name);
        }
        return role;
    }
}