package com.initgrep.cr.msauth.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.initgrep.cr.msauth.user.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User>  findByEmail(String email);
}
