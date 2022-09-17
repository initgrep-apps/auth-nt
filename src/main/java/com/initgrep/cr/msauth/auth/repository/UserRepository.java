package com.initgrep.cr.msauth.auth.repository;

import com.initgrep.cr.msauth.auth.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, String> {
    Optional<AppUser> findByIdentifier(String identifier);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByPhoneNumber(String phoneNumber);
    boolean existsByIdentifier(String identifier);
    boolean existsByEmail(String email);


}
