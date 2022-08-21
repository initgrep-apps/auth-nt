package com.initgrep.cr.msauth.user.dto;

import com.initgrep.cr.msauth.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserModel {

    private String name;

    private String email;

    private String phoneNumber;

    public static UserModel fromEntity(User user) {
        return UserModel.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public static User toEntity(UserModel userModel) {
        User user = new User();
        user.setName(userModel.getName());
        user.setEmail(userModel.getEmail());
        user.setPhoneNumber(userModel.getPhoneNumber());
        return user;
    }
}
