package com.webproject.pms.controller.user;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.AccountServiceImpl;
import com.webproject.pms.service.impl.BankCardServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class AccountController {
	
	private final AccountServiceImpl accountService;
	private final UserServiceImpl userService;

	private List<Account> accountList;
	
	public AccountController(AccountServiceImpl accountService, UserServiceImpl userService, BankCardServiceImpl bankCardService) {
		this.accountService = accountService;
		this.userService = userService;
	}
	
	/**
	 * User account creation form
	 * @param model Model
	 * @param principal Principal
	 * @return userCreateAccount view
	 */
	@GetMapping("/create-account")
	public String createAccountForm(Model model,
	                                Principal principal
	) {
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("account", new Account());
		return "user/userCreateAccount";
	}
	
	/**
	 * User account creation
	 * @param model Model
	 * @param principal Principal
	 * @param account Account
	 * @return user/userCreateAccount view
	 */
	@PostMapping("/create-account")
	public String createAccount(Model model,
	                            Principal principal,
	                            @ModelAttribute("account") Account account
	) {
		if (!accountService.registrationAccount(account, principal)){
			model.addAttribute("accountError", "Create account error");
		}
		else {
			model.addAttribute("accountError", "Create success");
		}
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		return "user/userCreateAccount";
	}
	
	/**
	 * Show accounts
	 * @param model Model
	 * @param principal Principal
	 * @return userShowAccount view
	 */
	@GetMapping("/show-accounts")
	public String showAccounts(Model model,
	                           Principal principal
	) {
		User user = userService.findUserByUsername(principal.getName());
		accountList = accountService.findAllAccountsByUserId(user.getUserId());
		
		model.addAttribute("user", user);
		model.addAttribute("accountList", accountList);
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		return "user/userShowAccounts";
	}
	
	/**
	 * User show found accounts
	 * @param model Model
	 * @param principal Principal
	 * @param accountNumber String
	 * @param min_value String
	 * @param max_value String
	 * @param currency String
	 * @return userShowAccounts view
	 */
	@PostMapping("/show-accounts")
	public String showFoundAccounts(Model model,
	                                Principal principal,
									@RequestParam("currency") String currency,
									@RequestParam("min_value") String min_value,
									@RequestParam("max_value") String max_value,
									@RequestParam("accountNumber") String accountNumber
									) {
		User user = userService.findUserByUsername(principal.getName());
		accountList = accountService.searchByCriteria(
				user.getUserId(),
				accountNumber,
				min_value,
				max_value,
				currency);
		
		model.addAttribute("user", user);
		model.addAttribute("currency", currency);
		model.addAttribute("min_value", min_value);
		model.addAttribute("max_value", max_value);
		model.addAttribute("accountList", accountList);
		model.addAttribute("accountNumber", accountNumber);
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		model.addAttribute("numberOfAccounts", accountList.size());
		model.addAttribute("response", "searchAccountsSuccess");
		return "user/userShowAccounts";
	}
	
	/**
	 * Show account settings
	 * @param model Model
	 * @param principal Principal
	 * @param accountNumber String
	 * @return userShowAccountSettings view
	 */
	@GetMapping("/account-setting/{accountNumber}")
	public String showAccountSettingPage(Model model,
										 Principal principal,
										 @PathVariable("accountNumber") String accountNumber
	) {
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("account", accountService.findAccountByAccountNumber(accountNumber));
		return "user/userShowAccountSettings";
	}
	
	/**
	 * Blocking and unblocking of the account by the user
	 * @param model Model
	 * @param principal Principal
	 * @param accountNumber String
	 * @return userShowAccountSettings view
	 */
	@PostMapping("/account-setting/{accountNumber}")
	public String blockAccount(Model model,
	                           Principal principal,
	                           @PathVariable("accountNumber") String accountNumber
	) {
		User user = userService.findUserByUsername(principal.getName());
		Account account = accountService.findAccountByAccountNumber(accountNumber);

		if (!account.getBlocked()) {
			accountService.blockAccount(account);
			model.addAttribute("response", "accountBlockedSuccess");
		}
		else if(account.getBlocked()) {
			accountService.unblockAccount(account);
			model.addAttribute("response", "accountUnblockedSuccess");
		}
		model.addAttribute("user", user);
		model.addAttribute("account", account);
		return "user/userShowAccountSettings";
	}
	
	/**
	 * Deleting an account by a user
	 * @param model Model
	 * @param principal Principal
	 * @param accountNumber String
	 * @return redirect:/my-account page
	 */
	@PostMapping("/account-setting/delete/{accountNumber}")
	public String deleteAccount(Model model,
	                           Principal principal,
	                           @PathVariable("accountNumber") String accountNumber
	) {
		Account account = accountService.findAccountByAccountNumber(accountNumber);
		User user = userService.findUserByUsername(principal.getName());
		
		if (account != null) {
			if (!accountService.deleteAccount(account)){
				model.addAttribute("response", "accountHasFundsError");
			}
		}
		model.addAttribute("user", user);
		model.addAttribute("account", account);
		return "redirect:/my-account";
	}
}
