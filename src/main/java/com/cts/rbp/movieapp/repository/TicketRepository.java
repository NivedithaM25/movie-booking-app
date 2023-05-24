package com.cts.rbp.movieapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cts.rbp.movieapp.model.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String>{

	
	List<Ticket> findByMovieName(String movieName);

}
