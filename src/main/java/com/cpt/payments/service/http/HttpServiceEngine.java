package com.cpt.payments.service.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpServiceEngine {

	private RestTemplate restTemplate;

    public HttpServiceEngine(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        System.out.println("HttpServiceEngine.HttpServiceEngine()"
        		+ "|restTemplate:" + restTemplate);
    }
    
    public ResponseEntity<String> makeHttpRequest(
    		HttpRequest httpRequest) {
    	
        try {
        	HttpHeaders httpHeaders =  httpRequest.getHttpHeaders();
            if (httpHeaders == null) {
            	httpHeaders = new HttpHeaders();
            }
            
            HttpEntity<Object> entity = new HttpEntity<>(
            		httpRequest.getRequest(), httpHeaders);
            
            ResponseEntity<String> httpResponse = restTemplate.exchange(
            		httpRequest.getUrl(), httpRequest.getMethod(), entity, String.class);
            System.out.println("Got httpResponse:" + httpResponse);
            
			return httpResponse;
			
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            System.out.println("Got Client/Server error while making httpCall" + ex);
            ex.printStackTrace();
        	return ResponseEntity.status(ex.getStatusCode()).body(null);
        	
        } catch (Exception ex) {
        	System.out.println("Exception making http call:" + ex);
        	ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
