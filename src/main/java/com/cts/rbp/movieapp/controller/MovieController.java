package com.cts.rbp.movieapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.rbp.movieapp.model.Movie;
import com.cts.rbp.movieapp.repository.MovieRepository;
import com.cts.rbp.movieapp.services.MovieService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1.0/moviebooking")
@Slf4j
public class MovieController {

	@Autowired
	private MovieService movieService;
	
	@Autowired
	private MovieRepository movieRepo;
	
	
	@GetMapping("/all")
	public ResponseEntity<List<Movie>> getAllMovies(){
		List<Movie> movieList = movieRepo.findAll();
		
		if(movieList.isEmpty()) {
			System.out.println("Empty list");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<>(movieList,HttpStatus.FOUND);
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity addMovie(@RequestBody Movie movie){
		
		movieService.saveMovie(movie);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
