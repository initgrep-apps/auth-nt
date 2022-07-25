package com.initgrep.cr.msauth.user.dto;

import java.util.List;

public class UserDto {
	
	private String userId;
	private String name;
	private List<String> addresses;

	
	
	public UserDto(String userId, String name, List<String> addresses) {
		super();
		this.userId = userId;
		this.name = name;
		this.addresses = addresses;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}
	
}
