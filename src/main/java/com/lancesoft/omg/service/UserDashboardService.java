package com.lancesoft.omg.service;

import com.lancesoft.omg.entity.AddressEntity;
import com.lancesoft.omg.entity.MyCart;
import com.lancesoft.omg.entity.MyCartList;
import com.lancesoft.omg.entity.OrdersEntity;


public interface UserDashboardService {
	 MyCart saveCart(MyCart myCart, String userName);
	 MyCartList getMyCart(String userName, MyCartList myCartList); 
	 AddressEntity addAddress(AddressEntity addressEntity, String userName);
	 OrdersEntity orderByPaytm(String userName, OrdersEntity entity);

}
