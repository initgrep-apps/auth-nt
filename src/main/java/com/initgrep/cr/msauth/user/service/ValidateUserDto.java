package com.initgrep.cr.msauth.user.service;

import com.initgrep.cr.msauth.user.dto.UserDto;

public class ValidateUserDto {

	public static String validateUserDtoInfo(UserDto userDto) {
		
		if(userDto.getUserId().equals("") && userDto.getUserId()==null) {
			return "User Id is mandatory";
		}
		
		if(userDto.getName().equals("") && userDto.getName()==null) {
			return "User Name is mandatory";
		}
		
		return "Valid user";
	}

}
