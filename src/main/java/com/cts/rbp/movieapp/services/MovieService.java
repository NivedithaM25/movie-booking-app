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
		// logger.info(movieName + " " + ticket.getTheaterName());
		// Movie availableTickets=findMovieByMovieNameAndTheatreName(movieName,
		// ticket.getMovieName());

		// logger.info(findAvailableTickets(movieName,
		// ticket.getTheaterName()).toString());
		int availableTickets = findAvailableTickets(movieName, ticket.getTheaterName()).get(0)
				.getNoOfTicketsAvailable();

		if (availableTickets >= ticket.getNoOfTickets()) {

			saveTikcet(ticket);
			kafkaTemplate.send(topic.name(), "Movie ticket booked. " + "Booking Details are: " + ticket);
			Movie movies = findMovieByMovieNameAndTheatreName(movieName, ticket.getTheaterName());
			
			movies.setNoOfTicketsAvailable(movies.getNoOfTicketsAvailable() - ticket.getNoOfTickets());
			movies.setTicketStatus(updateTicketStatus(movies));
			saveMovie(movies);
			return new ResponseEntity<>("Ticket Booked Successfully with seat number " + ticket.getSeatNumber(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>("All Tickets Sold out", HttpStatus.OK);
		}

	}

	public String updateTicketStatus(Movie movie) {

		if (movie == null) {
			throw new MoviesNotFound("Movie not found: " + movie.getMovieName());
		}

		List<Ticket> ticketList = ticketRepo.findByMovieNameAndTheaterName(movie.getMovieName(), movie.getTheaterName());
		int ticketsBooked = 0;
		for (Ticket ticket : ticketList) {
			ticketsBooked = ticketsBooked + ticket.getNoOfTickets();
		}

		if (movie.getNoOfTicketsAvailable() == 0) {
			return "SOLD OUT";
		} else if (ticketsBooked >= movie.getNoOfTicketsAvailable()) {
			return "BOOK ASAP";
		} else {
			return "Available";
		}

	}

	public List<Ticket> findSeats(String movieName, String theatreName) {
		return ticketRepo.findSeats(movieName, theatreName);
	}

	public List<Movie> findAvailableTickets(String movieName, String theatername) {
		return movieRepo.findAvailableTickets(movieName, theatername);
	}

	public Movie findMovieByMovieNameAndTheatreName(String movieName, String theatername) {
		return movieRepo.findByMovieNameAndTheaterName(movieName, theatername);
	}

	public String updateTicketStatus(String movieName, ObjectId ticket) {
		List<Movie> movie = movieRepo.findByMovieName(movieName);
		List<Ticket> tickets = ticketRepo.findById(ticket);

		if (movie == null) {
			throw new MoviesNotFound("Movie not found: " + movieName);
		}
		if (tickets == null) {
			throw new NoSuchElementException("Ticket Not Found: " + ticket);
		}

		int ticketsBooked = getTotalNoTickets(movieName);

		for (Movie movies : movie) {
			// log.info("No. of Tickets available- " +movies.getNoOfTicketsAvailable());
			if (movies.getNoOfTicketsAvailable() == 0) {
				movies.setTicketStatus("SOLD OUT");
			} else if (ticketsBooked >= movies.getNoOfTicketsAvailable()) {
				movies.setTicketStatus("BOOK ASAP");
			} else {
				movies.setTicketStatus("Available");
			}
			saveMovie(movies);
		}

//		for (Movie movies : movie) {
//			if (ticketsBooked >= movies.getNoOfTicketsAvailable()) {
//				movies.setTicketStatus("SOLD OUT");
//			} else {
//				movies.setTicketStatus("BOOK AS SOON AS POSSIBLE");
//			}
//
//			saveMovie(movies);
//		}

		// kafka impl
		 kafkaTemplate.send(topic.name(), "tickets status upadated by the Admin for movie "+ movieName);
		return "Ticket status updated successfully";
	}

	private int getTotalNoTickets(String movieName) {
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
