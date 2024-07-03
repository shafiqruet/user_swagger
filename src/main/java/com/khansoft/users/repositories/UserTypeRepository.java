package com.khansoft.users.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khansoft.users.entities.UsersType;

import java.util.Optional;

@Repository
public interface UserTypeRepository extends JpaRepository<UsersType, Integer> {
    Optional<UsersType> findById(Integer id);
    Optional<UsersType> findByCode(String code);
}

