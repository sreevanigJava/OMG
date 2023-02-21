package com.lancesoft.omg.entity;

import com.lancesoft.omg.dto.JwtToken;

import lombok.Data;

@Data
public class CurrentUserDetails {

	private String userName;
	private String role;
	private JwtToken token;
}
