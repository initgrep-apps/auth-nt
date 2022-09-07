package com.initgrep.cr.msauth.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.initgrep.cr.msauth.auth.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String>{



	
}
