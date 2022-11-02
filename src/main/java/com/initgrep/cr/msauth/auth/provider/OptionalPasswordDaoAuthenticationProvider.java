package com.initgrep.cr.msauth.auth.provider;

import com.initgrep.cr.msauth.auth.service.AppUserDetailsManager;
import com.initgrep.cr.msauth.auth.util.UtilMethods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Qualifier("OptionalPasswordDaoAuthenticationProvider")
public final class OptionalPasswordDaoAuthenticationProvider extends DaoAuthenticationProvider {


    @Autowired
    public OptionalPasswordDaoAuthenticationProvider(PasswordEncoder passwordEncoder, AppUserDetailsManager userDetailsManager) {
        super.setPasswordEncoder(passwordEncoder);
        super.setUserDetailsService(userDetailsManager);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

        if (UtilMethods.isEmpty(authentication.getCredentials().toString())) {
            log.debug("password is empty so no further password checks");
            return;
        }

        String presentedPassword = authentication.getCredentials().toString();
        String userPassword = userDetails.getPassword();
        boolean isAMatch = getPasswordEncoder().matches(presentedPassword, userPassword);
        if (!isAMatch) {
            log.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }
}
