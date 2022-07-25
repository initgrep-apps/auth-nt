package com.initgrep.cr.msauth.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.initgrep.cr.msauth.user.dao.Address;
import com.initgrep.cr.msauth.user.dao.User;
import com.initgrep.cr.msauth.user.dto.UserDto;
import com.initgrep.cr.msauth.user.service.AddressService;
import com.initgrep.cr.msauth.user.service.Mapper;
import com.initgrep.cr.msauth.user.service.UserService;
import com.initgrep.cr.msauth.user.service.ValidateUserDto;

@RestController
@RequestMapping("/user")
public class UserController {

	private UserService userService;
	private AddressService addressService;
	private Mapper mapper;

	public UserController() {
		super();
	}

	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello from the other side";
	}

	@GetMapping
	@ResponseBody
	public List<UserDto> getUsers() {
		List<UserDto> userDto = new ArrayList<UserDto>();
		for (User user : userService.getAllUsers()) {
			userDto.add(mapper.toDto(user));
		}
		return userDto;
	}
	
	@PostMapping
    @ResponseBody
    public String create(@RequestBody UserDto userDto) {
        
        User user = null ;
        
        
        String validate = ValidateUserDto.validateUserDtoInfo(userDto);
        user.setUserId(userDto.getUserId());
        user.setName(userDto.getName());
        user.setEmail("abc@gmail.com");
        user.setPhoneNumber("1234567890");
        userService.save(user);

        return "Success";
    }
	
}
