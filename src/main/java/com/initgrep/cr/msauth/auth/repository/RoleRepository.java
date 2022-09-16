package com.initgrep.cr.msauth.auth.repository;

import com.initgrep.cr.msauth.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
