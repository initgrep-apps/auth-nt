package com.initgrep.cr.msauth.user.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String house;
	private String street;
	private String city;
	private String state;
	private String country;
	private String pinCode;

	@ManyToOne
	@JoinColumn(name = "user_id", updatable = false, nullable = false)
	private User user;
	

}
