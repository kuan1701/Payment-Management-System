package com.webproject.pms.service;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.dto.AccountDto;
import org.springframework.ui.Model;

import java.util.List;

public interface AccountService {
	
	Account save(Account account);
	
	Boolean deleteAccountByAccountId(Long accountId);
	
	Boolean blockAccount(Long accountId);
	
	Boolean unblockAccount(Long accountId);
	
	Boolean registrationAccount(Account Account, Model model);
	
	List<AccountDto> findAllAccounts();
	
	AccountDto findAccountByAccountId(Long accountId);
	
	List<AccountDto> findAllAccountsByUserId(Long userId);
	
	AccountDto findAccountByAccountNumber(String number);
	
	String findAccountNumberByAccountId(Long accountId);
	
	List<AccountDto> searchByCriteria(String number, String min_value, String max_value, String currency);
	
	List<AccountDto> searchByCriteria(Long userId, String number, String min_value, String max_value, String currency);
}
