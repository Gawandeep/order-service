package com.example.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.order.dto.TransactionRequest;
import com.example.order.dto.TransactionResponse;
import com.example.order.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;


@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/saveOrder")
	public TransactionResponse saveOrder(@RequestBody TransactionRequest transactionRequest) throws JsonProcessingException {
		return orderService.saveOrder(transactionRequest);
	}
	
}
