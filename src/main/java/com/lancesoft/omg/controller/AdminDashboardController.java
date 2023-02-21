package com.lancesoft.omg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dao.CategoriesDao;
import com.lancesoft.omg.entity.CategoriesEntity;
import com.lancesoft.omg.entity.Inventory;
import com.lancesoft.omg.entity.ProductsEntity;
import com.lancesoft.omg.service.AdminDashBoardService;
import com.lancesoft.omg.serviceImpl.AdminDashboardServiceImpl;

@RestController
@RequestMapping("/api/admin")
public class AdminDashboardController {
	@Autowired
	AdminDashBoardService adminDashboardServiceImpl;
	@Autowired
	CategoriesDao CategoriesDao;

	@PostMapping("/addCategories")
	public CategoriesEntity addCategories(@RequestBody CategoriesEntity categoriesEntity) {
		return adminDashboardServiceImpl.addCategory(categoriesEntity);

	}

	@PostMapping("/addProducts")
	public ProductsEntity addProducts(@RequestBody ProductsEntity productsEntity) {
		return adminDashboardServiceImpl.addProducts(productsEntity);

	}

	@PostMapping("/addInventory")
	public Inventory addInventory(@RequestBody Inventory inventory) {
		return adminDashboardServiceImpl.addInventory(inventory);
	}

	@GetMapping("/getAllCategories")
	public ResponseEntity<CategoriesEntity> getCategories() {
		return new ResponseEntity(adminDashboardServiceImpl.getAllCategories(), HttpStatus.OK);

	}

	@GetMapping("/getAllProducts")
	public ResponseEntity<ProductsEntity> getAllProducts() {
		return new ResponseEntity(adminDashboardServiceImpl.getAllProducts(), HttpStatus.OK);

	}

	@GetMapping("/getAllinventory")
	public ResponseEntity<Inventory> getAllInventory() {
		return new ResponseEntity(adminDashboardServiceImpl.getAllInventory(), HttpStatus.OK);

	}

	@GetMapping("/getOneCategory")
	public ResponseEntity<CategoriesEntity> getCategory(@RequestParam String catName) {
		return new ResponseEntity(adminDashboardServiceImpl.getOneCategory(catName), HttpStatus.OK);

	}
	
	
	
	
	
	
	
	

}
