package com.initgrep.cr.msauth.auth.dto;

import lombok.Data;

@Data
public class RegisterModel {
    private String email;
    private String fullName;
    private String phoneNumber;
    private String password;
    private SourceModel source;
    private OtpInfoModel otpInfo;
}
