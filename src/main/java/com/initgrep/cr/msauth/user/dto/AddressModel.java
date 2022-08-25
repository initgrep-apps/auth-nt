package com.initgrep.cr.msauth.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class AddressModel {
    private Long id;
	private String house;
	private String street;
	private String city;
	private String state;
	private String country;
	private String pinCode;

}
