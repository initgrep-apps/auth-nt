package com.initgrep.cr.msauth.user.service;

import com.initgrep.cr.msauth.user.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

import static java.util.Objects.*;

public class UserValidator {

    public static ValidatorResult validateUserDtoInfo(UserDto userDto) {

        if (isNull(userDto.getEmail()) || userDto.getEmail().isBlank()) {
            return new ValidatorResult(false, "email is required");
        }

        if (isNull(userDto.getName()) || userDto.getName().isBlank()) {
            return new ValidatorResult(false, "name is required");
        }

        if (isNull(userDto.getPhoneNumber()) || userDto.getPhoneNumber().isBlank()) {
            return new ValidatorResult(false, "phone number is required");
        }

        return new ValidatorResult(true, "valid user");
    }

    private UserValidator() {
    }

    @Data
    @AllArgsConstructor
    public static class ValidatorResult {
        private boolean isValid;
        private String message;
    }

}
