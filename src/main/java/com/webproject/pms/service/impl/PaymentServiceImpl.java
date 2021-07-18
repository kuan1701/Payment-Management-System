package com.webproject.pms.service.impl;

import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.AccountDao;
import com.webproject.pms.model.dao.PaymentDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.service.PaymentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
	
	private static final Logger LOGGER = LogManager.getLogger(PaymentServiceImpl.class);
	private final AccountDao accountDao;
	private final PaymentDao paymentDao;
	private final MapStructMapper mapStructMapper;
	
	@Autowired
	public PaymentServiceImpl(AccountDao accountDao, PaymentDao paymentDao, MapStructMapper mapStructMapper) {
		this.accountDao = accountDao;
		this.paymentDao = paymentDao;
		this.mapStructMapper = mapStructMapper;
	}
	
	@Override
	public synchronized Payment makePaymentOnAccount(Long accountId, String accountNumber, BigDecimal amount, BigDecimal exchangeRate, String appointment) {
		
		Account accountFrom = accountDao.getOne(accountId);
		Account accountTo = accountDao.findAccountByNumber(accountNumber);
		
		if (checkAvailableAccount(accountFrom)) {
			LOGGER.error("Sender account is blocked");
		}
		
		if (checkAvailableAccount(accountTo)) {
			LOGGER.error("Recipient account is blocked");
		}
		
		// Outgoing payment details
		Payment payment = new Payment();
		payment.setAccount(accountFrom);
		payment.setOutgoing(true);
		payment.setSenderNumber(accountFrom.getNumber());
		payment.setSenderAmount(amount);
		payment.setSenderCurrency(accountFrom.getCurrency());
		payment.setRecipientNumber(accountNumber);
		payment.setRecipientAmount(amount.multiply(exchangeRate));
		payment.setRecipientCurrency(accountTo.getCurrency());
		payment.setExchangeRate(exchangeRate);
		payment.setAppointment(appointment);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		payment.setDate(formatter.format(new Date()));
		
		if (checkAvailableAmount(accountFrom, amount)) {
			transaction(accountFrom, accountTo, amount);
			payment.setNewBalance(accountFrom.getBalance());
			payment.setCondition(true);
			paymentDao.save(payment);
			
			// Incoming payment details
			payment.setAccount(accountTo);
			payment.setOutgoing(false);
			payment.setNewBalance(accountTo.getBalance());
			paymentDao.save(payment);
		} else {
			LOGGER.error("Payment arrangement error!");
		}
		return payment;
	}
	
	@Override
	public synchronized void makePaymentOnCard(Long accountId, String cardNumber, BigDecimal amount, String appointment) {
	
	}
	
	@Override
	public synchronized boolean checkAvailableAccount(Account account) {
		
		return account.getBlocked();
	}
	
	@Override
	public synchronized boolean checkAvailableCard(BankCard card) {
		
		return card.getActive();
	}
	
	@Override
	public synchronized boolean checkAvailableAmount(Account account, BigDecimal amount) {
		
		BigDecimal balance = account.getBalance();
		return balance.compareTo(amount) >= 0;
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
			
			// [Add amount on bank card]
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
		
		return paymentDao.findPaymentsByUserId(userId);
	}
	
	@Override
	public List<Payment> findLastPaymentsByUserId(Long userId) {
		
		return paymentDao.findLastPaymentsByAccountUserId(userId);
	}
	
	@Override
	public List<Payment> findAllPayments() {
		
		return paymentDao.findAll();
	}
	
	@Override
	public List<Payment> searchByCriteria(Long userId, Boolean isOutgoing, String startDate, String finalDate) {
		
		return paymentDao.searchByCriteria(userId, isOutgoing, startDate, finalDate);
	}
	
	@Override
	public List<Payment> searchByCriteria(Long userId, String startDate, String finalDate) {
		
		return paymentDao.searchByCriteria(userId, startDate, finalDate);
	}
}
