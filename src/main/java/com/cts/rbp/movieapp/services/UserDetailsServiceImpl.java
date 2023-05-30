package com.cts.rbp.movieapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.rbp.movieapp.model.User;
import com.cts.rbp.movieapp.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	 @Autowired
	    UserRepository userRepository;

	    @Override
	    @Transactional
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        User user = userRepository.findByLoginId(username)
	                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

	        return UserDetailsImpl.build(user);
	    }

}
