package com.webproject.pms.service.impl;

import com.webproject.pms.model.dao.AccountDao;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
	
	private final UserDao userDao;
	private final AccountDao accountDao;
	private final ActionLogServiceImpl actionLogService;

	private final String STARTBALANCE = "10000.00";
	private static final Logger LOGGER = LogManager.getLogger(AccountServiceImpl.class);

	@Autowired
	public AccountServiceImpl(UserDao userDao,
							  AccountDao accountDao,
							  ActionLogServiceImpl actionLogService) {

		this.userDao = userDao;
		this.accountDao = accountDao;
		this.actionLogService = actionLogService;
	}
	
	@Override
	@Transactional
	public Boolean save(Account account) {

		accountDao.save(account);
		return true;
	}
	
	@Override
	@Transactional
	public Boolean registrationAccount(Account account, Principal principal) {

		StringBuilder stringBuilder = new StringBuilder();
		User user = userDao.findUserByUsername(principal.getName());
		
		if (accountDao.findAccountByNumber(account.getNumber()) != null) {
			actionLogService.createLog("ERROR: Unsuccessful attempt to create a new account", user);
			LOGGER.error("ERROR: Unsuccessful attempt to create a new account\n");
			return false;
		}
		account.setUser(user);
		account.setBlocked(false);
		account.setDeleted(false);
		account.setBalance(new BigDecimal(STARTBALANCE));
		accountDao.save(account);

		actionLogService.createLog(String.valueOf(stringBuilder.append("CREATED: Account [")
				.append(account.getNumber())
				.append(", ")
				.append(account.getCurrency())
				.append("]")), user);

		LOGGER.info(String.valueOf(stringBuilder.append("CREATED: Account [")
				.append(account.getNumber())
				.append(", ")
				.append(account.getCurrency())
				.append("]")));
		return true;
	}
	
	@Override
	@Transactional
	public Boolean adminAttachAccount(Account account, Long userId) {

		StringBuilder stringBuilder = new StringBuilder();
		User user = userDao.getById(userId);
		
		if (accountDao.findAccountByNumber(account.getNumber()) != null) {
			actionLogService.createLog("ERROR: Unsuccessful attempt to create a new account", user);
			LOGGER.error("ERROR: Unsuccessful attempt to create a new account\n");
			return false;
		}
		account.setUser(user);
		account.setBlocked(false);
		account.setDeleted(false);
		account.setBalance(new BigDecimal(STARTBALANCE));
		accountDao.save(account);

		actionLogService.createLog(String.valueOf(stringBuilder.append("CREATED: Account [")
				.append(account.getNumber())
				.append(", ")
				.append(account.getCurrency())
				.append("]")), user);

		LOGGER.info(String.valueOf(stringBuilder.append("CREATED: Account [")
				.append(account.getNumber())
				.append(", ")
				.append(account.getCurrency())
				.append("]")));
		return true;
	}
	
	@Override
	@Transactional
	public Boolean blockAccount(Account account) {

		StringBuilder stringBuilder = new StringBuilder();

		if (account != null) {
			account.setBlocked(true);
			accountDao.save(account);

			actionLogService.createLog(String.valueOf(stringBuilder.append("BLOCKED: Account [")
					.append(account.getNumber())
					.append("]")), account.getUser());

			LOGGER.info(String.valueOf(stringBuilder.append("BLOCKED: Account [")
					.append(account.getNumber())
					.append("]\n")));
			return true;
		}
		LOGGER.info(String.valueOf(stringBuilder.append("ERROR_BLOCKED: Account [")
				.append(account.getNumber())
				.append("]\n")));
		return false;
	}
	
	@Override
	@Transactional
	public Boolean unblockAccount(Account account) {

		StringBuilder stringBuilder = new StringBuilder();

		if (account != null) {
			account.setBlocked(false);
			accountDao.save(account);

			actionLogService.createLog(String.valueOf(stringBuilder.append("UNBLOCKED: Account [")
					.append(account.getNumber())
					.append("]")), account.getUser());

			LOGGER.info(String.valueOf(stringBuilder.append("UNBLOCKED: Account [")
					.append(account.getNumber())
					.append("]\n")));
			return true;
		}
		LOGGER.info(String.valueOf(stringBuilder.append("ERROR_UNBLOCKED: Account [")
				.append(account.getNumber())
				.append("]\n")));
		return false;
	}
	
	@Override
	@Transactional
	public Boolean deleteAccount(Account account) {

		StringBuilder stringBuilder = new StringBuilder();
		
		BigDecimal balance = account.getBalance();
		if (balance.compareTo(BigDecimal.ZERO) != 0) {

			actionLogService.createLog(String.valueOf(stringBuilder
					.append("ERROR: Unsuccessful attempt to delete account [")
					.append(account.getNumber())
					.append("]")), account.getUser());

			LOGGER.error(String.valueOf(stringBuilder
					.append("ERROR: Unsuccessful attempt to delete account [")
					.append(account.getNumber())
					.append("]\n")));
			return false;
		}
		accountDao.delete(account);

		actionLogService.createLog(String.valueOf(stringBuilder.append("DELETED: Account [")
				.append(account.getNumber())
				.append("]")), account.getUser());

		LOGGER.info(String.valueOf(stringBuilder.append("DELETED: Account [")
				.append(account.getNumber())
				.append("]\n")));
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
	public List<Account> searchByCriteriaWithoutId(String number,
												   String min_value,
												   String max_value,
												   String currency
	) {
		return accountDao.searchByCriteriaWithoutId(number, min_value, max_value, currency);
	}
	
	@Override
	public List<Account> searchByCriteria(Long userId,
										  String number,
										  String min_value,
										  String max_value,
										  String currency
	) {
		return accountDao.searchByCriteria(userId, number, min_value, max_value, currency);
	}
}
