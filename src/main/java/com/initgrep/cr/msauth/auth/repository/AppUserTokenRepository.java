package com.initgrep.cr.msauth.auth.repository;

import com.initgrep.cr.msauth.auth.entity.AppUserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserTokenRepository extends JpaRepository<AppUserToken, Long> {

    Optional<AppUserToken> findByJwtId(String jwtId);
}