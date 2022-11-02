package com.initgrep.cr.msauth.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpInfoModel {
    private String identifier;
    private int phoneOtp;
    private int emailOtp;
}
