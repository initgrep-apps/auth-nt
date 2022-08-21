package com.initgrep.cr.msauth.user.service;

import com.initgrep.cr.msauth.user.dto.UserModel;
import com.initgrep.cr.msauth.user.entity.User;
import com.initgrep.cr.msauth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserModel saveUser(UserModel userModel){
        User savedUser = userRepository.save(UserModel.toEntity(userModel));
        return UserModel.fromEntity(savedUser);
    }

}


