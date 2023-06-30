package com.cts.rbp.movieapp.payload.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupRequest {
	 @NotBlank
	    @Size(min = 3, max = 20)
	    private String loginId;

	    @NotBlank
	    private String firstName;


	    @NotBlank
	    private String lastName;

	    @NotBlank
	    @Size(max = 50)
	    @Email
	    private String email;

	    private Long contactNumber;

	    private Set<String> roles;

	    @NotBlank
	    @Size(min = 6, max = 40)
	    private String password;
	    @NotBlank
	    @Size(min = 6, max = 40)
	    private String confirmPassword;

	    public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Long getContactNumber() {
			return contactNumber;
		}

		public void setContactNumber(Long contactNumber) {
			this.contactNumber = contactNumber;
		}

		public Set<String> getRoles() {
			return roles;
		}

		public void setRoles(Set<String> roles) {
			this.roles = roles;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
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

		public String getConfirmPassword() {
			return confirmPassword;
		}

		public void setConfirmPassword(String confirmPassword) {
			this.confirmPassword = confirmPassword;
		}
	    
	    
}
