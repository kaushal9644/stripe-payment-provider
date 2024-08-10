package com.cpt.payments.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.cpt.payments.dto.CreatePaymentReqDTO;
import com.cpt.payments.dto.CreatePaymentResDTO;
import com.cpt.payments.dto.LineItemDTO;
import com.cpt.payments.dto.SessionDTO;
import com.cpt.payments.service.http.HttpRequest;
import com.cpt.payments.service.http.HttpServiceEngine;
import com.cpt.payments.service.interfaces.PaymentService;
import com.cpt.payments.stripe.Session;
import com.google.gson.Gson;

@Service
public class PaymentServiceImpl implements PaymentService{

	@Autowired
	private HttpServiceEngine httpServiceEngine; 
	
	@Value("${stripe.createsession.url}")
	private String stripeCreateSessionUrl;
	
	@Value("${stripe.secretkey}")
	private String stripeSecretKey;
	
	@Autowired
	private Gson gson;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CreatePaymentResDTO processPayment(CreatePaymentReqDTO paymentReq) {
		
		System.out.println("PaymentServiceImpl.processPayment()|"
				+ "paymentReq:" + paymentReq);
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		httpHeaders.setBasicAuth(stripeSecretKey, "");
		
		MultiValueMap<String, String> formUrlEncodedRequest = new LinkedMultiValueMap<>();
		formUrlEncodedRequest.add("success_url", paymentReq.getSuccessUrl());
		formUrlEncodedRequest.add("cancel_url", paymentReq.getCancelUrl());
		formUrlEncodedRequest.add("mode", "payment");// FIXED, Property

		for(int i = 0; i < paymentReq.getLineItem().size(); i++) {
			LineItemDTO lineItem = paymentReq.getLineItem().get(i);
			
			formUrlEncodedRequest.add("line_items[" + i + "][price_data][currency]", lineItem.getCurrency());
			formUrlEncodedRequest.add("line_items[" + i + "][price_data][product_data][name]", lineItem.getProductName());
			
			int unitAmountInCents = Integer.valueOf((int) (lineItem.getUnitAmount() * 100));
			formUrlEncodedRequest.add("line_items[" + i + "][price_data][unit_amount]", String.valueOf(unitAmountInCents));
			formUrlEncodedRequest.add("line_items[" + i + "][quantity]", String.valueOf(lineItem.getQuantity()));
		}
		
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setUrl(stripeCreateSessionUrl);
		httpRequest.setHttpHeaders(httpHeaders);
		httpRequest.setMethod(HttpMethod.POST);
		httpRequest.setRequest(formUrlEncodedRequest);
		
		ResponseEntity<String> response = httpServiceEngine.makeHttpRequest(
        		httpRequest);

        System.out.println("RESPONSE from http service ||response:" + response.getBody());
        
        if (response.getStatusCode().is2xxSuccessful()) {
        	Session session = gson.fromJson(
        			response.getBody(), Session.class);
        	
        	CreatePaymentResDTO responseObj = modelMapper.map(session, CreatePaymentResDTO.class);
        	
        	System.out.println("responseObj:" + responseObj);
    		
    		return responseObj;
        	
        } else {
        	//TODO handle it later
        	return new CreatePaymentResDTO();
        }
		
	}

}
