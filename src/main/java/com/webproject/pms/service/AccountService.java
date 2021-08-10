package com.webproject.pms.service;

import com.webproject.pms.model.entities.Account;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

public interface AccountService {

	Boolean save(Account account);
	
	Boolean deleteAccount(Account account);
	
	Boolean blockAccount(Account account);
	
	Boolean unblockAccount(Account account);
	
	Boolean registrationAccount(Account Account, Principal principal);
	
	Boolean adminAttachAccount(Account Account, Long userId);
	
	List<Account> findAllAccounts();
	
	List<Account> findAllActivateAccountsByUserId(Long userId);
	
	Account findAccountByAccountId(Long accountId);
	
	List<Account> findAllAccountsByUserId(Long userId);
	
	Account findAccountByAccountNumber(String number);
	
	List<Account> searchByCriteriaWithoutId(String number, String min_value, String max_value, String currency);
	
	List<Account> searchByCriteria(Long userId, String number, String min_value, String max_value, String currency);
}
