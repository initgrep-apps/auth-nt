package com.initgrep.cr.msauth.user.entity;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String name;
	
	@NotNull
	private String email;
	
	@NotNull
	private String phoneNumber;
	
	@OneToMany(mappedBy = "user")
	private List<Address> addresses;	
	
}
