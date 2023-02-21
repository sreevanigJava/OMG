package com.lancesoft.omg.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lancesoft.omg.exception.InvalidSession;
import com.lancesoft.omg.exception.NotValidOtpException;
import com.lancesoft.omg.exception.SreevaniWithNoBrainException;
import com.lancesoft.omg.exception.UserAlreadyExist;

@RestControllerAdvice
public class ExceptionHandlers {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
		Map<String, String> errorMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errorMap.put(error.getField(), error.getDefaultMessage());
		});
		return errorMap;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(SreevaniWithNoBrainException.class)
	public Map<String, String> handleInvalidArgument(SreevaniWithNoBrainException brainException) {
		Map<String, String> map = new HashMap<>();
		map.put("........Error message.....", brainException.getMessage());
		return map;
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(NotValidOtpException.class)
	public Map<String, String> handleInvalidArgument(NotValidOtpException notValidOtpException) {
		Map<String, String> map = new HashMap<>();
		map.put("........Error message.....", notValidOtpException.getMessage());
		return map;
	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UserAlreadyExist.class)
	public Map<String, String> handleInvalidArgument(UserAlreadyExist userAlreadyExist) {
		Map<String, String> map = new HashMap<>();
		map.put("........Error message.....", userAlreadyExist.getMessage());
		return map;
	}
	public Map<String ,String> handleInvalidArgument(InvalidSession invalidSession)
	{
		Map<String , String> map=new HashMap<>();
		map.put(".....Error message.....", invalidSession.getMessage());
		return map;
		
	}
	public Map<String ,String> handleInvalidArgument(com.lancesoft.omg.exception.CategoriesAreEmpty CategoriesAreEmpty)
	{
		
		Map<String ,String > map=new HashMap<>();
		map.put("....Error message", CategoriesAreEmpty.getMessage());
		return map;

	}
	/*
	 * @ResponseStatus(HttpStatus.BAD_REQUEST)
	 * 
	 * @ExceptionHandler(NotValidOtpException.class) public Map<String, String>
	 * handleInvalidArgument(NotValidOtpException exception) { Map<String, String>
	 * map = new HashMap<>(); map.put("...Invalid..", exception.getMessage());
	 * return map;
	 * 
	 * }
	 * 
	 * @ResponseStatus(HttpStatus.BAD_REQUEST)
	 * 
	 * @ExceptionHandler(NotValidOtpException.class) public Map<String, String>
	 * handleInvalidArgument(UserAlreadyExist userAlreadyExist) { Map<String,
	 * String> map = new HashMap<>(); map.put("...user exist",
	 * userAlreadyExist.getMessage()); return map;
	 * 
	 * }
	 */
}
