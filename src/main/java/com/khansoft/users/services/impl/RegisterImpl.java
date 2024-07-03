package com.khansoft.users.services.impl;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import java.util.*;

import com.khansoft.users.entities.*;
import com.khansoft.users.entities.request.RoleRequest;
import com.khansoft.users.entities.request.UserRegister;
import com.khansoft.users.exception.DuplicateEntryException;
import com.khansoft.users.exception.InvalidDataTypeException;
import com.khansoft.users.exception.MethodArgumentException;
import com.khansoft.users.repositories.RoleRepository;
import com.khansoft.users.repositories.UserTypeRepository;
import com.khansoft.users.repositories.UsersRepository;
import com.khansoft.users.services.Register;
import com.khansoft.users.util.ClientIdExtractor;
import com.khansoft.users.util.UsersSystem;
import com.khansoft.users.util.KSCrypto;
import com.khansoft.users.util.Constant;

@Service
public class RegisterImpl implements Register {

    private final UsersRepository usersRepository;
    private final UserTypeRepository userTypeRepository;
    private final RoleRepository roleRepository;
    private UsersSystem usersSystem;
    private final ModelMapper modelMapper;
    BCryptPasswordEncoder passwordEncoder;
    
    private final KSCrypto ksCrypto;


    private static final Logger logger = LogManager.getLogger(RegisterImpl.class);

    public RegisterImpl(UsersRepository usersRepository, UserTypeRepository userTypeRepository,
            RoleRepository roleRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder, KSCrypto ksCrypto, UsersSystem usersSystem) {
        this.usersRepository = usersRepository;
        this.userTypeRepository = userTypeRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.ksCrypto = ksCrypto;
        this.usersSystem = usersSystem;
    }

    @Transactional
    @Override
    public Users createUser(UserRegister request, HttpHeaders headers) {
        String email = request.getEmail();
        String phone = request.getPhone();
        Integer userTypeCode = request.getUserType();
        Integer termAndConditions = request.getUsersTermsAndConditions();

        // validate country and phone number

        String countryCode = request.getCountryCode();
        
        String phoneValidation = ksCrypto.validatePhoneNumber(phone, countryCode);

        if (!"valid".equals(phoneValidation)) {
            logger.error(phoneValidation);
            throw new MethodArgumentException(phoneValidation);
        }
        
        // Fetch the UsersType by ID (if the request is sending the ID)
        UsersType usersType = userTypeRepository.findById(userTypeCode)
                .orElseThrow(() -> new InvalidDataTypeException("Invalid userType ID: " + userTypeCode));

   
        // Continue with the usersType object for further processing

        String getclientId = headers.getFirst("decrypted-client-id");
        //System.err.println("Dycrypted id" + headers.getFirst("decrypted-client-id"));
        // Convert the clientId from String to Integer
        Integer clientId = Integer.parseInt(getclientId);

        Users existingUsersByEmail = usersRepository.findByEmailAndClientId(email, clientId);
        //System.out.println(existingUsersByEmail + "existingUsers");
        if (existingUsersByEmail != null) {
            logger.error("Email '{}' already exists", email);
            throw new DuplicateEntryException("Email " + email + " already exists");
        }

        Users existingUsersByPhone = usersRepository.findByPhoneAndClientId(phone, clientId);
        System.out.println(existingUsersByPhone + "existingUsers");
        if (existingUsersByPhone != null) {
            logger.error("Client with phone '{}' already exists", phone);
            throw new DuplicateEntryException("Phone " + phone + " already exists");
        }

        Users users = modelMapper.map(request, Users.class);

        users.setUserType(usersType);

        users.setUsersTermsAndConditions(new UsersTermsAndConditions(termAndConditions));

        Date created_at = new Date();
        users.setCreatedAt(created_at);

        String ref_no = usersSystem.generateUniqueReferralNumber(Constant.Register.REF_NO_LENGTH);

        users.setReferenceNo(ref_no);

        users.setPassword(passwordEncoder.encode(request.getPassword()));

        String token = ksCrypto.generate6DigitToken();

        users.setToken(token);

        users.setCountryId(1);

        String sponsor = request.getReferenceNo();
        if (sponsor != null && !sponsor.isEmpty()) {
            Optional<Users> sponsorReferNo = usersRepository.findUserByReferenceNo(sponsor);
            if (sponsorReferNo.isPresent()) {
                users.setSponsorUsername(sponsorReferNo.get().getReferenceNo());
                users.setSponsorId(sponsorReferNo.get());
            }
        }

        // TODO: vat id will be added later
        users.setClientId(clientId);
        // Fetch or create roles

        UsersPreferences usersPreferences = new UsersPreferences();
        usersPreferences.setUser(users);
        usersPreferences.setClientId(clientId);
        usersPreferences.setCreatedAt(created_at);
        usersPreferences.setUpdatedAt(created_at);
        usersPreferences.setLangaugeId(1);
        usersPreferences.setMailTemplateId(10);
        users.setUsersPreferences(usersPreferences);

        // Fetch or create roles and set additional fields like createdAt
        createAndSetRole(request, users, clientId);

        // TODO: need to check client service access then save binary, unilevel service
        // etc

        Users savedUser;
        try {
            savedUser = usersRepository.save(users);
        } catch (Exception e) {
            logger.error("Error saving user:", e);
            throw new DuplicateEntryException("Failed to save user");
        }

        return savedUser;
    }

    // Method to create a new role if it does not exist and set additional fields
    private void createAndSetRole(UserRegister userRegister, Users users, Integer clientId) {
        Set<Role> userRoles = new HashSet<>();
        List<RoleRequest> roleRequests = userRegister.getRoles();
        if (roleRequests != null) {
            for (RoleRequest roleRequest : roleRequests) {
                Role role = roleRepository.getByNameAndClientId(roleRequest.getName(), clientId).stream().findFirst()
                        .orElse(null);

                if (role == null) {
                    role = new Role();
                    role.setName(roleRequest.getName());
                    role.setActiveStatus(1);
                    role.setCreatedAt(new Date());
                    role.setUpdateAt(new Date());
                    role.setClientId(clientId);
                    role = roleRepository.save(role);
                }

                userRoles.add(role);
            }
        }
        users.setRoles(userRoles);
    }

}
