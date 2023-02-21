package com.lancesoft.omg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.OrdersEntity;

public interface OrderDao extends JpaRepository<OrdersEntity ,String>{

	OrdersEntity findByorderId(String orderId);

}
