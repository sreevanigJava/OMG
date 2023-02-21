package com.lancesoft.omg.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.dao.AdminRegistrationDao;
import com.lancesoft.omg.dao.MyCartDao;
import com.lancesoft.omg.dto.AdminRegistrationDto;
import com.lancesoft.omg.entity.AdminRegistrationEntity;
import com.lancesoft.omg.entity.Authorities;
import com.lancesoft.omg.entity.ChangePasswordEntity;
import com.lancesoft.omg.entity.MyCart;
import com.lancesoft.omg.entity.SmsEntity;
import com.lancesoft.omg.exception.InvalidEnteredPassword;
import com.lancesoft.omg.exception.InvalidSession;
import com.lancesoft.omg.exception.NotValidOtpException;
import com.lancesoft.omg.exception.UserAlreadyExist;
import com.lancesoft.omg.security.JwtUtil;
import com.lancesoft.omg.service.AdminRegistrationService;
import com.twilio.rest.lookups.v1.PhoneNumber;

@Service
public class AdminRegistrationServiceImpl implements AdminRegistrationService {
	@Autowired
	private OtpGenerator otpGenerator;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AdminRegistrationDao adminRegistrationDao;
	@Autowired
	private SmsService smsService;
	@Autowired
	private HttpSession httpSession;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MyCartDao myCartDao;
	@Override
	public AdminRegistrationEntity saveUser(AdminRegistrationDto adminRegistrationDto) {
		ModelMapper modelMapper = new ModelMapper();
		AdminRegistrationEntity adminRegistrationEntity = new AdminRegistrationEntity();
		otpGenerator.setOtp();
		if (!validateOTP(adminRegistrationDto.getPhoneNumber(), adminRegistrationDto.getUserOtp())) {
			throw new NotValidOtpException("please enter a valid OTP");
		} else {
			if (adminRegistrationDto == null)
				throw new RuntimeException("null found in registration plss check");

			else

				modelMapper.map(adminRegistrationDto, adminRegistrationEntity);

			if (adminRegistrationDao.existsByUserName(adminRegistrationEntity.getUserName())
					|| adminRegistrationDao.existsByPhoneNumber(adminRegistrationEntity.getPhoneNumber())) {
				throw new UserAlreadyExist("UserName is already exists");
			}
			Authorities authorities = new Authorities();
			authorities.setRole("ADMIN");
			List<Authorities> authority = new ArrayList<Authorities>();
			authority.add(authorities);
			adminRegistrationEntity.setAuthorities(authority);
			if (!(adminRegistrationEntity.getPassword().equals(adminRegistrationEntity.getConfirmPassword()))) {
				throw new InvalidEnteredPassword("Password and confirm password must be match");
			}
			adminRegistrationEntity.setPassword(passwordEncoder.encode(adminRegistrationEntity.getPassword()));
			adminRegistrationEntity
					.setConfirmPassword(passwordEncoder.encode(adminRegistrationEntity.getConfirmPassword()));

			return adminRegistrationDao.save(adminRegistrationEntity);
		}
	}

	private boolean validateOTP(String phoneNumber, Integer userEnterdOtpNumber) {
		Integer cacheOTP = (Integer) httpSession.getAttribute("otpGntd");

		String phoneNum = (String) httpSession.getAttribute("phoneNumber");

		if (cacheOTP != null && cacheOTP.equals(userEnterdOtpNumber) && phoneNumber.equals(phoneNum)) {
			httpSession.invalidate();

			return true;
		}

		return false;
	}

	public AdminRegistrationEntity getAdmin(String userName) {
		return adminRegistrationDao.findByUserName(userName);
	}

	public String getMyToken(HttpServletRequest httpServletRequest) {

		String authroizationHeader = httpServletRequest.getHeader("Authorization");

		String token = null;
		String userName = null;

		if (authroizationHeader != null && authroizationHeader.startsWith("Bearer ")) {
			token = authroizationHeader.substring(7);
			if (!jwtUtil.isTokenExpired(token)) {
				userName = jwtUtil.extractUsername(token);

			} else
				throw new InvalidSession("Invalid session please login");
		} else
			throw new InvalidSession("Invalid session please login");

		return userName;
	}

	public AdminRegistrationEntity updateAdmin(AdminRegistrationEntity adminRegistrationEntity) {

		return adminRegistrationDao.save(adminRegistrationEntity);
	}

	public boolean changePassword(AdminRegistrationEntity adminRegistrationEntity,
			ChangePasswordEntity changePasswordEntity) {

		//String oldPassword = adminRegistrationEntity.getPassword();
		//String enterOldPassword = passwordEncoder.encode(changePasswordEntity.getOldPassword());
		boolean isMatches = passwordEncoder.matches(changePasswordEntity.getOldPassword(), adminRegistrationEntity.getPassword());

		if (isMatches) {
			if (changePasswordEntity.getNewPassword().equals(changePasswordEntity.getConfirmPassword())) {
				String enterNewPassword = passwordEncoder.encode(changePasswordEntity.getNewPassword());
				String enterConfirmPassword = passwordEncoder.encode(changePasswordEntity.getConfirmPassword());

				adminRegistrationEntity.setPassword(enterNewPassword);
				adminRegistrationEntity.setConfirmPassword(enterNewPassword);
				adminRegistrationDao.save(adminRegistrationEntity);
				return true;
			} else
				throw new InvalidEnteredPassword("new password and confirm password must be match");
		} else
			throw new InvalidEnteredPassword("old password must be match");
	}



}
