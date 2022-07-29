package com.initgrep.cr.msauth.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.initgrep.cr.msauth.user.dao.User;
import com.initgrep.cr.msauth.user.dto.UserDto;
import com.initgrep.cr.msauth.user.service.AddressService;
import com.initgrep.cr.msauth.user.service.Mapper;
import com.initgrep.cr.msauth.user.service.UserService;
import com.initgrep.cr.msauth.user.service.UserValidator;
import com.initgrep.cr.msauth.user.service.UserValidator.ValidatorResult;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;



	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello from the other side";
	}

	@GetMapping
	@ResponseBody
	public List<UserDto> getUsers() {

	}
	
	@PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
			return userService.save(userDto);
    }
	
}
