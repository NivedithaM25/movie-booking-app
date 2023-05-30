package com.cts.rbp.movieapp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cts.rbp.movieapp.model.ERole;
import com.cts.rbp.movieapp.model.Role;

public interface RoleRepository extends MongoRepository<Role, String>{

	Optional<Role> findByName(ERole name);
	
}
