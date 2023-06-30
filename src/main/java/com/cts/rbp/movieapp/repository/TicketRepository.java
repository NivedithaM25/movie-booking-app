package com.cts.rbp.movieapp.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.cts.rbp.movieapp.model.Movie;
import com.cts.rbp.movieapp.model.Ticket;

public interface TicketRepository extends MongoRepository<Ticket, String>{

	
	List<Ticket> findByMovieName(String movieName);

	 @Query(value = "{'movieName' : ?0,'theaterName' : ?1}", fields = "{id:0, seatNumber:1}")
	List<Ticket> findSeats(String movieName, String theaterNmae);

	List<Ticket> findById(ObjectId id);
	
	@Query("{movieName : ?0,theaterName : ?1}")
	List<Ticket> findByMovieNameAndTheaterName(String movieName, String theaterName);

}
