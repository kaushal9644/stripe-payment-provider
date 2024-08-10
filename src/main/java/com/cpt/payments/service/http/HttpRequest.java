package com.cpt.payments.service.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import lombok.Data;

@Data
public class HttpRequest {
	
	private String url;
	private HttpMethod method; 
	private Object request;
	private HttpHeaders httpHeaders;

}
