package com.initgrep.cr.msauth.user.entity;

import com.initgrep.cr.msauth.base.entity.BaseAuditEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Address extends BaseAuditEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String house;
	private String street;
	private String city;
	private String state;
	private String country;
	private String pinCode;

	@ManyToOne
	@JoinColumn(name = "user_id", updatable = false, nullable = false)
	private AppUser appUser;
	

}
