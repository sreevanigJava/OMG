package com.lancesoft.omg.service;

import com.lancesoft.omg.dto.AdminRegistrationDto;
import com.lancesoft.omg.entity.AdminRegistrationEntity;

public interface AdminRegistrationService {
public AdminRegistrationEntity saveUser(AdminRegistrationDto adminRegistrationDto);
}
