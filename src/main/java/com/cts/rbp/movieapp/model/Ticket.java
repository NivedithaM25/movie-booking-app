package com.cts.rbp.movieapp.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="ticket")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

	private ObjectId id;
	
	private String movieName;
	private String thaterName;
	private int noOfTickets;
	private List<String> seatNumber;
	
	public Ticket(ObjectId id, String movieName, String thaterName, int noOfTickets, List<String> seatNumber) {
		this.id = id;
		this.movieName = movieName;
		this.thaterName = thaterName;
		this.noOfTickets = noOfTickets;
		this.seatNumber = seatNumber;
	}
	
	
}
