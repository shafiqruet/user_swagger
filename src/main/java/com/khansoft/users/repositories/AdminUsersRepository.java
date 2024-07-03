/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.khansoft.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.khansoft.users.entities.Users;

import java.util.Optional;

/**
 * @author hafiz.khan
 */
@Repository
public interface AdminUsersRepository extends JpaRepository<Users, Long> {

    //  Optional<Users> findByUserName(String username);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhone(String phone);

}
