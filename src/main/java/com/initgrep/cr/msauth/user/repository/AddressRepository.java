package com.initgrep.cr.msauth.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.initgrep.cr.msauth.user.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String>{



	
}
