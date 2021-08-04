package com.webproject.pms.controller.user;

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
		List<Account> accounts = accountService.findAllActivateAccountsByUserId(user.getUserId());
		
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
		List<Account> accounts = accountService.findAllActivateAccountsByUserId(user.getUserId());
		Account account = accountService.findAccountByAccountId(Long.parseLong(accountNumber));
		
		model.addAttribute("user", user);
		model.addAttribute("accounts", accounts);
		model.addAttribute("bankCard", bankCard);
		
		
		if (!bankCardService.addNewBankCard(bankCard, account)) {
			model.addAttribute("user", user);
			model.addAttribute("cardError", "Card attachment error");
			return "user/userAttachCard";
		} else {
			model.addAttribute("cardError", "Card attached successfully");
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
		List<BankCard> bankCardsList = bankCardService.findCardsByAccountId(
				accountService.findAccountByAccountNumber(number).getAccountId());
		
		for (BankCard bankCard : bankCardsList) {
			model.addAttribute("bankCard", bankCard);
		}
		
		model.addAttribute("bankCardsList", bankCardsList);
		model.addAttribute("bankCardsEmpty", bankCardsList.isEmpty());
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("account", accountService.findAccountByAccountNumber(number));
		return "user/userShowAccountCards";
	}
	
	/**
	 * Show detach card
	 * @param model
	 * @param principal
	 * @param accountNumber
	 * @param cardId
	 * @return user/userShowAccountCards
	 */
	@GetMapping("/attached-cards/{accountNumber}/detach/{cardId}")
	public String showDetachCards(Model model,
	                                Principal principal,
	                                @PathVariable("accountNumber") String accountNumber,
	                                @PathVariable("cardId") Long cardId
	) {
		List<BankCard> bankCardsList = bankCardService.findCardsByAccountId(
				accountService.findAccountByAccountNumber(accountNumber).getAccountId()
		);
		
		model.addAttribute("bankCardsList", bankCardsList);
		model.addAttribute("bankCardsEmpty", bankCardsList.isEmpty());
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("bankCard", bankCardService.findCardByCardId(cardId));
		model.addAttribute("account", accountService.findAccountByAccountNumber(accountNumber));
		return "user/userShowAccountCards";
	}
	
	/**
	 * Block card
	 * @param model
	 * @param principal
	 * @param accountNumber
	 * @param cardId
	 * @return redirect:/attached-cards/{accountNumber}
	 */
	@PostMapping("/attached-cards/{accountNumber}")
	public String blockCard(Model model,
	                         Principal principal,
	                         @PathVariable("accountNumber") String accountNumber,
	                         @RequestParam("cardId") Long cardId
	) {
		Account account = accountService.findAccountByAccountNumber(accountNumber);
		BankCard bankCard = bankCardService.findCardByCardId(cardId);
		
		if (bankCard.getActive()) {
			bankCardService.blockCard(cardId);
			model.addAttribute("alert", "cardBlockedSuccess");
		} else if (!bankCard.getActive()) {
			bankCardService.unblockCard(cardId);
			model.addAttribute("alert", "cardUnblockedSuccess");
		}
		
		model.addAttribute("cardId", cardId);
		model.addAttribute("bankCard", bankCard);
		model.addAttribute("account", account);
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		return "redirect:/attached-cards/{accountNumber}";
	}
	
	/**
	 * Detach card
	 * @param model
	 * @param principal
	 * @param accountNumber
	 * @param cardId
	 * @return redirect:/attached-cards/{accountNumber}
	 */
	@PostMapping("/attached-cards/{accountNumber}/detach/{cardId}")
	public String deleteAccount(Model model,
	                            Principal principal,
                                @PathVariable("accountNumber") String accountNumber,
                                @PathVariable("cardId") Long cardId
	) {
		Account account = accountService.findAccountByAccountNumber(accountNumber);
		BankCard bankCard = bankCardService.findCardByCardId(cardId);
		
		bankCardService.deleteCard(bankCard);
		model.addAttribute("bankCard", bankCard);
		model.addAttribute("account", account);
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		
		return "redirect:/attached-cards/{accountNumber}";
	}
}
