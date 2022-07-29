package com.initgrep.cr.msauth.user.util;

import com.initgrep.cr.msauth.user.entity.Address;
import com.initgrep.cr.msauth.user.entity.User;
import com.initgrep.cr.msauth.user.dto.AddressDto;
import com.initgrep.cr.msauth.user.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toUserDto(User user) {

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    public User toUserEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        return user;
    }

    public Address toAddressEntity(AddressDto dto) {
        Address address = new Address();
        address.setId(dto.getId());
        address.setHouse(dto.getHouse());
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setCountry(dto.getCountry());
        address.setPinCode(dto.getPinCode());
        return address;
    }

}