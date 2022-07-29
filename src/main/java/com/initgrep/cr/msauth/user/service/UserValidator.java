package com.initgrep.cr.msauth.user.service;

import com.initgrep.cr.msauth.user.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class UserValidator {

	public static ValidatorResult validateUserDtoInfo(UserDto userDto) {
		
		if(userDto.getUserId().equals("") && userDto.getUserId()==null) {
			return new ValidatorResult(false, "username is required");
		}
		
		if(userDto.getName().equals("") && userDto.getName()==null) {
			 return new ValidatorResult(false, "name is required");
		}
		
		return new ValidatorResult(true, "valida user");
	}


	
	@Data
	@AllArgsConstructor
	public static class ValidatorResult{
		private boolean isValid;
		private String message;
	}

}
