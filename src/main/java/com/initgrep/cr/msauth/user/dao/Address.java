package com.initgrep.cr.msauth.user.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	private String house;
	private String street;
	private String city;
	private String state;
	private String country;
	private String pincode;
	

}
