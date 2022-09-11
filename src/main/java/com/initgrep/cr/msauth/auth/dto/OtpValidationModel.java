package com.initgrep.cr.msauth.auth.dto;

import lombok.Data;

@Data
public class OtpValidationModel {
    private int identifier;
    private int phoneOtp;
    private int emailOtp;
}
