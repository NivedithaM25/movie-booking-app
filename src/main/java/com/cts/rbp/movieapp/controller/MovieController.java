package com.cts.rbp.movieapp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.kafka.clients.admin.NewTopic;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins="http://localhost:3000")
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
			return new ResponseEntity<>(movieList,HttpStatus.OK);
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
		return new ResponseEntity<>(movieList,HttpStatus.OK);
	}
	
	
	
	@PostMapping("/{movieName}/add")
	@Operation(summary="Book tickets for a movie")
	public ResponseEntity<String> bookTickets(@RequestBody Ticket ticket,@PathVariable("movieName") String movieName){
		
		return movieService.bookTickets(ticket,movieName);
	}
	
	//Admin Access
	@GetMapping("/getallbookedtickets/{movieName}")
	//@ApiOperation("get all booked tickets (Admin only)")
	public ResponseEntity<List<Ticket>> getAllBookedTickets(@PathVariable String movieName){
		return new ResponseEntity<>(movieService.getALlBookedTickets(movieName),HttpStatus.OK);
	}
	
	@PutMapping("/{movieName}/update")
	@Operation(summary="Update ticket status")
	public ResponseEntity<String> upadteTicketStatus(@PathVariable String movieName,@RequestBody ObjectId ticket){
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
	           kafkaTemplate.send(topic.name(),"Movie Deleted by the Admin. "+movieName+" is now not available");
	            return new ResponseEntity<>("Movie deleted successfully",HttpStatus.OK);
	        }
	}
	
//	@PostMapping("add/tickets")
//	public ResponseEntity addTickets(@RequestBody Ticket ticket){
//		
//		movieService.saveTikcet(ticket);
//		return new ResponseEntity<>(HttpStatus.ACCEPTED);
//	}
	
	@PostMapping("/add")
	public ResponseEntity addMovie(@RequestBody Movie movie){
		
		List<Movie> movieList=movieService.getAllMovies();
		
		if(movieList.isEmpty()) {
			
		}
		movieService.saveMovie(movie);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	@Autowired
	RoleRepository roleRepo;
	
	@GetMapping("/addrole")
	public ResponseEntity<?> addRoles(){
		Role admin = new Role(ERole.ROLE_ADMIN);
		Role user = new Role(ERole.ROLE_USER);

		roleRepo.saveAll(List.of(admin,user));
		
		return new ResponseEntity(roleRepo.findAll(),HttpStatus.OK);
	}
	
	@GetMapping("/seats/{totalSeats}")
    public List<String> seats(@PathVariable int totalSeats){
        //log.debug("SEATS Array");
        List<String> seats = new ArrayList<String>();
        
        List<String> a = Arrays.asList("A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10");
        List<String> b = Arrays.asList("B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "B10");
        List<String> c = Arrays.asList("C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10");
        List<String> d = Arrays.asList("D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10");
        List<String> e = Arrays.asList("E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "E10");
        List<String> f = Arrays.asList("F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10");
        List<String> g = Arrays.asList("G1", "G2", "G3", "G4", "G5", "G6", "G7", "G8", "G9", "G10");
        List<String> h = Arrays.asList("H1", "H2", "H3", "H4", "H5", "H6", "H7", "H8", "H9", "H10");
        List<String> i = Arrays.asList("I1", "I2", "I3", "I4", "I5", "I6", "I7", "I8", "I9", "I10");
        List<String> j = Arrays.asList("J1", "J2", "J3", "J4", "J5", "J6", "J7", "J8", "J9", "J10");
        List<String> k = Arrays.asList("K1", "K2", "K3", "K4", "K5", "K6", "K7", "K8", "K9", "K10");
        List<String> l = Arrays.asList("L1", "L2", "L3", "L4", "L5", "L6", "L7", "L8", "L9", "L10");
        List<String> m = Arrays.asList("C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10");
        List<String> n = Arrays.asList("D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10");
        List<String> o = Arrays.asList("E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "E10");
        List<String> p = Arrays.asList("A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10");
        List<String> q = Arrays.asList("B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "B10");
        List<String> r = Arrays.asList("C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10");
        List<String> s = Arrays.asList("D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10");
        List<String> t = Arrays.asList("E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "E10");
        List<String> u = Arrays.asList("A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "A10");
        List<String> v = Arrays.asList("B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "B10");
        List<String> w = Arrays.asList("C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "C10");
        List<String> x = Arrays.asList("D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10");
        List<String> y = Arrays.asList("E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "E10");
        List<List<String>> llist = Arrays.asList(a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y);
        
                
        int totalRows = totalSeats / 10;
        
        for(int row=0; row<10; ++row) {
        	for(int col=0; col<totalRows; ++col) {
        		seats.add(llist.get(col).get(row));
        	}
        }
        return seats;
    }

    
    
    @GetMapping("/bookedSeats/{movieName}/{theaterName}")
    public List<String> bookedSeats(@PathVariable String movieName, @PathVariable String theaterName ){
        //log.debug("Getting Booked Seats");
        List<String> bookedSeats = new ArrayList<String>();
        
        List<Ticket> allTickets = movieService.findSeats(movieName, theaterName);
        for(Ticket each : allTickets){ 
                bookedSeats.addAll(each.getSeatNumber());
        }
        return bookedSeats;
    }
	
}
