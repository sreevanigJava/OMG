package com.lancesoft.omg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.Inventory;

public interface InventoryDao extends JpaRepository<Inventory, Integer>{
boolean existsByProductName(String productName);

Inventory findByProductName(String orderedProductName);
}
