package com.lancesoft.omg.serviceImpl;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OtpGenerator {
	int otp;
	String phoneNum;
	@Autowired
	private HttpSession httpSession;

	public int generateOtp(String phoneNum) {

		this.phoneNum = phoneNum;
		int min = 100000;
		int max = 999999;
		int otp = (int) (Math.random() * (max - min + 1) + min);
		this.otp = otp;
		return otp;

	}

public void setOtp()
{
	httpSession.setAttribute("otpGntd", otp);
	httpSession.setAttribute("phoneNumber", phoneNum);
	
}
}
