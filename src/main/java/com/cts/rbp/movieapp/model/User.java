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
	
	

	public User() {
		super();
	}

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

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	  public String getUsername() {
	        return this.loginId;
	    }
	
	
}

