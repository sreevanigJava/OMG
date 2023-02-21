package com.lancesoft.omg.service;

import com.lancesoft.omg.dto.UserRegistrationDto;
import com.lancesoft.omg.entity.UserRegistrationEntity;

public interface UserRegistrationService{
public UserRegistrationEntity save(UserRegistrationDto userRegistrationDto);
}
