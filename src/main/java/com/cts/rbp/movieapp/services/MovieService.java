package com.cts.rbp.movieapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.rbp.movieapp.model.Movie;
import com.cts.rbp.movieapp.repository.MovieRepository;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepo;
	
	public List<Movie> getAllMovies(){
		return movieRepo.findAll();
	}
	
	public void saveMovie(Movie movie) {
		movieRepo.save(movie);
	}
	
}
