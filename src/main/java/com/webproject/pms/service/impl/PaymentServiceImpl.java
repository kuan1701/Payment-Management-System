package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.AccountDao;
import com.webproject.pms.model.dao.PaymentDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.PaymentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

	private final AccountDao accountDao;
	private final PaymentDao paymentDao;
	private final ActionLogServiceImpl actionLogService;

	private List<Payment> paymentList;
	private final StringBuilder stringBuilder = new StringBuilder();
	private static final Logger LOGGER = LogManager.getLogger(PaymentServiceImpl.class);
	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	public PaymentServiceImpl(AccountDao accountDao,
							  PaymentDao paymentDao,
							  ActionLogServiceImpl actionLogService
	) {
		this.accountDao = accountDao;
		this.paymentDao = paymentDao;
		this.actionLogService = actionLogService;
	}

	@Override
	public Payment initializePayment(User user, BigDecimal amount, String appointment) {

		BigDecimal exchangeRate = new BigDecimal("1.00");

		Payment payment = new Payment();
		payment.setUserId(user.getUserId());
		payment.setOutgoing(true);
		payment.setSenderAmount(amount);
		payment.setRecipientAmount(amount.multiply(exchangeRate));
		payment.setExchangeRate(exchangeRate);
		payment.setAppointment(appointment);
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		payment.setDate(formatter.format(new Date()));
		return payment;
	}

	@Override
	public Boolean makePaymentOnAccount(Long accountFromId,
													 String accountToNumber,
													 BigDecimal amount,
													 String appointment,
													 Model model,
													 User user
	) {
		Account accountFrom = accountDao.getById(accountFromId);
		Account accountTo = accountDao.findAccountByNumber(accountToNumber);

		if (accountFrom.getBlocked()) {
			model.addAttribute("paymentError", "senderAccountBlockedError");
			actionLogService.createLog("ERROR: Unsuccessful attempt to make a payment", user);
			LOGGER.error("ERROR: Unsuccessful attempt to make a payment. Sender account blocked\n");
			return false;
		}
		
		if (accountTo.getBlocked()) {
			model.addAttribute("paymentError", "recipientCardNotExistOrBlockedError");
			actionLogService.createLog("ERROR: Unsuccessful attempt to make a payment", user);
			LOGGER.error("ERROR: Unsuccessful attempt to make a payment. Recipient account blocked\n");
			return false;
		}

		// Outgoing payment details
		Payment paymentFrom = initializePayment(user, amount, appointment);
		paymentFrom.setAccount(accountFrom);
		paymentFrom.setRecipientNumber(accountToNumber);
		paymentFrom.setSenderNumber(accountFrom.getNumber());
		paymentFrom.setSenderCurrency(accountFrom.getCurrency());
		paymentFrom.setRecipientCurrency(accountTo.getCurrency());

		// Incoming payment details
		Payment paymentTo = initializePayment(user, amount, appointment);
		paymentTo.setAccount(accountTo);
		paymentTo.setRecipientNumber(accountToNumber);
		paymentTo.setSenderNumber(accountFrom.getNumber());
		paymentTo.setSenderCurrency(accountFrom.getCurrency());
		paymentTo.setRecipientCurrency(accountTo.getCurrency());


		if (checkAvailableAmount(accountFrom, amount)) {
			transaction(accountFrom, accountTo, amount);
			paymentFrom.setNewBalance(accountFrom.getBalance());
			paymentFrom.setStatus(true);
			paymentDao.save(paymentFrom);

			paymentTo.setAccount(accountTo);
			paymentTo.setOutgoing(false);
			paymentTo.setStatus(true);
			paymentTo.setNewBalance(accountTo.getBalance());
			paymentDao.save(paymentTo);
		}
		else {
			paymentFrom.setStatus(false);
			paymentDao.save(paymentFrom);
			model.addAttribute("paymentError", "insufficientFundsError");
			LOGGER.error("Payment arrangement error!\n");
			return false;
		}

		actionLogService.createLog(String.valueOf(stringBuilder
				.append("PAYMENT_COMPLETED: The payment was made from account [")
				.append(accountFrom.getNumber())
				.append("] to account [")
				.append(accountToNumber)
				.append("]")), user);

		LOGGER.info(String.valueOf(stringBuilder
				.append("PAYMENT_COMPLETED: The payment was made from account [")
				.append(accountFrom.getNumber())
				.append("] to account [")
				.append(accountToNumber)
				.append("]\n")));
		return true;
	}
	
	@Override
	public Boolean makePaymentOnCard(Long accountFromId,
									 String cardNumber,
									 BigDecimal amount,
									 String appointment,
									 Model model,
									 User user
	) {
		Account accountFrom = accountDao.getById(accountFromId);

		if (accountFrom.getBlocked()) {
			model.addAttribute("paymentError", "senderAccountBlockedError");
			actionLogService.createLog("ERROR: Unsuccessful attempt to make a payment", user);
			LOGGER.error("ERROR: Unsuccessful attempt to make a payment\n");
			return false;
		}

		// Outgoing payment details
		Payment payment = initializePayment(user, amount, appointment);
		payment.setAccount(accountFrom);
		payment.setSenderNumber(accountFrom.getNumber());
		payment.setSenderCurrency(accountFrom.getCurrency());
		payment.setRecipientNumber(cardNumber);

		if (checkAvailableAmount(accountFrom, amount)) {
			transaction(accountFrom, cardNumber, amount);
			payment.setNewBalance(accountFrom.getBalance());
			payment.setStatus(true);
		}
		else {
			payment.setStatus(false);
			LOGGER.error("Payment arrangement error!");
		}
		paymentDao.save(payment);

		actionLogService.createLog(String.valueOf(stringBuilder
				.append("PAYMENT_COMPLETED: The payment was made from account [")
				.append(accountFrom.getNumber())
				.append("] to account [")
				.append(cardNumber)
				.append("]")), user);

		LOGGER.info(String.valueOf(stringBuilder
				.append("PAYMENT_COMPLETED: The payment was made from account [")
				.append(accountFrom.getNumber())
				.append("] to account [")
				.append(cardNumber)
				.append("]\n")));
		return true;
	}
	
	@Override
	public Boolean checkAvailableAmount(Account account, BigDecimal amount) {
		return account.getBalance().compareTo(amount) >= 0;
	}

	//Account-to-account transaction
	@Override
	public void transaction(Account accountFrom, Account accountTo, BigDecimal amount) {
		
		if (!accountFrom.getBlocked() && !accountTo.getBlocked()) {
			accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
			accountTo.setBalance(accountTo.getBalance().add(amount));
			accountDao.save(accountFrom);
			accountDao.save(accountTo);
		} else {
			LOGGER.error("Trying to withdraw or add funds to a blocked account!\n");
		}
	}

	//Account-to-card transaction
	@Override
	public void transaction(Account accountFrom, String cardNumber, BigDecimal amount) {
		
		if (!accountFrom.getBlocked()) {
			accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
			accountDao.save(accountFrom);
		} else {
			LOGGER.error("Trying to withdraw funds from a blocked account or or add funds to a non-existing card!\n");
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
	public List<Payment> searchByCriteria(Long userId,
										  Boolean isOutgoing,
										  String startDate,
										  String finalDate
	) {
		if (StringUtils.isEmpty(startDate)) {
			startDate = "01/01/2020 00:00:00";
		} else {
			startDate += " 00:00:00";
		}
		
		if (StringUtils.isEmpty(finalDate)) {
			paymentList = paymentDao.searchByCriteriaAndFinalDateAsCurrentTimestamp(userId, isOutgoing, startDate);
		} else {
			finalDate += " 23:59:59";
			paymentList = paymentDao.searchByCriteria(userId, isOutgoing, startDate, finalDate);
		}
		return paymentList;
	}

	@Override
	public List<Payment> searchByCriteriaOutgoingFalse(Long userId,
													   Boolean isOutgoing,
													   String startDate,
													   String finalDate)
	{
		if (StringUtils.isEmpty(startDate)) {
			startDate = "01/01/2020 00:00:00";
		} else {
			startDate += " 00:00:00";
		}

		if (StringUtils.isEmpty(finalDate)) {
			paymentList = paymentDao.searchByCriteriaAndFinalDateAsCurrentTimestamp(userId, isOutgoing, startDate);
		} else {
			finalDate += " 23:59:59";
			paymentList = paymentDao.searchByCriteria(userId, isOutgoing, startDate, finalDate);
		}
		return paymentList;
	}

	@Override
	public List<Payment> searchByCriteriaWithoutOutgoing(Long userId, String startDate, String finalDate) {
		
		if (StringUtils.isEmpty(startDate)) {
			startDate = "01/01/2020 00:00:00";
		} else {
			startDate += " 00:00:00";
		}
		
		if (StringUtils.isEmpty(finalDate)) {
			paymentList = paymentDao.searchByCriteriaWithoutIsOutgoingAndFinalDateAsCurrentTimestamp(userId, startDate);
		} else {
			finalDate += " 23:59:59";
			paymentList = paymentDao.searchByCriteriaWithoutIsOutgoing(userId, startDate, finalDate);
		}
		return paymentList;
	}
}
