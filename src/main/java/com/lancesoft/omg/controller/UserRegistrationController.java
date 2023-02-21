package com.lancesoft.omg.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dto.UserRegistrationDto;
import com.lancesoft.omg.entity.AdminRegistrationEntity;
import com.lancesoft.omg.entity.ChangePasswordEntity;
import com.lancesoft.omg.entity.SmsEntity;
import com.lancesoft.omg.entity.UserRegistrationEntity;
import com.lancesoft.omg.serviceImpl.SmsService;
import com.lancesoft.omg.serviceImpl.UserRegistrationServiceImpl;

@RestController
@RequestMapping("/api")

public class UserRegistrationController {
	@Autowired
	UserRegistrationServiceImpl userRegistrationServiceImpl;
	@Autowired
	SmsService smsService;

	@PostMapping("/userRegister")
	public UserRegistrationEntity save(@RequestBody @Valid UserRegistrationDto registrationDto) {
		return userRegistrationServiceImpl.save(registrationDto);

	}

	@PostMapping("/user/sendotp")
	public ResponseEntity<Boolean> sendOtp(@RequestBody SmsEntity smsEntity) {
		try {
			smsService.send(smsEntity);

		} catch (Exception e) {
			return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	@GetMapping("/user/myProfile")
	public UserRegistrationEntity getDetails(HttpServletRequest httpServletRequest) {
		String userName = userRegistrationServiceImpl.getMyToken(httpServletRequest);
		return userRegistrationServiceImpl.getUser(userName);

	}
	@PostMapping("/updateUser")
	public ResponseEntity<UserRegistrationEntity> updateUser(@RequestBody UserRegistrationEntity userRegistrationEntity)
	{
	return new ResponseEntity(userRegistrationServiceImpl.updateUser(userRegistrationEntity),HttpStatus.OK);
	}
	@PostMapping("/user/changePassword")
	public String changePassword(@RequestBody ChangePasswordEntity changePasswordEntity,HttpServletRequest httpServletRequest) 
	{
		String userName=userRegistrationServiceImpl.getMyToken(httpServletRequest);
		UserRegistrationEntity userRegistrationEntity=userRegistrationServiceImpl.getUser(userName);
		boolean passwordChanged=userRegistrationServiceImpl.changePassword(changePasswordEntity, userRegistrationEntity);
		if(passwordChanged)
		{
			return "password has been changed";
		}else
		return "Error in changing password";
		
	}
	
	
	
	
	
	
	
	
}
