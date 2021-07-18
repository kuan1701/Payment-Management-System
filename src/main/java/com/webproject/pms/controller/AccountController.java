package com.webproject.pms.controller;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.service.impl.AccountServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class AccountController {
	
	private final AccountServiceImpl accountService;
	private final UserServiceImpl userService;
	
	public AccountController(AccountServiceImpl accountService, UserServiceImpl userService) {
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
	                                Principal principal)
	{
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("account", new Account());
		return "user/userCreateAccount";
	}
	
	@PostMapping("/create-account")
	public String createAccount(
			@ModelAttribute("account") Account account,
			BindingResult bindingResult,
			Principal principal,
			Model model) {
		
		if (bindingResult.hasErrors()){
			model.addAttribute("accountError", "Create error");
			return "user/userCreateAccount";
		}
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
		List<Account> accountList = accountService.findAllAccountsByUserId(
				userService.findUserByUsername(principal.getName()).getUserId());
		
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		model.addAttribute("accountList", accountList);
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
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
}
