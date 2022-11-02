package com.initgrep.cr.msauth.auth.service;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AppUserDetailsManager extends UserDetailsService {

    /**
     * Create a new user with the supplied details.
     */
    UserModel createUser(UserModel user);

    /**
     * Update the specified user.
     */
    UserModel updateUser(UserModel user);

    /**
     * Check if a user with the supplied login name exists in the system.
     */
    boolean userExists(String identifier);


    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException;
}


