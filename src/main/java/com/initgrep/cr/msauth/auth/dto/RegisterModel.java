package com.initgrep.cr.msauth.auth.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import static com.initgrep.cr.msauth.auth.constants.ValidationConstants.*;

@Data
public class RegisterModel {

    @Email(regexp = EMAIL_PATTERN, message = EMAIL_INVALID)
    private String email;
    private String fullName;

    @Pattern(regexp = PHONE_PATTERN, message = PHONE_INVALID)
    private String phoneNumber;

    private String password;
    private OtpInfoModel otpInfo;
}
