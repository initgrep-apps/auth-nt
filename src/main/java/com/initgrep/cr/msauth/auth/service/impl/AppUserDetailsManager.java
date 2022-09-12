package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.entity.AppUser;
import com.initgrep.cr.msauth.auth.exception.UserExistsException;
import com.initgrep.cr.msauth.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.USER_ALREADY_EXIST;
import static com.initgrep.cr.msauth.auth.constants.AuthConstants.USER_NOT_FOUND;

@Service
public class AppUserDetailsManager implements UserDetailsManager {

    @Autowired
    UserRepository userRepository;

    @Override
    public void createUser(UserDetails user) {
        throwIfUserExists((AppUser) user);
        userRepository.save((AppUser) user);
    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByPhoneNumber(username)
                .or(() -> userRepository.findByPhoneNumber(username))
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    private void throwIfUserExists(AppUser user) {
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())
                || userRepository.existsByEmail(user.getEmail())
        ) {
            throw new UserExistsException(USER_ALREADY_EXIST);
        }
    }
}


