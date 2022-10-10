package com.example.order.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.order.Entity.Order;
import com.example.order.dto.Payment;
import com.example.order.dto.TransactionRequest;
import com.example.order.dto.TransactionResponse;
import com.example.order.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@RefreshScope
@Slf4j
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;

	@Lazy
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${microservice.payment-service.endpoints.endpoint.uri}")
	private String PAYMENT_ENDPOINT;
	
	public TransactionResponse saveOrder(TransactionRequest transactionRequest) throws JsonProcessingException {
		log.info("Order Request : {}",new ObjectMapper().writeValueAsString(transactionRequest));
		Order order=transactionRequest.getOrder();
		Payment payment=transactionRequest.getPayment();
		payment.setOrder_id(order.getOrder_id());
		payment.setAmount(order.getAmount());
		payment.setStatus(transactionStatus());
		Payment paymentResponse=restTemplate.postForObject(PAYMENT_ENDPOINT, payment, Payment.class);
		log.info("Payment Response : {}",new ObjectMapper().writeValueAsString(paymentResponse));
		orderRepository.save(order);
		return new TransactionResponse(order,paymentResponse.getTrans_id(),paymentResponse.getStatus());
	}
	
	public String transactionStatus() {
		return new Random().nextBoolean()?"Success":"Failure";
	}

}
