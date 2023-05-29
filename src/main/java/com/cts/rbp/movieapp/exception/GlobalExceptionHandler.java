package com.cts.rbp.movieapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends Exception{
	
	@ExceptionHandler
	public ResponseEntity<String> inCaseOFMoviesNotFound(Exception e){
		
		return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
	}
	

		@ExceptionHandler
		public ResponseEntity<String> inCaseOfSeatAlreadyBooked(Exception e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_FOUND);
		}
}
