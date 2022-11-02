package com.initgrep.cr.msauth.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import static com.initgrep.cr.msauth.auth.constants.ValidationConstants.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @Email(regexp = EMAIL_PATTERN)
    private String email;

    @Pattern(regexp = PHONE_PATTERN, message = PHONE_INVALID)
    private String phoneNumber;

    private String password;

    private OtpInfoModel otpInfo;
}
