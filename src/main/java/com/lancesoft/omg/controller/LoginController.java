package com.lancesoft.omg.controller;


import java.util.Iterator;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.servlet.oauth2.resourceserver.JwtDsl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dao.UserRegistrationDao;
import com.lancesoft.omg.dto.JwtToken;
import com.lancesoft.omg.entity.Authorities;
import com.lancesoft.omg.entity.CurrentUserDetails;
import com.lancesoft.omg.entity.LoginRequest;
import com.lancesoft.omg.entity.UserRegistrationEntity;
import com.lancesoft.omg.security.JwtUtil;

@RestController()
@RequestMapping("/api")
@CrossOrigin("*")
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserRegistrationDao userRegistrationDao;
	
	@PostMapping("/login")
	public ResponseEntity<CurrentUserDetails> login(@RequestBody LoginRequest loginRequest,CurrentUserDetails currentUserDetails)
	{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
			
		} catch(Exception exception)
		{
			throw new ValidationException("Invalid");
		}
		JwtToken jwtToken=new JwtToken();
		jwtToken.setJwtToken(jwtUtil.generateToken(loginRequest.getUserName()));
		
		UserRegistrationEntity userRegistrationEntity=userRegistrationDao.findByUserName(loginRequest.getUserName());
		currentUserDetails.setUserName(userRegistrationEntity.getUserName());
	
		Iterator iterator=userRegistrationEntity.getAuthorities().iterator();
		
		Authorities authorities=(Authorities) iterator.next();
		currentUserDetails.setRole(authorities.getRole());
		currentUserDetails.setToken(jwtToken);
		return new ResponseEntity<CurrentUserDetails>(currentUserDetails,HttpStatus.OK);
		
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

