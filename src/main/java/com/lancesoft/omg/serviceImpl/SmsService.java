package com.lancesoft.omg.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.entity.SmsEntity;
import com.lancesoft.omg.exception.SreevaniWithNoBrainException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {
	private final String ACCOUNT_SID = "ACc173552c03815035e936c5780f438118";
	private final String AUTH_TOKEN = "1e02475c217d97025a251d99cc1aac68";
	private final String FROM_NUMBER = "+15106169612";
	@Autowired
	OtpGenerator otpGenerator;

	public void send(SmsEntity sms) {
		try {
			Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
			
		Integer Otp = otpGenerator.generateOtp(sms.getPhoneNumber());
		System.out.println(sms.getPhoneNumber()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		String msg = "Your OTP -" + Otp + " Please verify this otp in your application for reset password";
		MessageCreator creater = Message.creator(new PhoneNumber(sms.getPhoneNumber()),new PhoneNumber(FROM_NUMBER), msg);creater.create();
		}
		catch (Exception ex) {
			System.out.println("Unable to send Otp");
			throw new SreevaniWithNoBrainException("Unable to send Otp ");
		}
		
	
	}
}
