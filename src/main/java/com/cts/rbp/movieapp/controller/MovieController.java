package com.cts.rbp.movieapp.controller;

import java.util.List;
import java.util.Optional;

import org.apache.kafka.clients.admin.NewTopic;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.rbp.movieapp.exception.MoviesNotFound;
import com.cts.rbp.movieapp.model.ERole;
import com.cts.rbp.movieapp.model.Movie;
import com.cts.rbp.movieapp.model.Role;
import com.cts.rbp.movieapp.model.Ticket;
import com.cts.rbp.movieapp.model.User;
import com.cts.rbp.movieapp.payload.request.LoginRequest;
import com.cts.rbp.movieapp.repository.MovieRepository;
import com.cts.rbp.movieapp.repository.RoleRepository;
import com.cts.rbp.movieapp.repository.UserRepository;
import com.cts.rbp.movieapp.services.MovieService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
	
	@Autowired
	private UserRepository userRepository;
	
	  @Autowired
	   PasswordEncoder passwordEncoder;
	
	@Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
	
	 @Autowired
	    private NewTopic topic;
	 
	 
	 @PutMapping("/{loginId}/forgot")
	    @SecurityRequirement(name = "Bearer Authentication")
	    @Operation(summary = "reset password")
	   // @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	    public ResponseEntity<String> changePassword(@RequestBody LoginRequest loginRequest, @PathVariable String loginId){
	        //log.debug("forgot password endopoint accessed by "+loginRequest.getLoginId());
	        Optional<User> user1 = userRepository.findByLoginId(loginId);
	            User availableUser = user1.get();
	            User updatedUser = new User(
	                            loginId,
	                    availableUser.getFirstName(),
	                    availableUser.getLastName(),
	                    availableUser.getEmail(),
	                    availableUser.getContactNumber(),
	                    passwordEncoder.encode(loginRequest.getPassword())
	                    );
	            updatedUser.setId(availableUser.getId());
	            updatedUser.setRoles(availableUser.getRoles());
	            userRepository.save(updatedUser);
	            //log.debug(loginRequest.getLoginId()+" has password changed successfully");
	            return new ResponseEntity<>("Users password changed successfully",HttpStatus.OK);
	    }

	 
	@GetMapping("/all")
	 @SecurityRequirement(name = "Bearer Authentication")
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
	           //kafkaTemplate.send(topic.name(),"Movie Deleted by the Admin. "+movieName+" is now not available");
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
	@Autowired
	RoleRepository roleRepo;
	
	@GetMapping("/addrole")
	public ResponseEntity<?> addRoles(){
		Role admin = new Role(ERole.ROLE_ADMIN);
		Role user = new Role(ERole.ROLE_USER);

		roleRepo.saveAll(List.of(admin,user));
		
		return new ResponseEntity(roleRepo.findAll(),HttpStatus.OK);
	}
}
