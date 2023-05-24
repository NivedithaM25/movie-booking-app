package com.cts.rbp.movieapp.model;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(value = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

	@Id
	private ObjectId id;
	
	@NotBlank
	@Size(max=20)
	private String loginId;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	@NotBlank
	@Email
	@Size(max=50)
	private String email;
	
	@NotBlank
	private long contactNumber;
	
	@NotBlank
	private String password;
	
	@DBRef
	private Set<Role> roles = new HashSet<>();

	public User(@NotBlank @Size(max = 20) String loginId, @NotBlank String firstName, @NotBlank String lastName,
			@NotBlank @Email @Size(max = 50) String email, @NotBlank long contactNumber, @NotBlank String password,
			Set<Role> roles) {
		super();
		this.loginId = loginId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.contactNumber = contactNumber;
		this.password = password;
		this.roles = roles;
	}

	public User(@NotBlank @Size(max = 20) String loginId, @NotBlank String firstName, @NotBlank String lastName,
			@NotBlank @Email @Size(max = 50) String email, @NotBlank long contactNumber, @NotBlank String password) {
		super();
		this.loginId = loginId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.contactNumber = contactNumber;
		this.password = password;
	}
	
	
	
	
}

