package com.initgrep.cr.msauth.user.repository;

import com.initgrep.cr.msauth.user.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, String>{

    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    Optional<AppUser> findByPhoneNumber(String phoneNumber);
    Optional<AppUser>  findByEmail(String email);
}
