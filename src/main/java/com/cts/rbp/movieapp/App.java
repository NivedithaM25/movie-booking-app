package com.cts.rbp.movieapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cts.rbp.movieapp.model.ERole;
import com.cts.rbp.movieapp.model.Movie;
import com.cts.rbp.movieapp.model.Role;
import com.cts.rbp.movieapp.repository.MovieRepository;
import com.cts.rbp.movieapp.repository.RoleRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class App {

	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    
    public void run(String... args) throws Exception {

		mongoTemplate.dropCollection("roles");
//		mongoTemplate.dropCollection("ticket");
//		mongoTemplate.dropCollection("users");
		mongoTemplate.dropCollection("movie");

		Movie movie1 = new Movie("Dasara","Miraj",126,"Book ASAP");
	 	Movie movie2 = new Movie("Bhoola","Miraj",122,"Book ASAP");
	 	Movie movie3 = new Movie("Balagam","Konark",107,"Book ASAP");

	 	movieRepository.saveAll(List.of(movie1,movie2,movie3));

		Role admin = new Role(ERole.ROLE_ADMIN);
		Role user = new Role(ERole.ROLE_USER);

		roleRepository.saveAll(List.of(admin,user));
	}
}
