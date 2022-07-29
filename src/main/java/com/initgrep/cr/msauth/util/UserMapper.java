package com.initgrep.cr.msauth.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.initgrep.cr.msauth.user.dao.User;
import com.initgrep.cr.msauth.user.dto.AddressDto;
import com.initgrep.cr.msauth.user.dto.UserDto;

@Component
public class UserMapper {

        public UserDto  toUserDto(User user) {
            
            List<AddressDto> addressDtos = user.getAddresses()
            .stream()
            .map(a -> new AddressDto(ad, address.getAddress()))
            .collect(Collectors.toList());
            return new UserDto(user.getUserId(), user.getName(), addressDtos);
        }
    
        public User toUserEntity(UserDto userDto){
                userDto.getAddresses()
                .stream()
                .map(dto -> new Address())
        
    }

    public Address toAddressEntity(AddressDto address){
            Address
    }
    
}
