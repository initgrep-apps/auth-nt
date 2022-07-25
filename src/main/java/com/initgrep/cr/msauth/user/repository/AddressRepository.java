package com.initgrep.cr.msauth.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.initgrep.cr.msauth.user.dao.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, String>{



	
}
