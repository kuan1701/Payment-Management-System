package com.webproject.pms.service.impl;

import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.AccountDao;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
	
	private final UserDao userDao;
	private final AccountDao accountDao;
	private final MapStructMapper mapStructMapper;
	private final ActionLogServiceImpl actionLogService;
	private static final Logger LOGGER = LogManager.getLogger(AccountServiceImpl.class);

	@Autowired
	public AccountServiceImpl(UserDao userDao,
							  AccountDao accountDao,
							  MapStructMapper mapStructMapper,
							  ActionLogServiceImpl actionLogService) {

		this.userDao = userDao;
		this.accountDao = accountDao;
		this.mapStructMapper = mapStructMapper;
		this.actionLogService = actionLogService;
	}
	
	@Override
	public Boolean save(Account account) {

		accountDao.save(account);
		return true;
	}
	
	@Override
	public Boolean registrationAccount(Account account, Principal principal) {
		
		User user = userDao.findUserByUsername(principal.getName());
		
		if (accountDao.findAccountByNumber(account.getNumber()) != null) {
			actionLogService.createLog("ERROR: Unsuccessful attempt to create a new account", user);
			LOGGER.error("ERROR: Unsuccessful attempt to create a new account");
			return false;
		}
		account.setUser(user);
		account.setBlocked(false);
		account.setDeleted(false);
		account.setBalance(new BigDecimal("10000.00"));
		accountDao.save(account);

		actionLogService.createLog("CREATED: Account [" + account.getNumber() + ", " + account.getCurrency() + "]", user);
		LOGGER.info("CREATED: Account [" + account.getNumber() + ", " + account.getCurrency() + "]");
		return true;
	}
	
	@Override
	public Boolean adminAttachAccount(Account account, Long userId) {
		
		User user = userDao.getById(userId);
		
		if (accountDao.findAccountByNumber(account.getNumber()) != null) {
			actionLogService.createLog("ERROR: Unsuccessful attempt to create a new account", user);
			LOGGER.error("ERROR: Unsuccessful attempt to create a new account");
			return false;
		}
		account.setUser(user);
		account.setBlocked(false);
		account.setDeleted(false);
		account.setBalance(new BigDecimal("10000.00"));
		accountDao.save(account);

		actionLogService.createLog("CREATED: Account [" + account.getNumber() + ", " + account.getCurrency() + "]", user);
		LOGGER.info("CREATED: Account [" + account.getNumber() + ", " + account.getCurrency() + "]");
		return true;
	}
	
	@Override
	public Boolean blockAccount(Account account) {

		if (account != null) {
			account.setBlocked(true);
			accountDao.save(account);

			actionLogService.createLog("BLOCKED: Account [" + account.getNumber() + "]", account.getUser());
			LOGGER.info("BLOCKED: Account [" + account.getNumber() + ", " + account.getCurrency() + "]");
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean unblockAccount(Account account) {

		if (account != null) {
			account.setBlocked(false);
			accountDao.save(account);

			actionLogService.createLog("UNBLOCKED: Account [" + account.getNumber() + "]", account.getUser());
			LOGGER.info("UNBLOCKED: Account [" + account.getNumber() + ", " + account.getCurrency() + "]");
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean deleteAccount(Account account) {
		
		BigDecimal balance = account.getBalance();
		if (balance.compareTo(BigDecimal.ZERO) != 0) {
			actionLogService.createLog("ERROR: Unsuccessful attempt to delete account [" + account.getNumber() + "]", account.getUser());
			LOGGER.error("ERROR: Unsuccessful attempt to delete account [" + account.getNumber() + "]");
			return false;
		}
		accountDao.delete(account);
		actionLogService.createLog("DELETED: Account [" + account.getNumber() + "]", account.getUser());
		LOGGER.info("DELETED: Account [" + account.getNumber() + ", " + account.getCurrency() + "]");
		return true;
	}
	
	@Override
	public Account findAccountByAccountId(Long accountId) {
		return accountDao.getById(accountId);
	}
	
	@Override
	public Account findAccountByAccountNumber(String number) {
		return accountDao.findAccountByNumber(number);
	}

	@Override
	public List<Account> findAllAccountsByUserId(Long userId) {
		return accountDao.findAllAccountsByUser_UserId(userId);
	}
	
	@Override
	public List<Account> findAllActivateAccountsByUserId(Long userId) {
		return accountDao.findAllActivateAccountByUserId(userId);
	}
	
	@Override
	public List<Account> findAllAccounts() {
		return accountDao.findAll();
	}
	
	@Override
	public List<Account> searchByCriteriaWithoutId(String number, String min_value, String max_value, String currency) {
		
		return accountDao.searchByCriteriaWithoutId(number, min_value, max_value, currency);
	}
	
	@Override
	public List<Account> searchByCriteria(Long userId, String number, String min_value, String max_value, String currency) {
		
		return accountDao.searchByCriteria(userId, number, min_value, max_value, currency);
	}
}
