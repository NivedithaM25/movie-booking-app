package com.cts.rbp.movieapp.exception;

public class SeatAlreadyBooked extends RuntimeException{
	
	public SeatAlreadyBooked(String seatBooked) {
		super(seatBooked);
	}
	

}
