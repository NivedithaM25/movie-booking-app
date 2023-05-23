package com.cts.rbp.movieapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cts.rbp.movieapp.model.Movie;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String>{

}
