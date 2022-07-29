package com.initgrep.cr.msauth.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {

    private String id;

    private String name;

    private String email;

    private String phoneNumber;
}
