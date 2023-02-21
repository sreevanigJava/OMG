package com.lancesoft.omg.service;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.lancesoft.omg.entity.CategoriesEntity;
import com.lancesoft.omg.entity.Inventory;
import com.lancesoft.omg.entity.ProductsEntity;

public interface AdminDashBoardService {
public CategoriesEntity addCategory(CategoriesEntity categoriesEntity);
public Inventory addInventory(Inventory inventory);
public ProductsEntity addProducts(ProductsEntity productsEntity);
List<ProductsEntity>getAllProducts();
List<CategoriesEntity> getAllCategories();
List<Inventory> getAllInventory();
List<ProductsEntity> getOneCategory(String catName);



}
