package com.webproject.pms.controller;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CardController {
	
	@GetMapping("/attach-card")
	public String attachCard(Model model)
	{
		model.addAttribute("bankCard", new BankCard());
		return "user/userAttachCard";
	}
}
