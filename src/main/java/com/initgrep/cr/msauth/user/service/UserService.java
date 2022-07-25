package com.initgrep.cr.msauth.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.initgrep.cr.msauth.user.dao.User;
import com.initgrep.cr.msauth.user.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userReposity;
	
	public List<User> getAllUsers() {
	
	List<User> users = userReposity.findAll();	
		
	return users;	
		
	}
	
	public String save(User user) {
		
		userReposity.save(user);	
			
		return "Success";	
			
		}
}
