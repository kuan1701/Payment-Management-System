package com.webproject.pms.service;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.model.entities.User;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface PaymentService {
	
	Boolean makePaymentOnAccount(Long accountId, String accountNumber, BigDecimal amount, String appointment, Model model, User user);
	
	Boolean makePaymentOnCard(Long accountFromId, String cardNumber, BigDecimal amount, String appointment, Model model, User user);
	
	Boolean checkAvailableAmount(Account account, BigDecimal amount);
	
	void transaction(Account accountFrom, Account accountTo, BigDecimal amount);
	
	void transaction(Account accountFrom, String cardNumber, BigDecimal amount);
	
	Payment findPaymentByPaymentId(Long paymentId);

	Payment initializePayment(User user, BigDecimal amount, String appointment);

	List<Payment> findAllPaymentsByAccountId(Long accountId);
	
	List<Payment> findAllPaymentsByUserId(Long userId);
	
	List<Payment> searchByCriteria(Long userId, Boolean isOutgoing, String startDate, String finalDate);

	List<Payment> searchByCriteriaOutgoingFalse(Long userId, Boolean isOutgoing, String startDate, String finalDate);

	List<Payment> searchByCriteriaWithoutOutgoing(Long userId, String startDate, String finalDate);
}
