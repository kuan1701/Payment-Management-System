package com.webproject.pms.service;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.model.entities.dto.PaymentGetDto;

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
	
	PaymentGetDto findPaymentByPaymentId(Long paymentId);
	
	List<PaymentGetDto> findAllPaymentsByAccountId(Long accountId);
	
	List<PaymentGetDto> findAllPaymentsByUserId(Long userId);
	
	List<PaymentGetDto> findLastPaymentsByUserId(Long userId);
	
	List<PaymentGetDto> findAllPayments();
	
	List<PaymentGetDto> searchByCriteria(Long userId, Boolean isOutgoing, String startDate, String finalDate);
	
	List<PaymentGetDto> searchByCriteria(Long userId, String startDate, String finalDate);
}
