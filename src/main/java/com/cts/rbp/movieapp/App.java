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

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
public class App {

	
    public static void main(String[] args) {
    	
        SpringApplication.run(App.class, args);
    }
    
  
}
