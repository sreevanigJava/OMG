package com.lancesoft.omg.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lancesoft.omg.entity.CategoriesEntity;
import com.lancesoft.omg.entity.ProductsEntity;

public interface ProductsDao extends JpaRepository<ProductsEntity, String>{
	boolean existsByProdName(String prodName);
	List<ProductsEntity> findBycategoriesEntity(CategoriesEntity categoriesEntity);
	ProductsEntity findByProdId(String prodId);
	ProductsEntity findByprodName(String orderedProductName);
}
