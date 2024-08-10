package com.cpt.payments.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cpt.payments.constant.EndPoints;
import com.cpt.payments.dto.CreatePaymentReqDTO;
import com.cpt.payments.dto.CreatePaymentResDTO;
import com.cpt.payments.pojo.CreatePaymentReq;
import com.cpt.payments.pojo.CreatePaymentRes;
import com.cpt.payments.service.interfaces.PaymentService;

@RestController
@RequestMapping(EndPoints.V1_PAYMENTS)
public class PaymentController {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PaymentService service;

	@PostMapping(value = {EndPoints.EMPTY_STRING, EndPoints.SLASH})
	public ResponseEntity<CreatePaymentRes> createPayment(
			@RequestBody CreatePaymentReq paymentReq) {

		System.out.println("PaymentController.createPayment()|"
				+ "paymentReq:" + paymentReq);

		
		CreatePaymentReqDTO reqDTO = modelMapper.map(
				paymentReq, CreatePaymentReqDTO.class);

		System.out.println("Converted Pojo to DTO|reqDTO:" + reqDTO);

		CreatePaymentResDTO serviceResponse = service.processPayment(reqDTO);
		
		CreatePaymentRes finalRes = modelMapper.map(serviceResponse, CreatePaymentRes.class);

		System.out.println("Converted response back to pojo"
				+ "|finalRes:" + finalRes);
		
		return new ResponseEntity<CreatePaymentRes>(finalRes, 
				HttpStatus.CREATED);
	}

}
