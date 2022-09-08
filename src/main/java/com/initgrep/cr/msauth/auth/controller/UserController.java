package com.initgrep.cr.msauth.auth.controller;

import com.initgrep.cr.msauth.auth.entity.AppUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/{id}")
    @PreAuthorize("#user.id == #id")
    public AppUser getUser(@AuthenticationPrincipal AppUser user, @PathVariable("id") Long id){
            return user;
    }
}
