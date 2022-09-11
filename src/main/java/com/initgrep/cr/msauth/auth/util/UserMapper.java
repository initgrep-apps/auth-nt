package com.initgrep.cr.msauth.auth.util;

import com.initgrep.cr.msauth.auth.dto.RegisterModel;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.entity.AppUser;

public final class UserMapper {

    public static UserModel fromEntity(AppUser appUser) {
        return UserModel.builder()
                .name(appUser.getFullName())
                .email(appUser.getEmail())
                .phoneNumber(appUser.getPhoneNumber())
                .build();
    }

    /** Password is not set in this conversion **/
    public static AppUser toEntityFromRegisterModel(RegisterModel registerModel) {
        AppUser appUser = new AppUser();
        appUser.setEmail(registerModel.getEmail());
        appUser.setFullName(registerModel.getFullName());
        appUser.setPhoneNumber(registerModel.getPhoneNumber());
        //set encoded password using a setter
        return appUser;
    }

    private UserMapper(){}
}
