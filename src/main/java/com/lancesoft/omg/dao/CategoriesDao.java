package com.lancesoft.omg.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.CategoriesEntity;

public interface CategoriesDao extends JpaRepository<CategoriesEntity, String>{
public CategoriesEntity findByCatName(String catName);
}
