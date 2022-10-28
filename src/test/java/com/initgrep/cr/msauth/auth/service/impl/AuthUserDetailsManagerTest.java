package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.entity.AppUser;
import com.initgrep.cr.msauth.auth.entity.Role;
import com.initgrep.cr.msauth.auth.exception.UserExistsException;
import com.initgrep.cr.msauth.auth.repository.RoleRepository;
import com.initgrep.cr.msauth.auth.repository.UserRepository;
import com.initgrep.cr.msauth.auth.util.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUserDetailsManagerTest {

    @InjectMocks
    AuthUserDetailsManager userDetailsManager;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Test
    void test_createUser_ForExistingUserEmail() {
        testExistingUser(true, false);
    }

    @Test
    void test_createUser_ForExistingUserPhone() {
        testExistingUser(false, true);
    }

    @Test
    void test_createUser_ForExistingEmailAndPhone() {
        testExistingUser(true, true);
    }

    void testExistingUser(boolean isExistingEmail, boolean isExistingPhone) {
        String phone = "0000000000";
        String email = "test@email.com";
        UserModel userModel = UserModel.builder().phoneNumber(phone).email(email).build();
        lenient().when(userRepository.existsByPhoneNumber(phone)).thenReturn(isExistingPhone);
        lenient().when(userRepository.existsByEmail(email)).thenReturn(isExistingEmail);
        assertThat(
                catchThrowable(() -> userDetailsManager.createUser(userModel)))
                .isInstanceOf(UserExistsException.class).hasMessage(USER_ALREADY_EXIST);
    }

    @Captor
    ArgumentCaptor<AppUser> appUserArgumentCaptor;

    @Test
    void test_createUser() {
        String phone = "0000000000";
        String email = "test@email.com";
        UserModel userModel = UserModel.builder()
                .phoneNumber(phone)
                .email(email)
                .fullName("test")
                .password("")
                .grantedAuthorities(Collections.emptySet())
                .build();

        Role userRole = ServiceTestUtil.getUserRole();

        AppUser appUser = UserMapper.toEntityWithAccountEnabled(userModel);
        appUser.setRoles(Set.of(userRole));
        System.out.println(appUser);
        when(roleRepository.findByName(ROLE_USER)).thenReturn(userRole);
        when(userRepository.existsByPhoneNumber(phone)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(appUserArgumentCaptor.capture())).thenReturn(appUser);

        UserModel user = userDetailsManager.createUser(userModel);
        assertThat(user.getEmail()).isEqualTo(userModel.getEmail());
        assertThat(user.getPhoneNumber()).isEqualTo(userModel.getPhoneNumber());
        AppUser appUserArgument = appUserArgumentCaptor.getValue();
        assertThat(appUserArgument.getRoles()).contains(userRole);
    }


    @Test
    void test_userExists() {
        when(userRepository.existsByIdentifier("1")).thenReturn(true);
        boolean exists = userDetailsManager.userExists("1");
        assertThat(exists).isTrue();

    }

    @Test
    void test_userNotExists() {
        when(userRepository.existsByIdentifier("1")).thenReturn(false);
        boolean notExists = userDetailsManager.userExists("1");
        assertThat(notExists).isFalse();

    }

    @Test
    void test_loadUserByUsername_forIdentifier() {

        var phone = "0000000000";
        var email = "test@email.com";
        var id = "1";
        Role userRole = ServiceTestUtil.getUserRole();
        AppUser user = ServiceTestUtil.getAppUser(id, email, phone, userRole);
        lenient().when(userRepository.findByIdentifier(id)).thenReturn(Optional.of(user));
        lenient().when(userRepository.findByPhoneNumber(phone)).thenReturn(Optional.empty());
        lenient().when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserModel userDetails = (UserModel) userDetailsManager.loadUserByUsername(id);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getPhoneNumber()).isEqualTo(phone);
        assertThat(userDetails.getEmail()).isEqualTo(email);
        assertThat(userDetails.getIdentifier()).isEqualTo(id);

    }


    @Test
    void test_loadUserByUsername_forEmail() {

        var phone = "0000000000";
        var email = "test@email.com";
        var id = "1";
        Role userRole = ServiceTestUtil.getUserRole();
        AppUser user = ServiceTestUtil.getAppUser(id, email, phone, userRole);

        lenient().when(userRepository.findByIdentifier(id)).thenReturn(Optional.empty());
        lenient().when(userRepository.findByPhoneNumber(phone)).thenReturn(Optional.empty());
        lenient().when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserModel userDetails = (UserModel) userDetailsManager.loadUserByUsername(email);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getPhoneNumber()).isEqualTo(phone);
        assertThat(userDetails.getEmail()).isEqualTo(email);
        assertThat(userDetails.getIdentifier()).isEqualTo(id);

    }


    @Test
    void test_loadUserByUsername_forPhone() {

        var phone = "0000000000";
        var email = "test@email.com";
        var id = "1";
        Role userRole = ServiceTestUtil.getUserRole();
        AppUser user = ServiceTestUtil.getAppUser(id, email, phone, userRole);

        lenient().when(userRepository.findByIdentifier(id)).thenReturn(Optional.empty());
        lenient().when(userRepository.findByPhoneNumber(phone)).thenReturn(Optional.of(user));
        lenient().when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserModel userDetails = (UserModel) userDetailsManager.loadUserByUsername(phone);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getPhoneNumber()).isEqualTo(phone);
        assertThat(userDetails.getEmail()).isEqualTo(email);
        assertThat(userDetails.getIdentifier()).isEqualTo(id);

    }

    @Test
    void test_loadUserByUsername_forException() {
        var phone = "0000000000";
        var email = "test@email.com";
        var id = "1";
        lenient().when(userRepository.findByIdentifier(id)).thenReturn(Optional.empty());
        lenient().when(userRepository.findByPhoneNumber(phone)).thenReturn(Optional.empty());
        lenient().when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThat(catchThrowable(() -> userDetailsManager.loadUserByUsername(id)))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(USER_NOT_FOUND);

    }
}