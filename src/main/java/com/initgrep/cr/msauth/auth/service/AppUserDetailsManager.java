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
     * Remove the user with the given login name from the system.
     */
    void deleteUser(String identifier);

    /**
     * Modify the current user's password. This should change the user's password in the
     * persistent user repository (datbase, LDAP etc).
     * @param oldPassword current password (for re-authentication if required)
     * @param newPassword the password to change to
     */
    void changePassword(String oldPassword, String newPassword);

    /**
     * Check if a user with the supplied login name exists in the system.
     */
    boolean userExists(String identifier);


    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException;
}


