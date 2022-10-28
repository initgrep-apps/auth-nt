package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.constants.TokenType;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.entity.AppUser;
import com.initgrep.cr.msauth.auth.entity.AppUserToken;
import com.initgrep.cr.msauth.auth.entity.Role;
import com.initgrep.cr.msauth.auth.entity.UserAccount;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Set;

class ServiceTestUtil {

    public static UserModel getUserModel() {
        return UserModel.builder()
                .fullName("user full name")
                .email("test@email.com")
                .build();
    }

    public static UsernamePasswordAuthenticationToken getMockAuthentication(UserModel user) {
        return UsernamePasswordAuthenticationToken.unauthenticated(user, null);
    }

    public static AppUserToken getMockAppUserRefreshToken(int hits) {
        return AppUserToken.builder()
                .tokenType(TokenType.REFRESH)
                .jwtId("2")
                .hits(hits)
                .build();
    }

    public static AppUser getAppUser(String id, String email, String phone, Role userRole){
        AppUser user =  new AppUser();
        user.setIdentifier(id);
        user.setFullName("test user");
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setPassword("pas@sdrew");
        user.setRoles(Set.of(userRole));
        user.setAccount(new UserAccount());
        user.getAccount().setAccountExpired(false);
        user.getAccount().setAccountLocked(false);
        user.getAccount().setAccountLocked(true);
        return user;
    }

    public static Role getUserRole(){
        Role userRole =  new Role();
        userRole.setName("test_role");
        userRole.setPermissions(Collections.emptySet());
        return userRole;
    }

}
