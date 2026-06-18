package com.services.banktransaction.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.services.banktransaction.service.TransactionService;

import lombok.RequiredArgsConstructor;

@CrossOrigin("*")
@RequestMapping("/transaction")
@RestController
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

	@PostMapping(value = "/transactionProcess", produces = "application/json")
	public ResponseEntity<?> transactionProcess(@RequestBody Map<String, Object> transactionMap) {
		try {

			return ResponseEntity.ok(transactionService.transactionProcess(transactionMap));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping(value = "/getTransactionDetails", produces = "application/json")
	public ResponseEntity<?> getTransactionDetails(@RequestParam String acctcode) {
		try {

			return ResponseEntity.ok(transactionService.getTransactionDetails(acctcode));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

}
