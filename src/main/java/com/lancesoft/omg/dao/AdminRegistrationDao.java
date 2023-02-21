package com.lancesoft.omg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.AdminRegistrationEntity;

public interface AdminRegistrationDao extends JpaRepository<AdminRegistrationEntity, Integer>{
boolean existsByUserName(String userName);
boolean existsByPhoneNumber(String phoneNumber);
AdminRegistrationEntity findByUserName(String userName);
}
