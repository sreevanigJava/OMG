package com.lancesoft.omg.serviceImpl;

import java.util.List;

import javax.management.RuntimeErrorException;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.lancesoft.omg.dao.CategoriesDao;
import com.lancesoft.omg.dao.ProductsDao;
import com.lancesoft.omg.dao.InventoryDao;
import com.lancesoft.omg.dao.MyCartDao;
import com.lancesoft.omg.entity.CategoriesEntity;
import com.lancesoft.omg.entity.Inventory;
import com.lancesoft.omg.entity.MyCart;
import com.lancesoft.omg.entity.ProductsEntity;
import com.lancesoft.omg.exception.CategoriesAreEmpty;
import com.lancesoft.omg.service.AdminDashBoardService;

@Service
public class AdminDashboardServiceImpl implements AdminDashBoardService {
	@Autowired
	CategoriesDao categoriesDao;
	@Autowired
	ProductsDao productsDao;
	@Autowired
	InventoryDao inventoryDao;
	@Autowired
	MyCartDao myCartDao;
	public CategoriesEntity addCategory(CategoriesEntity categoriesEntity) {
		if (categoriesEntity == null) {
			throw new RuntimeException("null found in categories please check");
		}

		return categoriesDao.save(categoriesEntity);

	}

	public ProductsEntity addProducts(ProductsEntity productsEntity) {
		if (productsEntity == null) {
			throw new RuntimeException("null found in categories please check");
		} else if (productsDao.existsByProdName(productsEntity.getProdName())) {
			throw new RuntimeException("product already exist");
		}
		CategoriesEntity categoriesEntity=categoriesDao.findByCatName(productsEntity.getCategoriesEntity().getCatName());
		productsEntity.setCategoriesEntity(categoriesEntity);
		return productsDao.save(productsEntity);
	}

	@Override
	public Inventory addInventory(Inventory inventory) {

		if (inventoryDao.existsByProductName(inventory.getProductName())) {
			throw new RuntimeException("product already exist");
		} else if (inventory == null) {
			throw new RuntimeException("null found in categories please check");
		}
		return inventoryDao.save(inventory);
	}

	public List<CategoriesEntity> getAllCategories() {

		List<CategoriesEntity> categoriesEntity = categoriesDao.findAll();
		if (!categoriesEntity.isEmpty()) {

			return categoriesEntity;
		} else
			throw new CategoriesAreEmpty("categories are empty ");
	}

	public List<ProductsEntity> getAllProducts() {
		List<ProductsEntity> productsEntity = productsDao.findAll();
		if (!productsEntity.isEmpty()) {
			return productsEntity;
		} else
			throw new CategoriesAreEmpty("Products are empty ");
	}

	public List<Inventory> getAllInventory() {
		List<Inventory> inventory = inventoryDao.findAll();
		if (!inventory.isEmpty()) {
			return inventory;
		} else
			throw new CategoriesAreEmpty("inventory is empty");
	}

	@Override
	public List<ProductsEntity> getOneCategory(String catName) {
		CategoriesEntity categoriesEntity = categoriesDao.findByCatName(catName);
		List<ProductsEntity> productEntity = productsDao.findBycategoriesEntity(categoriesEntity);
		if (productEntity.isEmpty()) {
			throw new RuntimeException("null found in categories please check");
		} else
			return productEntity;
	}


	
	
	
	
	
	
	
	
}
