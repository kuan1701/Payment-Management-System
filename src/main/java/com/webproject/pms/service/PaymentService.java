package com.webproject.pms.service;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.model.entities.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
	
	Payment makePaymentOnAccount(Long accountId, String accountNumber, BigDecimal amount, BigDecimal exchangeRate, String appointment);
	
	void makePaymentOnCard(Long accountId, String cardNumber, BigDecimal amount, String appointment);
	
	boolean checkAvailableAccount(Account account);
	
	boolean checkAvailableCard(BankCard card);
	
	boolean checkAvailableAmount(Account account, BigDecimal amount);
	
	void transaction(Account accountFrom, Account accountTo, BigDecimal amount);
	
	void transaction(Account accountFrom, String cardNumber, BigDecimal amount);
	
	Payment findPaymentByPaymentId(Long paymentId);
	
	List<Payment> findAllPaymentsByAccountId(Long accountId);
	
	List<Payment> findAllPaymentsByUserId(Long userId);
	
	List<Payment> findLastPaymentsByUserId(Long userId);
	
	List<Payment> findAllPayments();
	
	List<Payment> searchByCriteria(Long userId, Boolean isOutgoing, String startDate, String finalDate);
	
	List<Payment> searchByCriteria(Long userId, String startDate, String finalDate);
}
