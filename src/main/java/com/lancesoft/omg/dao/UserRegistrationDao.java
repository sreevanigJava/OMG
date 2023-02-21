package com.lancesoft.omg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.UserRegistrationEntity;
import com.lancesoft.omg.serviceImpl.UserRegistrationServiceImpl;

public interface UserRegistrationDao extends JpaRepository<UserRegistrationEntity, Integer>{
boolean existsByUserName(String userName);
boolean existsByPhoneNumber(String phoneNum);
UserRegistrationEntity findByUserName(String userName);
}
