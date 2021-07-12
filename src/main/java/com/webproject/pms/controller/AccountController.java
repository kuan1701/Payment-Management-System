package com.webproject.pms.controller;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.AccountServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AccountController {
	
	private final AccountServiceImpl accountService;
	
	public AccountController(AccountServiceImpl accountService) {
		this.accountService = accountService;
	}
	
	@GetMapping("/create-account")
	public String createAccount(Model model,
	                            @AuthenticationPrincipal User user)
	{
		model.addAttribute("user", user);
		model.addAttribute("account", new Account());
		return "user/userCreateAccount";
	}
	
	@PostMapping("/create-account")
	public String addAccount(
			@ModelAttribute("account") @Valid Account account,
			BindingResult bindingResult,
			Model model) {
		
		if (bindingResult.hasErrors()){
			return "user/userCreateAccount";
		}
		if (!accountService.registrationAccount(account, model)){
			model.addAttribute("registrationError", "Registration error");
			return "user/userCreateAccount";
		}
		return "redirect:/my-account";
	}
}
