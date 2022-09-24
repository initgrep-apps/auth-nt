package com.initgrep.cr.msauth.auth.providers.adapter;

import com.initgrep.cr.msauth.auth.dto.UserModel;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

public class AppAuditor implements AuditorAware<String> {
    @Override
    public @NonNull Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.isNull(authentication) || !authentication.isAuthenticated()){
            return Optional.empty();
        }
        return Optional.of(((UserModel)authentication.getPrincipal()).getIdentifier());
    }
}
