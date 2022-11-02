package com.initgrep.cr.msauth.auth.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import static com.initgrep.cr.msauth.auth.constants.ValidationConstants.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Email(regexp = EMAIL_PATTERN, message = EMAIL_INVALID)
    private String email;
    private String fullName;

    @Pattern(regexp = PHONE_PATTERN, message = PHONE_INVALID)
    private String phoneNumber;

    private String password;
    private OtpInfoModel otpInfo;
}
