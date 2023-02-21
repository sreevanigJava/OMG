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

import com.lancesoft.omg.dao.UserRegistrationDao;
import com.lancesoft.omg.dto.UserRegistrationDto;
import com.lancesoft.omg.entity.Authorities;
import com.lancesoft.omg.entity.ChangePasswordEntity;
import com.lancesoft.omg.entity.UserRegistrationEntity;
import com.lancesoft.omg.exception.InvalidEnteredPassword;
import com.lancesoft.omg.exception.InvalidSession;
import com.lancesoft.omg.exception.NotValidOtpException;
import com.lancesoft.omg.exception.UserAlreadyExist;
import com.lancesoft.omg.security.JwtUtil;
import com.lancesoft.omg.service.UserRegistrationService;

@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {
	@Autowired
	private UserRegistrationDao userregistrationDao;

	@Autowired
	private HttpSession httpSession;
	@Autowired
	private OtpGenerator otpGenerator;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public UserRegistrationEntity save(UserRegistrationDto userRegistrationDto) {
		ModelMapper modelMapper = new ModelMapper();
		UserRegistrationEntity userRegistrationEntity = new UserRegistrationEntity();
		otpGenerator.setOtp();
		if (!validateOTP(userRegistrationDto.getPhoneNumber(), userRegistrationDto.getUserOtp())) {
			throw new NotValidOtpException("Invalid otp or phone number");
		} else
			modelMapper.map(userRegistrationDto, userRegistrationEntity);
		if (userregistrationDao.existsByUserName(userRegistrationEntity.getUserName())
				|| userregistrationDao.existsByPhoneNumber(userRegistrationEntity.getPhoneNumber())) {
			throw new UserAlreadyExist("UserName or Phone number is already exists");
		}

		Authorities authorities = new Authorities();
		authorities.setRole("USER");
		List<Authorities> authority = new ArrayList<Authorities>();
		authority.add(authorities);
		userRegistrationEntity.setAuthorities(authority);

		if (!(userRegistrationEntity.getPassword().equals(userRegistrationEntity.getConfirmPassword()))) {
			throw new InvalidEnteredPassword("Password and confirm password must be match");
		}
		userRegistrationEntity.setPassword(passwordEncoder.encode(userRegistrationEntity.getPassword()));
		userRegistrationEntity.setConfirmPassword(passwordEncoder.encode(userRegistrationEntity.getConfirmPassword()));

		return userregistrationDao.save(userRegistrationEntity);
	}

	public boolean validateOTP(String phoneNumber, Integer otpNumber) {

		Integer cacheOTP = (Integer) httpSession.getAttribute("otpGntd");

		String phoneNum = (String) httpSession.getAttribute("phoneNumber");
		System.out.println(phoneNum);
		if (cacheOTP != null && cacheOTP.equals(otpNumber) && phoneNumber.equals(phoneNum)) {
			httpSession.invalidate();
			return true;

		}
		return false;
	}

	public UserRegistrationEntity getUser(String userName) {

		return userregistrationDao.findByUserName(userName);

	}

	public String getMyToken(HttpServletRequest httpServletRequest) {
		String authorizationHeader = httpServletRequest.getHeader("Authorization");

		String token = null;
		String userName = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
			if (!jwtUtil.isTokenExpired(token)) {
				userName = jwtUtil.extractUsername(token);
			} else
				throw new InvalidSession("Invalid session please login");
		} else
			throw new InvalidSession("Invalid Session Please login");

		return userName;
	}

	public UserRegistrationEntity updateUser(UserRegistrationEntity userRegistrationEntity) {
		
		return userregistrationDao.save(userRegistrationEntity);
	}

	public boolean changePassword(ChangePasswordEntity changePasswordEntity,UserRegistrationEntity userRegistrationEntity)
	{
		boolean isMatches=passwordEncoder.matches(changePasswordEntity.getOldPassword(), userRegistrationEntity.getPassword());
		if(isMatches)
		{
			if(changePasswordEntity.getNewPassword().equals(changePasswordEntity.getConfirmPassword()))
			{
				String enterNewPassword=passwordEncoder.encode(changePasswordEntity.getConfirmPassword());
				String enterConfirmPassword=passwordEncoder.encode(changePasswordEntity.getConfirmPassword());
	
				userRegistrationEntity.setPassword(enterNewPassword);
				userRegistrationEntity.setConfirmPassword(enterConfirmPassword);
				userregistrationDao.save(userRegistrationEntity);
				return true;
			}
			else
				throw new InvalidEnteredPassword("new password and confirm passsword must match");
		}else
			throw new InvalidEnteredPassword("old password must be match");
	}
	
}
