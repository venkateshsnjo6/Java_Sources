package com.services.banktransaction.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.services.banktransaction.model.CustomerPojo;
import com.services.banktransaction.service.CustomerService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RequestMapping("/customer")
@RestController
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping(value = "/createCustomer", produces = "application/json")
	public ResponseEntity<?> createCustomer(@RequestBody CustomerPojo customer) {
		try {

			return ResponseEntity.ok(customerService.createCustomer(customer));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping(value = "/updateCustomer", produces = "application/json")
	public ResponseEntity<?> updateCustomer(@RequestBody CustomerPojo customer) {
		try {

			return ResponseEntity.ok(customerService.updateCustomer(customer));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PostMapping(value = "/deleteCustomer", produces = "application/json")
	public ResponseEntity<?> deleteCustomer(@RequestBody CustomerPojo customer) {
		try {

			return ResponseEntity.ok(customerService.deleteCustomer(customer));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

}
