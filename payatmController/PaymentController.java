package com.lancesoft.omg.payatmController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lancesoft.omg.dao.MyCartListDao;
import com.lancesoft.omg.dao.OrdersDao;
import com.lancesoft.omg.entity.MyCartLists;
import com.lancesoft.omg.entity.OrdersEntity;
import com.lancesoft.omg.entity.PaytmDetailPojo;
import com.lancesoft.omg.service.AdminRegistrationServiceImpl;
import com.lancesoft.omg.service.UserDashBoardService;
import com.paytm.pg.merchant.PaytmChecksum;

@Controller
public class PaymentController {
	
	@Autowired
	private PaytmDetailPojo paytmDetailPojo;
	@Autowired
	private Environment env;
	@Autowired
	AdminRegistrationServiceImpl adminRegistrationServiceImpl;
	@Autowired
	UserDashBoardService userDashBoardServiceImpl;
	
	@Autowired
	MyCartListDao myCartListDao;
	@Autowired
	OrdersDao ordersDao;
	
	    @PostMapping(value = "/submitPaymentDetail")
	    @ResponseBody
	    public TreeMap<String, String> getRedirect(HttpServletRequest httpServletRequest, OrdersEntity ordersEntity) throws Exception {
	    	
	    	String userName=adminRegistrationServiceImpl.getMyToken(httpServletRequest);
	    	
	    	//save orders based on username
	    	OrdersEntity entity= userDashBoardServiceImpl.orderByPaytm(userName, ordersEntity);
	    
	        //from orders get order Id 
	    	String orderId=entity.getOrderId();
	    	
	    	MyCartLists myCartList= myCartListDao.findByUserName(userName);
	    	long totalAmmount=myCartList.getTotalCost();
	    	//String orderId= userDashBoardServiceImpl.getOrderid();
	       // ModelAndView modelAndView = new ModelAndView("redirect:" + paytmDetailPojo.getPaytmUrl());
	        TreeMap<String, String> parameters = new TreeMap<>();
	        paytmDetailPojo.getDetails().forEach((k, v) -> parameters.put(k, v));
	        parameters.put("MOBILE_NO", env.getProperty("paytm.mobile"));
	        parameters.put("EMAIL", env.getProperty("paytm.email"));
	        parameters.put("ORDER_ID", orderId);
	        parameters.put("TXN_AMOUNT", String.valueOf(totalAmmount));
	        parameters.put("CUST_ID", userName); 
	        parameters.put("PAYTM_URL", env.getProperty("paytm.payment.sandbox.paytmUrl"));
	        String checkSum = getCheckSum(parameters);
	        parameters.put("CHECKSUMHASH", checkSum);
	       // modelAndView.addAllObjects(parameters);
	        return parameters;
	    }

		Map<String, String> mpData = new HashMap<>();
	 
	 @PostMapping(value = "/paytmResponse")
	    public ModelAndView getResponseRedirect(Authentication authentication,HttpServletRequest request, Model model) {

		 ModelAndView modell = new ModelAndView("redirect:http://10.81.4.211:3001/receipt");
	        Map<String, String[]> mapData = request.getParameterMap();
	        TreeMap<String, String> parameters = new TreeMap<String, String>();
	        String paytmChecksum = "";
	        for (Entry<String, String[]> requestParamsEntry : mapData.entrySet()) {
	            if ("CHECKSUMHASH".equalsIgnoreCase(requestParamsEntry.getKey())){
	                paytmChecksum = requestParamsEntry.getValue()[0];
	            } else {
	            	parameters.put(requestParamsEntry.getKey(), requestParamsEntry.getValue()[0]);
	            }
	        }
	        String result;

	        boolean isValideChecksum = false;
	        System.out.println("RESULT : "+parameters.toString());
	        try {
	            isValideChecksum = validateCheckSum(parameters, paytmChecksum);
	             String orderId= parameters.get("ORDER_ID");
	            OrdersEntity entity= ordersDao.findByOrderId(orderId);
	        
	            if (isValideChecksum && parameters.containsKey("RESPCODE")) {
	                if (parameters.get("RESPCODE").equals("01")) {
	                    result = "Payment Successful";
	                    entity.setPaymentStatus("Paid");
	                    ordersDao.save(entity);
	                } else {
	                    result = "Payment Failed";
	                    ordersDao.deleteById(orderId);
	                }
	            } else {
	                result = "Checksum mismatched";
	            }
	        } catch (Exception e) {
	            result = e.toString();
	        }
	        parameters.remove("CHECKSUMHASH");
			mpData.putAll(parameters);
	       
	        return modell;
	    }

	    private boolean validateCheckSum(TreeMap<String, String> parameters, String paytmChecksum) throws Exception {
	        return PaytmChecksum.verifySignature(parameters,
	                paytmDetailPojo.getMerchantKey(), paytmChecksum);
	    }


	private String getCheckSum(TreeMap<String, String> parameters) throws Exception {
		return PaytmChecksum.generateSignature(parameters, paytmDetailPojo.getMerchantKey());
	}
	@PostMapping("/getPdf")
	@ResponseBody
	public void getResponseRedirec(HttpServletResponse response) throws IOException {
		String orderId=mpData.get("ORDER_ID");
		 
		 userDashBoardServiceImpl.geratePdf(response, orderId);
		
	}
	
	
	
}

