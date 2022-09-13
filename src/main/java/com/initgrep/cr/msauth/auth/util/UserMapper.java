package com.initgrep.cr.msauth.auth.util;

import com.initgrep.cr.msauth.auth.dto.RegisterModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.entity.User;

public final class UserMapper {

    public static UserModel fromEntity(User user) {
        return UserModel.builder()
                .name(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    /** Password is not set in this conversion **/
    public static User toEntityFromRegisterModel(RegisterModel registerModel) {
        User user = new User();
        user.setEmail(registerModel.getEmail());
        user.setFullName(registerModel.getFullName());
        user.setPhoneNumber(registerModel.getPhoneNumber());
        //set encoded password using a setter
        return user;
    }

    private UserMapper(){}
}
