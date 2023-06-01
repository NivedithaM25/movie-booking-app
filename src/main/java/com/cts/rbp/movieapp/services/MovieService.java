package com.cts.rbp.movieapp.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.kafka.clients.admin.NewTopic;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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

	final static Logger logger = LoggerFactory.getLogger(MovieService.class);

	@Autowired
	private MovieRepository movieRepo;

	@Autowired
	private TicketRepository ticketRepo;

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private NewTopic topic;

	public List<Movie> getAllMovies() {
		return movieRepo.findAll();
	}

	public void saveMovie(Movie movie) {
		movieRepo.save(movie);
	}

	public List<Movie> getMovieByName(String movieName) {

		return movieRepo.findByMovieName(movieName);
	}

	public List<Ticket> getALlBookedTickets(String movieName) {
		return ticketRepo.findByMovieName(movieName);
	}

	public void saveTikcet(Ticket ticket) {
		ticketRepo.save(ticket);
	}

	public ResponseEntity<String> bookTickets(Ticket ticket, String movieName) {
		List<Ticket> allTickets = findSeats(movieName, ticket.getTheaterName());
//	        for(Ticket each : allTickets){
//	            for(int i = 0; i < ticket.getNoOfTickets(); i++){
//	                if(each.getSeatNumber().contains(ticket.getSeatNumber().get(i))){
//	                    //log.debug("seat is already booked");
//	                    throw new SeatAlreadyBooked("Seat number "+ticket.getSeatNumber().get(i)+" is already booked");
//	                }
//	            }
//	        }

		List<String> seatNumbers = ticket.getSeatNumber();
		int numTickets = ticket.getNoOfTickets();
		if (seatNumbers.size() < numTickets) {
			throw new IllegalArgumentException("Not enough seat numbers provided");
		}

		for (Ticket each : allTickets) {
			for (int i = 0; i < numTickets; i++) {
				if (each.getSeatNumber().contains(seatNumbers.get(i))) {
					throw new SeatAlreadyBooked("Seat number " + seatNumbers.get(i) + " is already booked");
				}
			}
		}
		logger.info(movieName + " " + ticket.getTheaterName());
		// Movie availableTickets=findMovieByMovieNameAndTheatreName(movieName,
		// ticket.getMovieName());
		
		logger.info(findAvailableTickets(movieName, ticket.getTheaterName()).toString());
		int availableTickets = findAvailableTickets(movieName, ticket.getTheaterName()).get(0)
				.getNoOfTicketsAvailable();
		
//		if((findAvailableTickets(movieName, ticket.getThaterName()).get(0).getNoOfTicketsAvailable()) >= 
//				ticket.getNoOfTickets()) {
		if (availableTickets >= ticket.getNoOfTickets()) {

			saveTikcet(ticket);
			//kafkaTemplate.send(topic.name(), "Movie ticket booked. " + "Booking Details are: " + ticket);

			List<Movie> movies = getMovieByName(movieName);
			int available_tickets = 0;
			for (Movie movie : movies) {
				available_tickets = movie.getNoOfTicketsAvailable() - ticket.getNoOfTickets();
				movie.setNoOfTicketsAvailable(available_tickets);
				saveMovie(movie);
			}

			return new ResponseEntity<>("Ticket Booked Successfully with seat number " + ticket.getSeatNumber(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>("All Tickets Sold out", HttpStatus.OK);
		}

	}

	public List<Ticket> findSeats(String movieName, String theatreName) {
		return ticketRepo.findSeats(movieName, theatreName);
	}

	public List<Movie> findAvailableTickets(String movieName, String theatername) {
		return movieRepo.findAvailableTickets(movieName, theatername);
	}

//	public Movie findMovieByMovieNameAndTheatreName(String movieName,String theatername){
//		return movieRepo.findByMovieNameAndTheaterName(movieName,theatername).orElse(null);
//	}

	public String updateTicketStatus(String movieName, ObjectId ticket) {
		List<Movie> movie = movieRepo.findByMovieName(movieName);
		List<Ticket> tickets = ticketRepo.findById(ticket);

		if (movie == null) {
			throw new MoviesNotFound("Movie not found: " + movieName);
		}
		if (tickets == null) {
			throw new NoSuchElementException("Ticket Not Found: " + ticket);
		}

		int ticketsBooked = getTotalNoTicketz(movieName);
		for (Movie movies : movie) {
			if (ticketsBooked >= movies.getNoOfTicketsAvailable()) {
				movies.setTicketStatus("SOLD OUT");
			} else {
				movies.setTicketStatus("BOOK AS SOON AS POSSIBLE");
			}

			saveMovie(movies);
		}

		// kafka impl
		//kafkaTemplate.send(topic.name(), "tickets status upadated by the Admin for movie " + movieName);
		return "Ticket status updated successfully";
	}

	private int getTotalNoTicketz(String movieName) {
		List<Ticket> tickets = ticketRepo.findByMovieName(movieName);
		int totaltickets = 0;
		for (Ticket ticket : tickets) {
			totaltickets = totaltickets + ticket.getNoOfTickets();
		}
		// TODO Auto-generated method stub
		return totaltickets;
	}

	public void deleteByMovieName(String movieName) {
		movieRepo.deleteByMovieName(movieName);
	}

	public List<Movie> findByMovieName(String movieName) {
		return movieRepo.findByMovieName(movieName);
	}
}
