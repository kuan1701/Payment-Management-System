package com.webproject.pms.service;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.model.entities.Payment;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface PaymentService {
	
	boolean makePaymentOnAccount(Long accountId, String accountNumber, BigDecimal amount, String appointment, Model model, Principal principal);
	
	void makePaymentOnCard(Long accountId, String cardNumber, BigDecimal amount, String appointment, Principal principal);
	
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
	
	List<Payment> searchByCriteriaWithoutOutgoing(Long userId, String startDate, String finalDate);
}
