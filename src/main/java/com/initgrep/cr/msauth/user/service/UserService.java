package com.initgrep.cr.msauth.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.initgrep.cr.msauth.user.dao.User;
import com.initgrep.cr.msauth.user.dto.UserDto;
import com.initgrep.cr.msauth.user.repository.UserRepository;
import com.initgrep.cr.msauth.user.service.UserValidator.ValidatorResult;

@Service
public class UserService {

	@Autowired
	UserRepository userReposity;

	public List<UserDto> getUsers() {
		return userReposity.findAll().

	}

	public UserDto save(UserDto userDto) {

		ValidatorResult validatorResult = UserValidator.validateUserDtoInfo(userDto);
		if (!validatorResult.isValid()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validatorResult.getMessage());
		}

		User user = new User();
		user.setUserId(userDto.getUserId());
		user.setName(userDto.getName());
		user.setEmail("abc@gmail.com");
		user.setPhoneNumber("1234567890");
		User savedUser = userReposity.save(user);

		return new UserDto(savedUser.getUserId(), savedUser.getName(), null);
	}
}
