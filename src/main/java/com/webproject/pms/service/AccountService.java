package com.webproject.pms.service;

import com.webproject.pms.model.entities.Account;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

public interface AccountService {
	
	Account save(Account account);
	
	Boolean deleteAccount(Account account);
	
	Boolean blockAccount(Account account);
	
	Boolean unblockAccount(Account account);
	
	Boolean registrationAccount(Account Account, Model model, Principal principal);
	
	List<Account> findAllAccounts();
	
	List<Account> findAllActivateAccountsByUserId(Long userId);
	
	Account findAccountByAccountId(Long accountId);
	
	List<Account> findAllAccountsByUserId(Long userId);
	
	Account findAccountByAccountNumber(String number);
	
	String findAccountNumberByAccountId(Long accountId);
	
	List<Account> searchByCriteria(String number, String min_value, String max_value, String currency);
	
	List<Account> searchByCriteria(Long userId, String number, String min_value, String max_value, String currency);
}
