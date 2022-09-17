package com.initgrep.cr.msauth.auth.dto;

import lombok.Data;

@Data
public class LoginModel {
    private String email;
    private String phoneNumber;
    private String password;
    private SourceModel source;
    private OtpInfoModel otpInfo;
}
