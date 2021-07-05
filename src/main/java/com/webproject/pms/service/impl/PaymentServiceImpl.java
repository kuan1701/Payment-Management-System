package com.webproject.pms.service.impl;

import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.AccountDao;
import com.webproject.pms.model.dao.PaymentDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.dto.PaymentGetDto;
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
	@Transactional
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
	@Transactional
	public synchronized void makePaymentOnCard(Long accountId, String cardNumber, BigDecimal amount, String appointment) {
	
	}
	
	@Override
	@Transactional
	public synchronized boolean checkAvailableAccount(Account account) {
		
		return account.getBlocked();
	}
	
	@Override
	@Transactional
	public synchronized boolean checkAvailableCard(BankCard card) {
		
		return card.getActive();
	}
	
	@Override
	@Transactional
	public synchronized boolean checkAvailableAmount(Account account, BigDecimal amount) {
		
		BigDecimal balance = account.getBalance();
		return balance.compareTo(amount) >= 0;
	}
	
	@Override
	@Transactional
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
	@Transactional
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
	@Transactional
	public PaymentGetDto findPaymentByPaymentId(Long paymentId) {
		
		return mapStructMapper.paymentToPaymentGetDto(paymentDao.getOne(paymentId));
	}
	
	@Override
	@Transactional
	public List<PaymentGetDto> findAllPaymentsByAccountId(Long accountId) {
		
		return mapStructMapper.paymentsToPaymentGetDtos(
				paymentDao.findPaymentsByAccount_AccountId(accountId));
	}
	
	@Override
	@Transactional
	public List<PaymentGetDto> findAllPaymentsByUserId(Long userId) {
		
		return mapStructMapper.paymentsToPaymentGetDtos(
				paymentDao.findPaymentsByUserId(userId));
	}
	
	@Override
	@Transactional
	public List<PaymentGetDto> findLastPaymentsByUserId(Long userId) {
		
		return mapStructMapper.paymentsToPaymentGetDtos(
				paymentDao.findLastPaymentsByAccountUserId(userId));
	}
	
	@Override
	@Transactional
	public List<PaymentGetDto> findAllPayments() {
		
		return mapStructMapper.paymentsToPaymentGetDtos(
				paymentDao.findAll());
	}
	
	@Override
	@Transactional
	public List<PaymentGetDto> searchByCriteria(Long userId, Boolean isOutgoing, String startDate, String finalDate) {
		
		return mapStructMapper.paymentsToPaymentGetDtos(
				paymentDao.searchByCriteria(userId, isOutgoing, startDate, finalDate));
	}
	
	@Override
	@Transactional
	public List<PaymentGetDto> searchByCriteria(Long userId, String startDate, String finalDate) {
		
		return mapStructMapper.paymentsToPaymentGetDtos(
				paymentDao.searchByCriteria(userId, startDate, finalDate));
	}
}
