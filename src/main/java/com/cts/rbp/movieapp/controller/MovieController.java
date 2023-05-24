package com.cts.rbp.movieapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.rbp.movieapp.exception.MoviesNotFound;
import com.cts.rbp.movieapp.model.Movie;
import com.cts.rbp.movieapp.model.Ticket;
import com.cts.rbp.movieapp.repository.MovieRepository;
import com.cts.rbp.movieapp.services.MovieService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1.0/moviebooking")
public class MovieController {

	@Autowired
	private MovieService movieService;
	
	@Autowired
	private MovieRepository movieRepo;
	
	
	@GetMapping("/all")
	@ApiOperation("Get all Movie available")
	public ResponseEntity<List<Movie>> getAllMovies(){
		List<Movie> movieList = movieRepo.findAll();
		if(movieList.isEmpty()) {
		
			throw new MoviesNotFound("No Movies Found");
		}
		else {
			return new ResponseEntity<>(movieList,HttpStatus.FOUND);
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity addMovie(@RequestBody Movie movie){
		
		List<Movie> movieList=movieService.getAllMovies();
		
		if(movieList.isEmpty()) {
			
		}
		movieService.saveMovie(movie);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/movie/search/{movieName}")
	@ApiOperation("Get movie details by movie name")
	public ResponseEntity<List<Movie>> getByMovieName(@PathVariable("movieName") String movieName){
		
		List<Movie> movieList = movieService.findByMovieName(movieName);
		
		if(movieList.isEmpty()) {
			throw new MoviesNotFound("Movie not available");
		}
		return new ResponseEntity<>(movieList,HttpStatus.FOUND);
	}
	
	@GetMapping("/getAllBookedTickets/{movieName}")
	@ApiOperation("Get all booked tickets by movie name")
	public ResponseEntity<List<Ticket>> getALlBookedTickets(@PathVariable("movieName") String movieName){
		
		return new ResponseEntity<>(movieService.getALlBookedTickets(movieName),HttpStatus.OK);
	}
	
	@PostMapping("add/tickets")
	public ResponseEntity addTickets(@RequestBody Ticket ticket){
		
		movieService.addTikcet(ticket);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
}
