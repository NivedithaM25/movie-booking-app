package com.cts.rbp.movieapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection="roles")
@Getter
@Setter
public class Role {

	
	@Id
	private String id;
	
	private ERole name;

	public Role(ERole name) {
		super();
		this.name = name;
	}
	
	
	
}
