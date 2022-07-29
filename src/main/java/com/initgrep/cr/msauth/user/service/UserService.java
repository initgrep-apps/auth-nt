package com.initgrep.cr.msauth.user.service;

import com.initgrep.cr.msauth.user.entity.User;
import com.initgrep.cr.msauth.user.dto.UserDto;
import com.initgrep.cr.msauth.user.repository.UserRepository;
import com.initgrep.cr.msauth.user.service.UserValidator.ValidatorResult;
import com.initgrep.cr.msauth.user.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper mapper;

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream().map(user -> mapper.toUserDto(user))
                .collect(Collectors.toList());
    }

	@Transactional
    public UserDto save(UserDto userDto) {
        ValidatorResult validatorResult = UserValidator.validateUserDtoInfo(userDto);
        if (!validatorResult.isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validatorResult.getMessage());
        }
        User savedUser = userRepository.save(mapper.toUserEntity(userDto));
        return mapper.toUserDto(savedUser);
    }
}
