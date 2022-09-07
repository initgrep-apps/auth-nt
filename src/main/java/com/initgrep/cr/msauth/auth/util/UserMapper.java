package com.initgrep.cr.msauth.auth.util;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.entity.AppUser;

public final class UserMapper {

    public static UserModel fromEntity(AppUser appUser) {
        return UserModel.builder()
//                .name(appUser.getFullName())
//                .email(appUser.getEmail())
//                .phoneNumber(appUser.getPhoneNumber())
                .build();
    }

    public static AppUser toEntity(UserModel userModel) {
        AppUser appUser = new AppUser();
//        appUser.setFullName(userModel.getName());
//        appUser.setEmail(userModel.getEmail());
//        appUser.setPhoneNumber(userModel.getPhoneNumber());
        return appUser;
    }

    private UserMapper(){}
}
