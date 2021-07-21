package com.webproject.pms.controller;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.BankCard;
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
public class CardController {
	
	private final BankCardServiceImpl bankCardService;
	private final AccountServiceImpl accountService;
	private final UserServiceImpl userService;
	
	public CardController(BankCardServiceImpl bankCardService, AccountServiceImpl accountService, UserServiceImpl userService) {
		this.bankCardService = bankCardService;
		this.accountService = accountService;
		this.userService = userService;
	}
	
	/**
	 * Attach card tto account
	 * @param model
	 * @param principal
	 * @return userAttachCard page
	 */
	@GetMapping("/attach-card")
	public String attachCard(Model model,
	                         Principal principal
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Account> accounts = accountService.findAllAccountsByUserId(user.getUserId());
		
		model.addAttribute("user", user);
		model.addAttribute("accounts", accounts);
		model.addAttribute("bankCard", new BankCard());
		return "user/userAttachCard";
	}
	
	@PostMapping("/attach-card")
	public String attachCard(
			Model model,
			Principal principal,
			@RequestParam("accountNumber") String accountNumber,
			@ModelAttribute("card") BankCard bankCard
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Account> accounts = accountService.findAllAccountsByUserId(user.getUserId());
		Account account = accountService.findAccountByAccountId(Long.parseLong(accountNumber));
		
		model.addAttribute("user", user);
		model.addAttribute("accounts", accounts);
		model.addAttribute("card", bankCard);
		
		
		if (!bankCardService.addNewBankCard(bankCard, account)){
			model.addAttribute("cardError", "Card attachment error");
			return "user/userAttachCard";
		} else {
			model.addAttribute("accountError", "Card attached successfully");
		}
		return "user/userAttachCard";
	}
	
	/**
	 * Attach card
	 * @param model
	 * @param principal
	 * @param number
	 * @return userShowAccountCards page
	 */
	@GetMapping("/attached-cards/{accountNumber}")
	public String showAttachedCards(Model model,
	                                Principal principal,
	                                @PathVariable("accountNumber") String number
	) {
		List<BankCard> bankCardList = bankCardService.findCardsByAccountId(
				accountService.findAccountByAccountNumber(number).getAccountId());
		
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("bankCardList", bankCardList);
		model.addAttribute("cardsEmpty", bankCardList.isEmpty());
		model.addAttribute("account", accountService.findAccountByAccountNumber(number));
		return "user/userShowAccountCards";
	}
	
	
}
