package com.initgrep.cr.msauth.user.service;

import java.util.ArrayList;
import java.util.List;

import com.initgrep.cr.msauth.user.dao.Address;
import com.initgrep.cr.msauth.user.dao.User;
import com.initgrep.cr.msauth.user.dto.UserDto;

public class Mapper {
	public UserDto  toDto(User user) {
		String userId = user.getUserId();
        String name = user.getName();
        List<String> addresses = new ArrayList<String>();
        
        for (Address address: user.getAddresses()) {
        	addresses.add(address.getAddress());
        }
        return new UserDto(userId, name, addresses);
    }
}
