package com.initgrep.cr.msauth.user.dao;

import javax.persistence.Id;

public class Address {
	@Id
	private String addressId;
	private String address;
	
	
	
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}
