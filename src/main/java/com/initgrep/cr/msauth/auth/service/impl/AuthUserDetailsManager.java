package com.initgrep.cr.msauth.auth.service.impl;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import com.initgrep.cr.msauth.auth.exception.UserExistsException;
import com.initgrep.cr.msauth.auth.repository.RoleRepository;
import com.initgrep.cr.msauth.auth.repository.UserRepository;
import com.initgrep.cr.msauth.auth.service.AppUserDetailsManager;
import com.initgrep.cr.msauth.auth.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.initgrep.cr.msauth.auth.constants.AuthConstants.*;

@RequiredArgsConstructor
@Service
public class AuthUserDetailsManager implements AppUserDetailsManager {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserModel createUser(UserModel user) {
        throwIfExistingUser(user);
        var appUser = UserMapper.toEntityWithAccountEnabled(user);
        var roleUser = roleRepository.findByName(ROLE_USER);
        appUser.setRoles(Set.of(roleUser));
        appUser = userRepository.save(appUser);
        return UserMapper.fromEntity(appUser);
    }

    @Override
    public UserModel updateUser(UserModel user) {
        throw new UnsupportedOperationException("Operation not Implemented yet");
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
        if (userRepository.existsByEmail(userModel.getEmail()) || userRepository.existsByPhoneNumber(userModel.getPhoneNumber())) {
            throw new UserExistsException(USER_ALREADY_EXIST);
        }
    }
}


