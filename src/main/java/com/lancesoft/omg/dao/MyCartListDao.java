package com.lancesoft.omg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.MyCartList;

public interface MyCartListDao extends JpaRepository<MyCartList, String>{
	boolean existsByUserName(String userName);
	MyCartList findByUserName(String userName);
	//void deleteById(String catId);
}
