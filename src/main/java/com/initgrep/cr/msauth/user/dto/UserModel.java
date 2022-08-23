package com.initgrep.cr.msauth.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserModel {

    private String name;

    private String email;

    private String phoneNumber;


}
