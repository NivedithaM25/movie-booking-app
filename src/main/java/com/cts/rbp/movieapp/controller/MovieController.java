package com.cts.rbp.movieapp.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.rbp.movieapp.exception.MoviesNotFound;
import com.cts.rbp.movieapp.model.Movie;
import com.cts.rbp.movieapp.model.Ticket;
import com.cts.rbp.movieapp.repository.MovieRepository;
import com.cts.rbp.movieapp.services.MovieService;

import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.annotations.ApiOperation;
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
	//@ApiOperation("Search all Movie available")
	@Operation(summary = "View all movies")
	public ResponseEntity<List<Movie>> getAllMovies(){
		List<Movie> movieList = movieRepo.findAll();
		if(movieList.isEmpty()) {
		
			throw new MoviesNotFound("No Movies Found");
		}
		else {
			return new ResponseEntity<>(movieList,HttpStatus.FOUND);
		}
	}
	
	@GetMapping("/movie/search/{movieName}")
	@Operation(summary = "Search movie by movie name")
	//@ApiOperation("Search movie by movie name")
	public ResponseEntity<List<Movie>> getByMovieName(@PathVariable("movieName") String movieName){
		
		List<Movie> movieList = movieService.getMovieByName(movieName);
		
		if(movieList.isEmpty()) {
			throw new MoviesNotFound("Movie not available");
		}
		return new ResponseEntity<>(movieList,HttpStatus.FOUND);
	}
	
	
	@PostMapping("/{movieName}/add")
	@Operation(summary="Book tickets for a movie")
	public ResponseEntity<String> bookTickets(@RequestBody Ticket ticket,@PathVariable("movieName") String movieName){
		
		return movieService.bookTickets(ticket,movieName);
	}
	
//	//Admin Access
//	@GetMapping("/getallbookedtickets/{movieName}")
//	//@ApiOperation("get all booked tickets (Admin only)")
//	public ResponseEntity<List<Ticket>> getAllBookedTickets(@PathVariable String movieName){
//		return new ResponseEntity<>(movieService.getALlBookedTickets(movieName),HttpStatus.OK);
//	}
	
	@PutMapping("/{movieName}/update/{ticketId}")
	@Operation(summary="Update ticket status")
	public ResponseEntity<String> upadteTicketStatus(@PathVariable String movieName,@PathVariable ObjectId ticket){
		return new ResponseEntity<String>(movieService.updateTicketStatus(movieName,ticket),HttpStatus.OK);
	}
	
	@DeleteMapping("/{movieName}/delete")
	@Operation(summary="delete movie")
	//@ApiOperation("delete movie(Admin only)")
	public ResponseEntity<String>  deleteMovie(@PathVariable String movieName){
		 List<Movie> availableMovies = movieService.findByMovieName(movieName);
	        if(availableMovies.isEmpty()){
	            throw new MoviesNotFound("No movies Available with moviename "+ movieName);
	        }
	        else {
	            movieService.deleteByMovieName(movieName);
	           // kafkaTemplate.send(topic.name(),"Movie Deleted by the Admin. "+movieName+" is now not available");
	            return new ResponseEntity<>("Movie deleted successfully",HttpStatus.OK);
	        }
	}
	
//	@PostMapping("add/tickets")
//	public ResponseEntity addTickets(@RequestBody Ticket ticket){
//		
//		movieService.saveTikcet(ticket);
//		return new ResponseEntity<>(HttpStatus.ACCEPTED);
//	}
	
//	@PostMapping("/add")
//	public ResponseEntity addMovie(@RequestBody Movie movie){
//		
//		List<Movie> movieList=movieService.getAllMovies();
//		
//		if(movieList.isEmpty()) {
//			
//		}
//		movieService.saveMovie(movie);
//		return new ResponseEntity<>(HttpStatus.ACCEPTED);
//	}
	
	
}
