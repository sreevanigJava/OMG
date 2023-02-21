package com.lancesoft.omg.serviceImpl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lancesoft.omg.dao.AddressDao;
import com.lancesoft.omg.dao.InventoryDao;
import com.lancesoft.omg.dao.MyCartDao;
import com.lancesoft.omg.dao.MyCartListDao;
import com.lancesoft.omg.dao.OrderDao;
import com.lancesoft.omg.dao.ProductsDao;
import com.lancesoft.omg.dao.UserRegistrationDao;
import com.lancesoft.omg.entity.AddressEntity;
import com.lancesoft.omg.entity.Inventory;
import com.lancesoft.omg.entity.MyCart;
import com.lancesoft.omg.entity.MyCartList;
import com.lancesoft.omg.entity.OrdersEntity;
import com.lancesoft.omg.entity.ProductsEntity;
import com.lancesoft.omg.entity.UserRegistrationEntity;
import com.lancesoft.omg.service.UserDashboardService;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Header;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

@Service
@Transactional
public class UserDashboardServiceImpl implements UserDashboardService {
	@Autowired
	MyCartDao myCartDao;
	@Autowired
	MyCartListDao myCartListDao;
	@Autowired
	AddressDao addressDao;
	@Autowired
	OrderDao orderDao;
	@Autowired
	InventoryDao inventoryDao;
	@Autowired
	ProductsDao productsDao;
	@Autowired
	UserRegistrationDao userRegistrationDao;

	public MyCart saveCart(MyCart myCart, String userName) {
		String prodIdToBeAdded = myCart.getProductsEntity().getProdId();

		List<MyCart> myCarts = myCartDao.findByUserName(userName);

		for (int i = 0; i < myCarts.size(); i++) {
			String addedProd = myCarts.get(i).getProductsEntity().getProdId();
			if (addedProd == prodIdToBeAdded) {
				throw new RuntimeException("product already exists");
			}
		}
		myCart.setUserName(userName);
		return myCartDao.save(myCart);
	}

	public MyCartList getMyCart(String userName, MyCartList myCartList) {
		List<MyCart> cartList = myCartDao.findByUserName(userName);
		if (cartList.isEmpty()) {
			throw new RuntimeException("your cart is empty,please add products into the cart");
		}
		long totalPrice = 0;

		for (MyCart myCart : cartList) {
			System.err.println("my cart excuted>>>>>>>>>>>>>");
			totalPrice = totalPrice + myCart.getProductsEntity().getPrice() * myCart.getQuantity();
		}
		if (myCartListDao.existsByUserName(userName)) {
			System.err.println("inside if excuted>>>>>>>>>>>>>");
			MyCartList myCartList1 = myCartListDao.findByUserName(userName);
			System.err.println("inside if excuted line1>>>>>>>>>>>>>");
			String myCarListId = myCartList1.getId();
			myCartList.setId(myCarListId);
		}
		myCartList.setUserName(userName);
		myCartList.setMyCartItems(cartList);
		myCartList.setTotalCost(totalPrice);

		return myCartListDao.save(myCartList);
	}

	public MyCartList getMyCartList(String userName) {
		MyCartList cartList = myCartListDao.findByUserName(userName);
		return cartList;
	}

	public String deleteCart(String cartId, String userName) {
		if (!(myCartDao.existsById(cartId))) {
			throw new RuntimeException("Could not delete as it does not exist");
		}
		MyCart myCart = myCartDao.findByCartId(cartId);

		myCartDao.delete(myCart);
		return "deleted";
	}

	public String deleteMyCartList(String userName) {
		MyCartList myCartList = myCartListDao.findByUserName(userName);
		List<MyCart> myCarts = myCartList.getMyCartItems();
		for (int i = 0; i < myCarts.size(); i++) {
			myCartDao.delete(myCarts.get(i));
		}
		return "cartList deleted";
	}

	public MyCartList updateCart(String cartId, long quantity, MyCartList myCartList, String userName) {
		if (!(myCartDao.existsById(cartId))) {
			throw new RuntimeException("product is not exists in your cart");
		}
		MyCart cart = myCartDao.findByCartId(cartId);
		cart.setCartId(cartId);
		cart.setQuantity(quantity);
		myCartDao.save(cart);
		return this.getMyCart(userName, myCartList);

	}

	public AddressEntity addAddress(AddressEntity addressEntity, String userName) {
		if (addressEntity.equals(null)) {
			throw new RuntimeException("please add the address");
		}
		addressEntity.setUserName(userName);
		if (addressEntity.isCurrentAddress()) {
			List<AddressEntity> add = addressDao.findByUserName(userName);

			if (!(add == null)) {
				for (AddressEntity entity : add) {
					if (entity.isCurrentAddress()) {
						String id = entity.getId();
						entity.setId(id);
						entity.setCurrentAddress(false);
						addressDao.save(entity);
					}
				}
			}
		}
		return addressDao.save(addressEntity);

	}

	public OrdersEntity placeOrder(String userName, OrdersEntity ordersEntity) {

		ordersEntity.setUserName(userName);
		List<AddressEntity> addressEntity = addressDao.findByUserName(userName);
		for (AddressEntity adEntity : addressEntity) {
			if (adEntity.isCurrentAddress()) {
				ordersEntity.setAddressEntity(adEntity);
			}
		}
		MyCartList cartList = myCartListDao.findByUserName(userName);
		ordersEntity.setMyCartList(cartList);
		DateTime date = new DateTime();
		date = date.plusHours(4);
		String stringDate = date.toString();

		ordersEntity.setDeliveryTime(stringDate.substring(11, 19) + " Date: " + stringDate.substring(0, 10));
		ordersEntity.setPaymentMode("Cash on delivery");
		ordersEntity.setPaymentStatus("Not paid");

		List<MyCart> myCarts = ordersEntity.getMyCartList().getMyCartItems();
		OrdersEntity entity = orderDao.save(ordersEntity);
		for (int i = 0; i < myCarts.size(); i++) {
			MyCart cart = myCarts.get(i);
			String orderedProductName = cart.getProductsEntity().getProdName();
			long orderQuantity = cart.getQuantity();
			Inventory inventory = inventoryDao.findByProductName(orderedProductName);
			long remainingQnty = 0;
			if (inventory.getQuantity() >= orderQuantity) {
				remainingQnty = inventory.getQuantity() - orderQuantity;
			} else
				throw new RuntimeException("Given order qunatity is beyond our current stock");
			if (remainingQnty == 0) {
				ProductsEntity productsEntity = productsDao.findByprodName(orderedProductName);
				productsEntity.setStatus("not available");
			}
			inventory.setQuantity(remainingQnty);
			inventoryDao.save(inventory);
		}
		myCartListDao.delete(cartList);
		return entity;

	}

	public void generatePdf(HttpServletResponse httpServletResponse, String orderId)
			throws DocumentException, IOException {
		if (!(orderDao.existsById(orderId))) {
			throw new RuntimeException("order not found");
		}
		OrdersEntity ordersEntity = orderDao.findByorderId(orderId);
		UserRegistrationEntity userRegistrationEntity = userRegistrationDao.findByUserName(ordersEntity.getUserName());

		AddressEntity addressEntity = ordersEntity.getAddressEntity();
		MyCartList myCartList = ordersEntity.getMyCartList();
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, httpServletResponse.getOutputStream());

		Image image = Image.getInstance("omg.jpg");
		image.scaleAbsolute(120, 40);
		image.setAlignment(50);

		document.open();
		Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE);
		fontTitle.setSize(18);
		fontTitle.setColor(19, 124, 50);

		Header header = new Header("Invoice", "0");

		Paragraph paragraph = new Paragraph("Invoice #6006922 for Order # BPFVO­6012102­211215 : www.omg.com");
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);

		Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
		fontParagraph.setSize(12);
		// fontParagraph.setColor(206,33,33);

		Paragraph paragraph2 = new Paragraph("Invoice", fontTitle);
		paragraph2.setSpacingAfter(2f);
		paragraph2.setAlignment(Paragraph.ALIGN_CENTER);

		Paragraph paragraph4 = new Paragraph("Delivery Location :");
		Paragraph paragraph5 = new Paragraph(
				userRegistrationEntity.getFirstName() + " " + userRegistrationEntity.getLastName());
		Paragraph paragraph6 = new Paragraph(addressEntity.getHouseNumber() + "," + addressEntity.getLandmark());
		Paragraph paragraph7 = new Paragraph(addressEntity.getState() + "," + addressEntity.getPincode());

		Paragraph paragraph3 = new Paragraph("Order ID", fontParagraph);

		Table table = new Table(2, 6);
		table.setAlignment(5);
		table.setBorder(2);
		table.setPadding(3);
		Cell cell = new Cell("Invoice No");
		table.addCell(cell);
		table.addCell(String.valueOf(ordersEntity.getOrderId()));
		table.addCell(paragraph3);
		table.addCell(String.valueOf(ordersEntity.getOrderId()));
		table.addCell("Delivery Time");
		table.addCell(ordersEntity.getDeliveryTime());
		table.addCell(new Paragraph("Final Total", fontParagraph));
		table.addCell(String.valueOf(myCartList.getTotalCost()) + " INR");
		table.addCell("Payment By");
		table.addCell(ordersEntity.getPaymentMode());
		table.addCell("Amount payable");
		table.addCell(String.valueOf(myCartList.getTotalCost()) + " rs");
//		table.addCell("No.of items");
//		table.addCell("13");

		document.add(paragraph);
		document.add(paragraph2);
		document.add(image);
		document.add(paragraph4);
		document.add(paragraph5);
		document.add(paragraph6);
		document.add(paragraph7);
		document.add(table);

		document.close();

	}

	public OrdersEntity orderByPaytm(String userName, OrdersEntity ordersEntity) {
		ordersEntity.setUserName(userName);

		List<AddressEntity> addressEntity = addressDao.findByUserName(userName);
		// adding an address to orders which is default address
		for (AddressEntity adEntity : addressEntity) {
			if (adEntity.isCurrentAddress()) {
				ordersEntity.setAddressEntity(adEntity);
			}
		}
		MyCartList myCartLists = myCartListDao.findByUserName(userName);
		// setting myCarlist to orders entity
		ordersEntity.setMyCartList(myCartLists);
		DateTime date = new DateTime();
		date = date.plusHours(4);
		String stringDate = date.toString();

		ordersEntity.setDeliveryTime(stringDate.substring(11, 19) + " Date: " + stringDate.substring(0, 10));
		ordersEntity.setPaymentMode("Paytm");
		ordersEntity.setPaymentStatus("Not Paid");

		
		return orderDao.save(ordersEntity);
	


}

}
