package com.lancesoft.omg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.MyCart;

public interface MyCartDao extends JpaRepository<MyCart, String>{
	List<MyCart> findByUserName(String userName);
	MyCart findByCartId(String cartId);
}
