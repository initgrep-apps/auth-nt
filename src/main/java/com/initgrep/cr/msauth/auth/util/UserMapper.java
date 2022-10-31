package com.initgrep.cr.msauth.auth.util;

import com.initgrep.cr.msauth.auth.dto.RegisterRequest;
import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.entity.AppUser;
import com.initgrep.cr.msauth.auth.entity.Role;
import com.initgrep.cr.msauth.auth.entity.UserAccount;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class UserMapper {

    public static UserModel fromEntity(AppUser user) {
        return UserModel.builder()
                .identifier(user.getIdentifier())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .grantedAuthorities(getAuthoritiesFromRoles(user.getRoles()))
                .isAccountNonExpired(!user.getAccount().isAccountExpired())
                .isAccountNonLocked(!user.getAccount().isAccountLocked())
                .isCredentialsNonExpired(!user.getAccount().isCredentialExpired())
                .isEnabled(user.getAccount().isEnabled())
                .build();
    }

    public static AppUser toEntityWithAccountEnabled(UserModel userModel) {
        AppUser appUser = new AppUser();
        appUser.setEmail(userModel.getEmail());
        appUser.setFullName(userModel.getFullName());
        appUser.setPhoneNumber(userModel.getPhoneNumber());
        appUser.setPassword(userModel.getPassword());
        appUser.setAccount(new UserAccount());
        appUser.getAccount().setAccountExpired(false);
        appUser.getAccount().setAccountLocked(false);
        appUser.getAccount().setCredentialExpired(false);
        appUser.getAccount().setEnabled(true);
        return appUser;
    }

    public static UserModel toUserModel(RegisterRequest registerRequest) {
        return UserModel.builder()
                .email(registerRequest.getEmail())
                .fullName(registerRequest.getFullName())
                .phoneNumber(registerRequest.getPhoneNumber())
                .build();
    }


    private static Set<SimpleGrantedAuthority> getAuthoritiesFromRoles(Set<Role> roles) {
        return roles.stream()
                .flatMap(role -> {
                    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                    authorities.addAll(role.getPermissions().stream()
                            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                            .collect(Collectors.toSet()));
                    return authorities.stream();
                }).collect(Collectors.toSet());
    }

    private UserMapper() {
    }
}
