package com.webproject.pms.controller;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import com.webproject.pms.model.entities.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CardController {
	
	@GetMapping("/attach-card")
	public String attachCard(Model model,
	                         @AuthenticationPrincipal User user)
	{
		model.addAttribute("user", user);
		model.addAttribute("bankCard", new BankCard());
		return "user/userAttachCard";
	}
}
