package com.cts.rbp.movieapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.cts.rbp.movieapp.model.Movie;


public interface MovieRepository extends MongoRepository<Movie, String>{
	
	@Query("{movieName:{$regex:?0, $options:'i'}}")
	List<Movie> findByMovieName(String movieName);

	@Query("{'movieName' : ?0,'theatreName' : ?1}")
	List<Movie> findAvailableTickets(String movieName, String theatername);
	
	
	 void deleteByMovieName(String movieName);
	 

}
