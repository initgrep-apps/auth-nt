package com.initgrep.cr.msauth.auth.entity;

import com.initgrep.cr.msauth.auth.constants.TokenType;
import com.initgrep.cr.msauth.auth.entity.audit.BaseAuditEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "app_token")
@NoArgsConstructor
public class AppUserToken extends BaseAuditEntity {
    @Builder.Default
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id = 0L;

    @Column(name = "jit")
    private String jwtId;

    private TokenType tokenType;

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private int hits = 1;

}