package com.webproject.pms.service.impl;

import com.webproject.pms.mappers.MapStructMapper;
import com.webproject.pms.model.dao.AccountDao;
import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.dto.AccountDto;
import com.webproject.pms.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
	
	private static final Logger LOGGER = LogManager.getLogger(AccountServiceImpl.class);
	private final AccountDao accountDao;
	private final MapStructMapper mapStructMapper;
	
	@Autowired
	public AccountServiceImpl(AccountDao accountDao, MapStructMapper mapStructMapper) {
		
		this.accountDao = accountDao;
		this.mapStructMapper = mapStructMapper;
	}
	
	@Override
	@Transactional
	public Account save(Account account) {
		return accountDao.save(account);
	}
	
	@Override
	@Transactional
	public Boolean registrationAccount(Account account) {
		
		if (accountDao.findAccountByNumber(account.getNumber()) == null) {
			account.setBalance(new BigDecimal("0.00"));
			account.setBlocked(false);
			account.setDeleted(false);
			
			accountDao.save(account);
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional
	public Boolean blockAccount(Long accountId) {
		
		if (accountId != null) {
			Account account = accountDao.getOne(accountId);
			account.setBlocked(true);
			accountDao.save(account);
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional
	public Boolean unblockAccount(Long accountId) {
		
		if (accountId != null) {
			Account account = accountDao.getOne(accountId);
			account.setBlocked(false);
			accountDao.save(account);
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional
	public Boolean deleteAccountByAccountId(Long accountId) {
		
		if (accountDao.existsById(accountId)) {
			accountDao.deleteById(accountId);
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional
	public AccountDto findAccountByAccountId(Long accountId) {
		
		return mapStructMapper.accountToAccountDto(
				accountDao.getOne(accountId));
	}
	
	@Override
	@Transactional
	public AccountDto findAccountByAccountNumber(String number) {
		
		return mapStructMapper.accountToAccountDto(
				accountDao.findAccountByNumber(number));
	}
	
	@Override
	@Transactional
	public String findAccountNumberByAccountId(Long accountId) {
		
		return findAccountByAccountId(accountId).getNumber();
	}
	
	@Override
	@Transactional
	public List<AccountDto> findAllAccountsByUserId(Long userId) {
		
		return mapStructMapper.accountsToAccountDtos(
				accountDao.findAllAccountsByUser_UserId(userId));
	}
	
	@Override
	@Transactional
	public List<AccountDto> findAllAccounts() {
		
		return mapStructMapper.accountsToAccountDtos(
				accountDao.findAll());
	}
	
	@Override
	@Transactional
	public List<AccountDto> searchByCriteria(String number, String min_value, String max_value, String currency) {
		
		return mapStructMapper.accountsToAccountDtos(
				accountDao.searchByCriteria(number, min_value, max_value, currency));
	}
	
	@Override
	@Transactional
	public List<AccountDto> searchByCriteria(Long userId, String number, String min_value, String max_value, String currency) {
		
		return mapStructMapper.accountsToAccountDtos(
				accountDao.searchByCriteria(userId, number, min_value, max_value, currency));
	}
}
