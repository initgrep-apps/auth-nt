package com.initgrep.cr.msauth.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.initgrep.cr.msauth.user.entity.Address;
import com.initgrep.cr.msauth.user.repository.AddressRepository;

@Service
public class AddressService {
	
	@Autowired
	AddressRepository repository;
	
	public List<Address> getAllAddresses() {
		
		List<Address> addresses = repository.findAll();
			
		return addresses;
	}
}
