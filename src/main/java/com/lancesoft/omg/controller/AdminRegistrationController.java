package com.lancesoft.omg.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dto.AdminRegistrationDto;
import com.lancesoft.omg.entity.AdminRegistrationEntity;
import com.lancesoft.omg.entity.ChangePasswordEntity;
import com.lancesoft.omg.entity.SmsEntity;
import com.lancesoft.omg.entity.UserRegistrationEntity;
import com.lancesoft.omg.serviceImpl.AdminRegistrationServiceImpl;
import com.lancesoft.omg.serviceImpl.SmsService;

import net.bytebuddy.asm.Advice.Return;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")

public class AdminRegistrationController {
	private final String TOPIC_DESTINATION = "/lesson/sms";
	@Autowired
	private SmsService service;
	@Autowired
	private SimpMessagingTemplate websocket;
	@Autowired
	private AdminRegistrationServiceImpl adminRegistrationServiceImpl;

	@PostMapping("/admin/sendOtp")
	public ResponseEntity<Boolean> smsSubmit(@RequestBody SmsEntity sms) {
		try {
			service.send(sms);
		} catch (Exception e) {
			return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		websocket.convertAndSend(TOPIC_DESTINATION, getTimeStamp() + ": SMS has been sent !: " + sms.getPhoneNumber());
		return new ResponseEntity<>(true, HttpStatus.OK);
	}

	private String getTimeStamp() {

		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
	}

	@PostMapping("/admin/register")
	public ResponseEntity<UserRegistrationEntity> register(
			@RequestBody @Valid AdminRegistrationDto adminRegistrationDto) {
		return new ResponseEntity(adminRegistrationServiceImpl.saveUser(adminRegistrationDto), HttpStatus.CREATED);

	}

	@GetMapping("/adminProfile")
	public ResponseEntity<AdminRegistrationEntity> getAdminProfile(HttpServletRequest httpServletRequest) {
		String userName = adminRegistrationServiceImpl.getMyToken(httpServletRequest);

		return new ResponseEntity(adminRegistrationServiceImpl.getAdmin(userName), HttpStatus.OK);

	}

	@PostMapping("/updateAdmin")
	public AdminRegistrationEntity updateAdmin(@RequestBody AdminRegistrationEntity adminRegistrationEntity) {
		return adminRegistrationServiceImpl.updateAdmin(adminRegistrationEntity);

	}

	@PostMapping("/changePassword")
	public String changePassword(@RequestBody ChangePasswordEntity changePasswordEntity,
			HttpServletRequest httpServletRequest) {
		String userName = adminRegistrationServiceImpl.getMyToken(httpServletRequest);

		AdminRegistrationEntity adminRegistrationEntity = adminRegistrationServiceImpl.getAdmin(userName);
		boolean passwordChanged = adminRegistrationServiceImpl.changePassword(adminRegistrationEntity,
				changePasswordEntity);

		if (passwordChanged) {
			return "Password has been changed";
		} else
			return "Error in changing password";
	}
}
