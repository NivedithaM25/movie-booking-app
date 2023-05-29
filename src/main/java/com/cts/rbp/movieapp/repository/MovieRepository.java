package com.cts.rbp.movieapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.rbp.movieapp.model.Movie;


public interface MovieRepository extends MongoRepository<Movie, String>{
	
	@Query()
	List<Movie> findByMovieName(String movieName);

	List<Movie> findAvailableTickets(String movieName, String theatername);

}
