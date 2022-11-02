package com.initgrep.cr.msauth.auth.util;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.entity.AppUser;
import com.initgrep.cr.msauth.auth.entity.Permission;
import com.initgrep.cr.msauth.auth.entity.Role;
import com.initgrep.cr.msauth.auth.entity.UserAccount;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class UserMapperTest {


    @Test
    void fromEntity() {
        UserModel userModel = UserMapper.fromEntity(getMockAppUser());
        assertThat(userModel)
                .satisfies(u -> {
                    assertThat(u.getIdentifier()).isNotBlank();
                    assertThat(u.getFullName()).isNotBlank();
                    assertThat(u.getEmail()).isNotBlank();
                    assertThat(u.getPassword()).isNotBlank();
                    assertThat(u.getPhoneNumber()).isNotBlank();
                    assertThat(u.getAuthorities()).isNotEmpty();
                    assertThat(u.isAccountNonExpired()).isTrue();
                    assertThat(u.isAccountNonLocked()).isTrue();
                    assertThat(u.isEnabled()).isTrue();
                });
    }

    @Test
    void fromEntity_withAccountExpired() {
        AppUser mockAppUser = getMockAppUser();
        mockAppUser.getAccount().setAccountExpired(true);
        mockAppUser.getAccount().setAccountLocked(true);
        mockAppUser.getAccount().setCredentialExpired(true);
        mockAppUser.getAccount().setEnabled(false);
        UserModel userModel = UserMapper.fromEntity(mockAppUser);
        assertThat(userModel)
                .satisfies(u -> {
                    assertThat(u.getIdentifier()).isNotBlank();
                    assertThat(u.getFullName()).isNotBlank();
                    assertThat(u.getEmail()).isNotBlank();
                    assertThat(u.getPassword()).isNotBlank();
                    assertThat(u.getPhoneNumber()).isNotBlank();
                    assertThat(u.getAuthorities()).isNotEmpty();
                    assertThat(u.isAccountNonExpired()).isFalse();
                    assertThat(u.isAccountNonLocked()).isFalse();
                    assertThat(u.isEnabled()).isFalse();
                });
    }

    private AppUser getMockAppUser(){
        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setIdentifier("identifier");
        appUser.setFullName("test full name");
        appUser.setEmail("test@email.com");
        appUser.setPhoneNumber("0000000000");
        appUser.setPassword("0000000000");
        appUser.setAccount(new UserAccount());
        appUser.getAccount().setAccountExpired(false);
        appUser.getAccount().setAccountLocked(false);
        appUser.getAccount().setCredentialExpired(false);
        appUser.getAccount().setEnabled(true);

        Role role = new Role();
        role.setName("user");
        role.setId(1L);
        role.setDescription("userRole");

        Permission read = new Permission();
        read.setName("read");
        read.setId(1L);
        read.setDescription("read permission description");
        role.setPermissions(Set.of(read));
        appUser.setRoles(Set.of(role));
        return appUser;
    }

}