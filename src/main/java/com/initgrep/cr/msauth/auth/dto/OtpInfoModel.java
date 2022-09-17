package com.initgrep.cr.msauth.auth.dto;

import lombok.Data;

@Data
public class OtpInfoModel {
    private String identifier;
    private int phoneOtp;
    private int emailOtp;
}
