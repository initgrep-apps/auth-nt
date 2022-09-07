package com.initgrep.cr.msauth.auth.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserModel {

    private String name;

    private String email;

    private String phoneNumber;


}
