package com.lancesoft.omg.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lancesoft.omg.dao.MyCartDao;
import com.lancesoft.omg.dao.ProductsDao;
import com.lancesoft.omg.entity.AddToCart;
import com.lancesoft.omg.entity.AddressEntity;
import com.lancesoft.omg.entity.MyCart;
import com.lancesoft.omg.entity.MyCartList;
import com.lancesoft.omg.entity.OrdersEntity;
import com.lancesoft.omg.entity.ProductsEntity;
import com.lancesoft.omg.serviceImpl.AdminRegistrationServiceImpl;
import com.lancesoft.omg.serviceImpl.UserDashboardServiceImpl;
import com.lancesoft.omg.serviceImpl.UserRegistrationServiceImpl;
import com.lowagie.text.DocumentException;

@RestController
@RequestMapping("/api/user")
public class UserDashboardController {
	@Autowired
	UserRegistrationServiceImpl userRegistrationServiceImpl;
	@Autowired
	ProductsDao productsDao;
	@Autowired
	MyCartDao myCartDao;
	@Autowired
	AdminRegistrationServiceImpl adminRegistrationServiceImpl;
	@Autowired
	UserDashboardServiceImpl userDashboardServiceImpl;

	@PostMapping("/addToCart")
	public ResponseEntity<MyCart> saveToCart(@RequestBody AddToCart addToCart, MyCart myCart,
			HttpServletRequest httpServletRequest) {
		ProductsEntity productsEntity = productsDao.findByProdId(addToCart.getProdId());
		if (productsEntity.getStatus().equals("is not available")) {
			throw new RuntimeException("product is not available");
		} else
			myCart.setProductsEntity(productsEntity);
		myCart.setQuantity(addToCart.getQty());
		String userName = userRegistrationServiceImpl.getMyToken(httpServletRequest);
		return new ResponseEntity<MyCart>(userDashboardServiceImpl.saveCart(myCart, userName), HttpStatus.OK);

	}

	@GetMapping("/getMyCart")
	public ResponseEntity<MyCartList> getMyCart(HttpServletRequest httpServletRequest, MyCartList myCartList) {
		String userName = userRegistrationServiceImpl.getMyToken(httpServletRequest);
		return new ResponseEntity<MyCartList>(userDashboardServiceImpl.getMyCart(userName, myCartList), HttpStatus.OK);

	}

	@PostMapping("/updateCart")
	public ResponseEntity<MyCartList> updateCart(@RequestParam String cartId, @RequestParam long quantity,
			HttpServletRequest httpServletRequest, MyCartList myCartList) {
		String userName = userRegistrationServiceImpl.getMyToken(httpServletRequest);

		return new ResponseEntity<MyCartList>(
				userDashboardServiceImpl.updateCart(cartId, quantity, myCartList, userName), HttpStatus.OK);

	}

	@GetMapping("/getAllCartList")
	public ResponseEntity<MyCartList> getMyCartList(HttpServletRequest httpServletRequest) {
		String userName = userRegistrationServiceImpl.getMyToken(httpServletRequest);

		return new ResponseEntity<MyCartList>(userDashboardServiceImpl.getMyCartList(userName), HttpStatus.OK);

	}

	@DeleteMapping("/deleteCart")
	public ResponseEntity<String> deleteCart(@RequestParam String cartId, HttpServletRequest httpServletRequest) {
		String userName = userRegistrationServiceImpl.getMyToken(httpServletRequest);
		return new ResponseEntity<String>(userDashboardServiceImpl.deleteCart(cartId, userName), HttpStatus.OK);

	}

	@DeleteMapping("/deleteCartList")
	public ResponseEntity<String> deleteCartList(HttpServletRequest httpServletRequest, MyCartList myCartList) {
		String userName = userRegistrationServiceImpl.getMyToken(httpServletRequest);
		return new ResponseEntity<String>(userDashboardServiceImpl.deleteMyCartList(userName), HttpStatus.OK);
	}

	@PostMapping("/addAddress")
	public ResponseEntity<AddressEntity> addAddress(@RequestBody AddressEntity addressEntity,
			HttpServletRequest httpServletRequest) {
		String userName = userRegistrationServiceImpl.getMyToken(httpServletRequest);
		return new ResponseEntity<AddressEntity>(userDashboardServiceImpl.addAddress(addressEntity, userName),
				HttpStatus.OK);
	}

	@PostMapping("/placeOrderWithCOD")
	public ResponseEntity<OrdersEntity> placeOrder(HttpServletRequest httpServletRequest, OrdersEntity ordersEntity) {
		String userName = userRegistrationServiceImpl.getMyToken(httpServletRequest);
		return new ResponseEntity<OrdersEntity>(userDashboardServiceImpl.placeOrder(userName, ordersEntity),
				HttpStatus.OK);

	}
	
	@PostMapping("/generatePdf")
	public ResponseEntity<String> generatePdf(@RequestParam String orderId, HttpServletResponse httpServletResponse)
			throws DocumentException, IOException {
		userDashboardServiceImpl.generatePdf(httpServletResponse, orderId);
		return new ResponseEntity<String>(HttpStatus.OK);

	}
}
