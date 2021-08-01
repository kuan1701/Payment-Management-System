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
	
	public AccountController(AccountServiceImpl accountService, UserServiceImpl userService, BankCardServiceImpl bankCardService) {
		this.accountService = accountService;
		this.userService = userService;
	}
	
	/**
	 * Create account
	 * @param model
	 * @param principal
	 * @return /userCreateAccount page
	 */
	@GetMapping("/create-account")
	public String createAccountPage(Model model,
	                                Principal principal
	) {
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("account", new Account());
		return "user/userCreateAccount";
	}
	
	@PostMapping("/create-account")
	public String createAccount(Model model,
	                            Principal principal,
	                            @ModelAttribute("account") Account account
	) {
		if (!accountService.registrationAccount(account, model, principal)){
			model.addAttribute("accountError", "Create account error");
			return "user/userCreateAccount";
		} else {
			model.addAttribute("accountError", "Create success");
		}
		return "redirect:/my-account";
	}
	
	/**
	 * Show accounts
	 * @param model
	 * @param principal
	 * @return userShowAccountPage
	 */
	@GetMapping("/show-accounts")
	public String showAccounts(Model model,
	                           Principal principal
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Account> accountList = accountService.findAllAccountsByUserId(user.getUserId());
		
		model.addAttribute("user", user);
		model.addAttribute("accountList", accountList);
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		return "user/userShowAccounts";
	}

	@PostMapping("/show-accounts")
	public String showFoundAccounts(Model model,
	                                Principal principal,
                                    @RequestParam("accountNumber") String accountNumber,
                                    @RequestParam("min_value") String min_value,
                                    @RequestParam("max_value") String max_value,
                                    @RequestParam("currency") String currency
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Account> accountList = accountService.searchByCriteria(
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
		return "user/userShowAccounts";
	}
	
	/**
	 * Show account settings
	 * @param model
	 * @param principal
	 * @param number
	 * @return userShowAccountSettings page
	 */
	@GetMapping("/account-setting/{accountNumber}")
	public String showAccountSettingPage(Model model,
                                     Principal principal,
                                     @PathVariable("accountNumber") String number
	) {
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("account", accountService.findAccountByAccountNumber(number));
		return "user/userShowAccountSettings";
	}
	
	/**
	 * Block unblock account
	 * @param model
	 * @param principal
	 * @param number
	 * @return /show-accounts view
	 */
	@PostMapping("/account-setting/{accountNumber}")
	public String blockAccount(Model model,
	                           Principal principal,
	                           @PathVariable("accountNumber") String number
	) {
		Account account = accountService.findAccountByAccountNumber(number);
		
		if (!account.getBlocked()) {
			accountService.blockAccount(account);
		} else if(account.getBlocked()) {
			accountService.unblockAccount(account);
		}
		
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("account", account);
		
		return "redirect:/account-setting/{accountNumber}";
	}
	
	/**
	 * Delete account
	 * @param model
	 * @param principal
	 * @param number
	 * @return show-accounts view
	 */
	@PostMapping("/account-setting/delete/{accountNumber}")
	public String deleteAccount(Model model,
	                           Principal principal,
	                           @PathVariable("accountNumber") String number
	) {
		Account account = accountService.findAccountByAccountNumber(number);
		
		if (account != null) {
			accountService.deleteAccount(account);
		}
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("account", account);
		
		return "redirect:/show-accounts";
	}
}
