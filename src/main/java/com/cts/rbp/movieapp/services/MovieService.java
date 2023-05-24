package com.cts.rbp.movieapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
		// TODO Auto-generated method stub
		return ticketRepo.findByMovieName(movieName);
	}

	public void addTikcet(Ticket ticket) {
		// TODO Auto-generated method stub
		ticketRepo.save(ticket);
	}
}
