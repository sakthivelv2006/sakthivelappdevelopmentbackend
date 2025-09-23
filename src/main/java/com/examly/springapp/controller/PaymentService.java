package com.examly.springapp.controller;

import com.razorpay.RazorpayClient;
import com.razorpay.Order;
import org.json.JSONObject;

public class PaymentService {

public void createOrder() throws Exception {
RazorpayClient razorpayClient = new RazorpayClient("YOUR_KEY_ID", "YOUR_SECRET");

JSONObject options = new JSONObject();
options.put("amount", 50000); // amount in paise (50000 = Rs 500)
options.put("currency", "INR");
options.put("receipt", "txn_123456");

Order razorpayOrder = razorpayClient.orders.create(options);

System.out.println(razorpayOrder);
}
}