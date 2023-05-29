package com.cts.rbp.movieapp.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.rbp.movieapp.exception.MoviesNotFound;
import com.cts.rbp.movieapp.exception.SeatAlreadyBooked;
import com.cts.rbp.movieapp.model.Movie;
import com.cts.rbp.movieapp.model.Ticket;
import com.cts.rbp.movieapp.repository.MovieRepository;
import com.cts.rbp.movieapp.repository.TicketRepository;

@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepo;
	
	@Autowired
	private TicketRepository ticketRepo;
	
	public List<Movie> getAllMovies(){
		return movieRepo.findAll();
	}
	
	public void saveMovie(Movie movie) {
		movieRepo.save(movie);
	}
	
	
	public List<Movie> findByMovieName(String movieName) {
		
		return movieRepo.findByMovieName(movieName);
	}
	
	
	public List<Ticket> getALlBookedTickets(String movieName) {
		return ticketRepo.findByMovieName(movieName);
	}
	public void saveTikcet(Ticket ticket) {
		ticketRepo.save(ticket);
	}

	public ResponseEntity<String> bookTickets(Ticket ticket, String movieName) {
		List<Ticket> allTickets= findSeats(movieName,ticket.getThaterName());
		for(Ticket each:allTickets) {
			for(int i=0;i<ticket.getNoOfTickets();i++) {
				if(each.getSeatNumber().contains(ticket.getSeatNumber().get(i)));
					throw new SeatAlreadyBooked("Seat Number "+ticket.getSeatNumber().get(i)+" is already booked!");
			}
		}
		
		if(findAvailableTickets(movieName, ticket.getThaterName()).get(0).getNoOfTicketsAvailable() >= 
				ticket.getNoOfTickets()) {
			saveTikcet(ticket);
			//kafka implementation
			
			List<Movie> movies=findByMovieName(movieName);
			int available_tickets=0;
			for(Movie movie:movies) {
				available_tickets=movie.getNoOfTicketsAvailable()-ticket.getNoOfTickets();
				movie.setNoOfTicketsAvailable(available_tickets);
				saveMovie(movie);
			}
			
			return new ResponseEntity<>("Ticket Booked Successfully with seat number "+ticket.getSeatNumber(),HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>("All Tickets Sold out",HttpStatus.OK);
		}
		
		
	}

	private List<Ticket> findSeats(String movieName, String theaterNmae) {
			
		return ticketRepo.findSeats(movieName,theaterNmae);
	}
	
	
	private List<Movie> findAvailableTickets(String movieName,String theatername){
		return movieRepo.findAvailableTickets(movieName,theatername);
	}

	public String updateTicketStatus(String movieName, ObjectId ticket) {
		List<Movie> movie=movieRepo.findByMovieName(movieName);
		List<Ticket> tickets=ticketRepo.finBy_id(ticket);
		
		if(movie == null) {
			throw new MoviesNotFound("Movie not found: "+movieName);
		}
		if(tickets ==null) {
			throw new NoSuchElementException("Ticket Not Found: "+ticket);
		}
		
		int ticketsBooked = getTotalNoTicketz(movieName);
		for(Movie movies:movie) {
			if(ticketsBooked >= movies.getNoOfTicketsAvailable()) {
				movies.setTicketStatus("SOLD OUT");
			}else {
				movies.setTicketStatus("BOOK AS SOON AS POSSIBLE");
			}
			
			saveMovie(movies);
		}
		
		//kafka impl
		
		return "Ticket status updated successfully";
	}

	private int getTotalNoTicketz(String movieName) {
		List<Ticket> tickets=ticketRepo.findByMovieName(movieName);
		int totaltickets=0;
		for(Ticket ticket:tickets) {
			totaltickets = totaltickets+ticket.getNoOfTickets();
		}
		// TODO Auto-generated method stub
		return totaltickets;
	}

	public String deleteTicket(String movieName) {
		// TODO Auto-generated method stub
		return null;
	}
}
