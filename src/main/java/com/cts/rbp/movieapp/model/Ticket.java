package com.cts.rbp.movieapp.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value="ticket")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

	private ObjectId id;
	
	private String movieName;
	private String theaterName;
	private int noOfTickets;
	private List<String> seatNumber;
	
	public Ticket(ObjectId id, String movieName, String theaterName, int noOfTickets, List<String> seatNumber) {
		this.id = id;
		this.movieName = movieName;
		this.theaterName = theaterName;
		this.noOfTickets = noOfTickets;
		this.seatNumber = seatNumber;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
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

	public int getNoOfTickets() {
		return noOfTickets;
	}

	public void setNoOfTickets(int noOfTickets) {
		this.noOfTickets = noOfTickets;
	}

	public List<String> getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(List<String> seatNumber) {
		this.seatNumber = seatNumber;
	}

	public Ticket(String movieName, String thaterName, int noOfTickets, List<String> seatNumber) {
		super();
		this.movieName = movieName;
		this.theaterName = thaterName;
		this.noOfTickets = noOfTickets;
		this.seatNumber = seatNumber;
	}

	public Ticket() {
		super();
	}


	
	
}
