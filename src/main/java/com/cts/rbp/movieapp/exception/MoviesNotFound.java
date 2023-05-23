package com.cts.rbp.movieapp.exception;

public class MoviesNotFound extends RuntimeException{
	
	public MoviesNotFound(String noMoviesFound) {
		super(noMoviesFound);
	}

}
