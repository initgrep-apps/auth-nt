package com.initgrep.cr.msauth.base.entity;

import lombok.*;

import javax.persistence.Embeddable;
@Getter
@Setter
@Embeddable
@NoArgsConstructor
public class UserAccount {
    private boolean isAccountExpired;
    private boolean isAccountLocked;
    private boolean isCredentialExpired;
    private boolean isEnabled;
}
