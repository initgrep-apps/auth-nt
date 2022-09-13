package com.initgrep.cr.msauth.auth.entity;

import com.initgrep.cr.msauth.base.entity.BaseAuditEntity;
import com.initgrep.cr.msauth.base.entity.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser extends BaseAuditEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String email;

    @NonNull
    private String fullName;

    @NonNull
    @Column(unique = true)
    private String phoneNumber;
    @NonNull
    private String password;

    @ManyToMany
    @JoinTable(
            name = "app_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "isAccountExpired", column = @Column(name = "account_expired")),
            @AttributeOverride(name = "isAccountLocked", column = @Column(name = "account_locked")),
            @AttributeOverride(name = "isCredentialExpired", column = @Column(name = "credential_expired")),
            @AttributeOverride(name = "isEnabled", column = @Column(name = "enabled")),
    })
    private UserAccount account;

}
