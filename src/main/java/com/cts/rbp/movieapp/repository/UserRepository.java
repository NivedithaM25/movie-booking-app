package com.cts.rbp.movieapp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cts.rbp.movieapp.model.User;

public interface UserRepository extends MongoRepository<User, String>{

	 Optional<User> findByUsername(String username);

	    Boolean existsByUsername(String username);

	    Boolean existsByEmail(String email);
	    Optional<User> findByLoginId(String username);

	    boolean existsByLoginId(String loginId);
	
}
