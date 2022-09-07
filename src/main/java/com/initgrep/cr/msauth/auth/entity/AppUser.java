package com.initgrep.cr.msauth.auth.entity;

import com.initgrep.cr.msauth.base.entity.BaseAuditEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Collections;


@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class AppUser extends BaseAuditEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NonNull
//    private String fullName;
//
//    @OneToMany(mappedBy = "appUser")
//    private transient  List<Address> addresses;
//
//    @NonNull
//    @Column(unique = true)
//    private String email;
//
//    @NonNull
//    @Column(unique = true)
//    private String phoneNumber;

    @NonNull
    private String username;
    @NonNull
    private String password;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public @NonNull String getPassword() {
        return password;
    }

    @Override
    public @NonNull String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
