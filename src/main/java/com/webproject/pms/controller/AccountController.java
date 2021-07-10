package com.webproject.pms.controller;

import com.webproject.pms.model.entities.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
	
	@GetMapping("/create-account")
	public String createAccount(Model model)
	{
		model.addAttribute("account", new Account());
		return "user/userCreateAccount";
	}
}
