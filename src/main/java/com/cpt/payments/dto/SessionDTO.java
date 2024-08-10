package com.cpt.payments.dto;

import lombok.Data;

@Data
public class SessionDTO {
	
	private String id;
	
	private String url; 
	
	private String status;
	
	private String payment_status;
	
}
