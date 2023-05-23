package com.cts.rbp.movieapp.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value= "movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

	@Id
	private String id;
	
	private String movieName;
	
	private String theaterName;
	
	private Integer noOfTicketsAvailable;
	
	private String  ticketStatus;


	public Movie(String movieName, String theaterName, Integer noOfTicketsAvailable, String ticketStatus) {
		this.movieName = movieName;
		this.theaterName = theaterName;
		this.noOfTicketsAvailable = noOfTicketsAvailable;
		this.ticketStatus = ticketStatus;
	}

	public Movie(String id, String movieName, String theaterName, Integer noOfTicketsAvailable) {
		this.id = id;
		this.movieName = movieName;
		this.theaterName = theaterName;
		this.noOfTicketsAvailable = noOfTicketsAvailable;
	}

	public Movie(String movieName, String theaterName, Integer noOfTicketsAvailable) {
		this.movieName = movieName;
		this.theaterName = theaterName;
		this.noOfTicketsAvailable = noOfTicketsAvailable;
	}

	public Movie(String id, String movieName, String theaterName, Integer noOfTicketsAvailable, String ticketStatus) {
		super();
		this.id = id;
		this.movieName = movieName;
		this.theaterName = theaterName;
		this.noOfTicketsAvailable = noOfTicketsAvailable;
		this.ticketStatus = ticketStatus;
	}

	public Movie() {
		super();
	}
	
	
	
	
}
