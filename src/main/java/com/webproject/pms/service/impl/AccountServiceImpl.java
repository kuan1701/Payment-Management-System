package com.webproject.pms.service.impl;

import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.AccountDao;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.dto.AccountDto;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
	
	private static final Logger LOGGER = LogManager.getLogger(AccountServiceImpl.class);
	private final AccountDao accountDao;
	private final UserDao userDao;
	private final MapStructMapper mapStructMapper;
	
	@Autowired
	public AccountServiceImpl(AccountDao accountDao, UserDao userDao, MapStructMapper mapStructMapper) {
		
		this.accountDao = accountDao;
		this.userDao = userDao;
		this.mapStructMapper = mapStructMapper;
	}
	
	@Override
	public Account save(Account account) {
		return accountDao.save(account);
	}
	
	@Override
	public Boolean registrationAccount(Account account, Model model, Principal principal) {
		
		User user = userDao.findUserByUsername(principal.getName());
		
		if (accountDao.findAccountByNumber(account.getNumber()) != null) {
			return false;
		}
		model.addAttribute("accountNumberError", "This account already exist");
		account.setUser(user);
		account.setBalance(new BigDecimal("0.00"));
		account.setBlocked(false);
		account.setDeleted(false);
		
		accountDao.save(account);
		return true;
	}
	
	@Override
	public Boolean adminAttachAccount(Account account, Model model, Long userId) {
		
		User user = userDao.getById(userId);
		
		if (accountDao.findAccountByNumber(account.getNumber()) != null) {
			return false;
		}
		model.addAttribute("accountNumberError", "This account already exist");
		account.setUser(user);
		account.setBalance(new BigDecimal("0.00"));
		account.setBlocked(false);
		account.setDeleted(false);
		
		accountDao.save(account);
		return true;
	}
	
	@Override
	public Boolean blockAccount(Account account) {
		
		account.setBlocked(true);
		accountDao.save(account);
		return true;
	}
	
	@Override
	public Boolean unblockAccount(Account account) {
		
		account.setBlocked(false);
		accountDao.save(account);
		return true;
	}
	
	@Override
	public Boolean deleteAccount(Account account) {
		
		BigDecimal balance = account.getBalance();
		if (balance.compareTo(BigDecimal.ZERO) != 0) {
			return false;
		}
		accountDao.delete(account);
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
	public String findAccountNumberByAccountId(Long accountId) {
		return findAccountByAccountId(accountId).getNumber();
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
