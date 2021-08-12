package com.webproject.pms.service.impl;

import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.AccountDao;
import com.webproject.pms.model.dao.PaymentDao;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.PaymentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

	private final AccountDao accountDao;
	private final PaymentDao paymentDao;
	private final MapStructMapper mapStructMapper;
	private final ActionLogServiceImpl actionLogService;
	private static final Logger LOGGER = LogManager.getLogger(PaymentServiceImpl.class);

	@Autowired
	public PaymentServiceImpl(AccountDao accountDao,
							  PaymentDao paymentDao,
							  MapStructMapper mapStructMapper,
							  ActionLogServiceImpl actionLogService
	) {
		this.accountDao = accountDao;
		this.paymentDao = paymentDao;
		this.mapStructMapper = mapStructMapper;
		this.actionLogService = actionLogService;
	}
	
	@Override
	public synchronized Boolean makePaymentOnAccount(Long accountFromId,
													 String accountToNumber,
													 BigDecimal amount,
													 String appointment,
													 Model model,
													 User user
	) {
		Account accountFrom = accountDao.getById(accountFromId);
		Account accountTo = accountDao.findAccountByNumber(accountToNumber);
		BigDecimal exchangeRate = new BigDecimal("1.00");
		
		if (accountFrom.getBlocked()) {
			model.addAttribute("paymentError", "senderAccountBlockedError");
			actionLogService.createLog("ERROR: Unsuccessful attempt to make a payment", user);
			LOGGER.error("ERROR: Unsuccessful attempt to make a payment. Sender account blocked");
			return false;
		}
		
		if (accountTo.getBlocked()) {
			model.addAttribute("paymentError", "recipientCardNotExistOrBlockedError");
			actionLogService.createLog("ERROR: Unsuccessful attempt to make a payment", user);
			LOGGER.error("ERROR: Unsuccessful attempt to make a payment. Recipient account blocked");
			return false;
		}
		
		// Outgoing payment details
		Payment payment = new Payment();
		payment.setUserId(user.getUserId());
		payment.setAccount(accountFrom);
		payment.setOutgoing(true);
		payment.setSenderNumber(accountFrom.getNumber());
		payment.setSenderAmount(amount);
		payment.setSenderCurrency(accountFrom.getCurrency());
		payment.setRecipientNumber(accountToNumber);
		payment.setRecipientAmount(amount.multiply(exchangeRate));
		payment.setRecipientCurrency(accountTo.getCurrency());
		payment.setExchangeRate(exchangeRate);
		payment.setAppointment(appointment);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		payment.setDate(formatter.format(new Date()));
		
		Payment paymentTo = new Payment();
		paymentTo.setUserId(user.getUserId());
		paymentTo.setAccount(accountTo);
		paymentTo.setOutgoing(false);
		paymentTo.setSenderNumber(accountFrom.getNumber());
		paymentTo.setSenderAmount(amount);
		paymentTo.setSenderCurrency(accountFrom.getCurrency());
		paymentTo.setRecipientNumber(accountToNumber);
		paymentTo.setRecipientAmount(amount.multiply(exchangeRate));
		paymentTo.setRecipientCurrency(accountTo.getCurrency());
		paymentTo.setExchangeRate(exchangeRate);
		paymentTo.setAppointment(appointment);
		SimpleDateFormat formatterTo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatterTo.setTimeZone(TimeZone.getTimeZone("UTC"));
		paymentTo.setDate(formatterTo.format(new Date()));
		
		if (checkAvailableAmount(accountFrom, amount)) {
			transaction(accountFrom, accountTo, amount);
			payment.setNewBalance(accountFrom.getBalance());
			payment.setStatus(true);
			paymentDao.save(payment);
			
			// Incoming payment details
			paymentTo.setAccount(accountTo);
			paymentTo.setOutgoing(false);
			paymentTo.setStatus(true);
			paymentTo.setNewBalance(accountTo.getBalance());
			paymentDao.save(paymentTo);
		}
		else {
			payment.setStatus(false);
			paymentDao.save(payment);
			model.addAttribute("paymentError", "insufficientFundsError");
			LOGGER.error("Payment arrangement error!");
			return false;
		}

		actionLogService.createLog("PAYMENT_COMPLETED: The payment was made from account ["
				+ accountFrom.getNumber() + "] to account ["
				+ accountToNumber + "]", user);
		LOGGER.info("PAYMENT_COMPLETED: The payment was made from account ["
				+ accountFrom.getNumber() + "] to account ["
				+ accountToNumber + "]");
		return true;
	}
	
	@Override
	public synchronized Boolean makePaymentOnCard(Long accountFromId, String cardNumber, BigDecimal amount, String appointment, Model model, User user) {

		Account accountFrom = accountDao.getById(accountFromId);
		BigDecimal exchangeRate = new BigDecimal("1.00");

		if (accountFrom.getBlocked()) {
			model.addAttribute("paymentError", "senderAccountBlockedError");
			actionLogService.createLog("ERROR: Unsuccessful attempt to make a payment", user);
			LOGGER.error("ERROR: Unsuccessful attempt to make a payment");
			return false;
		}

		// Outgoing payment details
		Payment payment = new Payment();
		payment.setUserId(user.getUserId());
		payment.setAccount(accountFrom);
		payment.setOutgoing(true);
		payment.setSenderNumber(accountFrom.getNumber());
		payment.setSenderAmount(amount);
		payment.setSenderCurrency(accountFrom.getCurrency());
		payment.setRecipientNumber(cardNumber);
		payment.setRecipientAmount(amount.multiply(exchangeRate));
		payment.setRecipientCurrency(null);
		payment.setExchangeRate(exchangeRate);
		payment.setAppointment(appointment);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		payment.setDate(formatter.format(new Date()));

		if (checkAvailableAmount(accountFrom, amount)) {
			transaction(accountFrom, cardNumber, amount);
			payment.setNewBalance(accountFrom.getBalance());
			payment.setStatus(true);
			paymentDao.save(payment);
		}
		else {
			payment.setStatus(false);
			paymentDao.save(payment);
			LOGGER.error("Payment arrangement error!");
		}
		actionLogService.createLog("PAYMENT_COMPLETED: The payment was made from account ["
				+ accountFrom.getNumber() + "] to bank card ["
				+ cardNumber + "]", user);
		LOGGER.info("PAYMENT_COMPLETED: The payment was made from account ["
				+ accountFrom.getNumber() + "] to bank card ["
				+ cardNumber + "]");
		return true;
	}
	
	@Override
	public synchronized Boolean checkAvailableAmount(Account account, BigDecimal amount) {

		return account.getBalance().compareTo(amount) >= 0;
	}
	
	@Override
	public synchronized void transaction(Account accountFrom, Account accountTo, BigDecimal amount) {
		
		if (!accountFrom.getBlocked() && !accountTo.getBlocked()) {
			accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
			accountTo.setBalance(accountTo.getBalance().add(amount));
			accountDao.save(accountFrom);
			accountDao.save(accountTo);
		} else {
			LOGGER.info("Trying to withdraw or add funds to a blocked account!");
		}
	}
	
	@Override
	public synchronized void transaction(Account accountFrom, String cardNumber, BigDecimal amount) {
		
		if (!accountFrom.getBlocked()) {
			accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
			accountDao.save(accountFrom);
		} else {
			LOGGER.info("Trying to withdraw funds from a blocked account or or add funds to a non-existing card!");
		}
	}
	
	@Override
	public Payment findPaymentByPaymentId(Long paymentId) {
		return paymentDao.getById(paymentId);
	}
	
	@Override
	public List<Payment> findAllPaymentsByAccountId(Long accountId) {
		return paymentDao.findPaymentsByAccount_AccountId(accountId);
	}
	
	@Override
	public List<Payment> findAllPaymentsByUserId(Long userId) {
		
		return paymentDao.findPaymentsByUserIdOrderByDateDesc(userId);
	}
	
	@Override
	public List<Payment> searchByCriteria(Long userId, Boolean isOutgoing, String startDate, String finalDate) {
		List<Payment> paymentList = new ArrayList<>();

		if (startDate.equals("")) {
			startDate = "01/01/2020 00:00:00";
		} else {
			startDate += " 00:00:00";
		}
		
		if (finalDate.equals("")) {
			paymentList = paymentDao.searchByCriteriaAndFinalDateAsCurrentTimestamp(userId, isOutgoing, startDate);
		} else {
			finalDate += " 23:59:59";
			paymentList = paymentDao.searchByCriteria(userId, isOutgoing, startDate, finalDate);
		}
		return paymentList;
	}

	@Override
	public List<Payment> searchByCriteriaOutgoingFalse(Long userId, Boolean isOutgoing, String startDate, String finalDate) {

		List<Payment> paymentList = new ArrayList<>();;

		if (startDate.equals("")) {
			startDate = "01/01/2020 00:00:00";
		} else {
			startDate += " 00:00:00";
		}

		if (finalDate.equals("")) {
			paymentList = paymentDao.searchByCriteriaAndFinalDateAsCurrentTimestamp(userId, isOutgoing, startDate);
		} else {
			finalDate += " 23:59:59";
			paymentList = paymentDao.searchByCriteria(userId, isOutgoing, startDate, finalDate);
		}
		return paymentList;
	}

	@Override
	public List<Payment> searchByCriteriaWithoutOutgoing(Long userId, String startDate, String finalDate) {
		
		List<Payment> paymentList = new ArrayList<>();;
		
		if (startDate.equals("")) {
			startDate = "01/01/2020 00:00:00";
		} else {
			startDate += " 00:00:00";
		}
		
		if (finalDate.equals("")) {
			paymentList = paymentDao.searchByCriteriaWithoutIsOutgoingAndFinalDateAsCurrentTimestamp(userId, startDate);
		} else {
			finalDate += " 23:59:59";
			paymentList = paymentDao.searchByCriteriaWithoutIsOutgoing(userId, startDate, finalDate);
		}
		return paymentList;
	}
}
