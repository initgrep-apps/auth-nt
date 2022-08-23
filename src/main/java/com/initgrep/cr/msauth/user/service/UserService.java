package com.initgrep.cr.msauth.user.service;

import com.initgrep.cr.msauth.user.dto.UserModel;
import com.initgrep.cr.msauth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.initgrep.cr.msauth.user.util.UserMapper.fromEntity;
import static com.initgrep.cr.msauth.user.util.UserMapper.toEntity;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public UserModel saveUser(UserModel userModel){
        return fromEntity(userRepository.save(toEntity(userModel)));
    }

}


