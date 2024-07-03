package com.khansoft.users.repositories;

import com.khansoft.users.entities.Users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);
    Users findByEmailAndClientId(String email, Integer clientId);
    Users findByPhoneAndClientId(String phone, Integer clientId);

    Users findByPhone(String phone);

    Optional <Users> findByReferenceNo(String referenceNo);
    Optional <Users> findUserByReferenceNo(String ref_no);

    @Query("SELECT u FROM Users u WHERE u.email = :identifier OR u.phone = :identifier")
    Users findByEmailOrPhone(@Param("identifier") String identifier);

}
