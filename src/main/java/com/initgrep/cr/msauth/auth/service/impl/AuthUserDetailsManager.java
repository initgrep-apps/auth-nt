package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.exception.UserExistsException;
import com.initgrep.cr.msauth.auth.repository.RoleRepository;
import com.initgrep.cr.msauth.auth.repository.UserRepository;
import com.initgrep.cr.msauth.auth.service.AppUserDetailsManager;
import com.initgrep.cr.msauth.auth.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.USER_ALREADY_EXIST;
import static com.initgrep.cr.msauth.auth.constants.AuthConstants.USER_NOT_FOUND;

@Service
public class AuthUserDetailsManager implements AppUserDetailsManager {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserModel createUser(UserModel user) {
        throwIfExistingUser(user);
        var appUser = UserMapper.toEntityWithAccountEnabled(user);
        var roleUser = roleRepository.findByName("user");
        appUser.setRoles(Collections.singleton(roleUser));
        appUser = userRepository.save(appUser);
        return UserMapper.fromEntity(appUser);
    }

    @Override
    public UserModel updateUser(UserModel user) {
        return null;
    }

    @Override
    public void deleteUser(String identifier) {
        // TODO document why this method is empty
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // TODO document why this method is empty
    }

    @Override
    public boolean userExists(String identifier) {
        return userRepository.existsByIdentifier(identifier);
    }


    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return UserMapper.fromEntity(userRepository.findByIdentifier(identifier)
                .or(() -> userRepository.findByPhoneNumber(identifier))
                .or(() -> userRepository.findByEmail(identifier))
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND)));
    }

    private void throwIfExistingUser(UserModel userModel) {
        if (userRepository.existsByEmail(userModel.getEmail()) || userRepository.existsByPhoneNumber(userModel.getPhoneNumber()) ) {
            throw new UserExistsException(USER_ALREADY_EXIST);
        }
    }
}


