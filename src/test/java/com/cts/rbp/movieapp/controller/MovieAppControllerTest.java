package com.cts.rbp.movieapp.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cts.rbp.movieapp.model.Movie;
import com.cts.rbp.movieapp.model.Ticket;
import com.cts.rbp.movieapp.services.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class MovieAppControllerTest {

	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

//    @MockBean
//    private KafkaTemplate kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getAllMoviesAndNoneFound() throws Exception {
        authenticateUser();
        // Use MockMvc to send a GET request to the /api/v1.0/moviebooking/all endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/moviebooking/all"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllMoviesAndFound() throws Exception {
        authenticateUser();
        List<Movie> movies = new ArrayList<>();
        Movie movie1 = new Movie("Movie 1", "Theatre 1", 120, "Book now");
        Movie movie2 = new Movie("Movie 2", "Theatre 2", 150, "Book now");
        movies.add(movie1);
        movies.add(movie2);

        when(movieService.getAllMovies()).thenReturn(movies);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/moviebooking/all"))
                .andExpect(status().isFound());
    }

    @Test
    public void testGetMovieByNameAndFound() throws Exception {
        authenticateUser();
        String movieName = "Movie";
        List<Movie> movieList = new ArrayList<>();
        movieList.add(new Movie("Movie", "Theatre 1", 120, "Book now"));
        when(movieService.getMovieByName(movieName)).thenReturn(movieList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/moviebooking/movies/search/{movieName}", movieName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    public void testGetMovieByNameAndNotFound() throws Exception {
        authenticateUser();
        String movieName = "Movie";
        List<Movie> movieList = new ArrayList<>();
        when(movieService.getMovieByName(movieName)).thenReturn(movieList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/moviebooking/movies/search/{movieName}", movieName))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllBookedTickets() throws Exception {
        // Mock the ticket list that will be returned by the service method
        authenticateAdmin();
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(new Ticket("The Matrix", "Screen 1", 2, new ArrayList<String>(List.of("a1","a2"))));
        when(movieService.getALlBookedTickets("Movie1")).thenReturn(ticketList);

        // Perform GET request to endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1.0/moviebooking/getallbookedtickets/Movie1"))
                .andExpect(status().isOk());

    }

    private void authenticateUser(){
        // Mock the authentication process
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "testUser", "testPassword", List.of(new SimpleGrantedAuthority("ROLE_USER"))
                )
        );
    }
    private void authenticateAdmin(){
        // Mock the authentication process
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "testUser", "testPassword", List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                )
        );
    }
}
