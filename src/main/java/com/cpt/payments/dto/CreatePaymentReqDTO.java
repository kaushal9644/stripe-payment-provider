package com.cpt.payments.dto;

import java.util.List;

import lombok.Data;

@Data
public class CreatePaymentReqDTO {
	
	private String txnRef;
	
	private List<LineItemDTO> lineItem;
	
	private String successUrl;
	private String cancelUrl;

}
