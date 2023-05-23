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

	@Override
	public String toString() {
		return "Movie [id=" + id + ", movieName=" + movieName + ", theaterName=" + theaterName
				+ ", noOfTicketsAvailable=" + noOfTicketsAvailable + ", ticketStatus=" + ticketStatus + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public String getTheaterName() {
		return theaterName;
	}

	public void setTheaterName(String theaterName) {
		this.theaterName = theaterName;
	}

	public Integer getNoOfTicketsAvailable() {
		return noOfTicketsAvailable;
	}

	public void setNoOfTicketsAvailable(Integer noOfTicketsAvailable) {
		this.noOfTicketsAvailable = noOfTicketsAvailable;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}
	
	
	
	
}
