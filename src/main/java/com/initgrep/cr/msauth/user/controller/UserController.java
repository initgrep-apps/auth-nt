package com.initgrep.cr.msauth.user.controller;

import com.initgrep.cr.msauth.user.dto.UserDto;
import com.initgrep.cr.msauth.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;

	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello from the other side";
	}

	@GetMapping
	public List<UserDto> getUsers() {
			return userService.getUsers();
	}
	
	@PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
			return userService.save(userDto);
    }
	
}
