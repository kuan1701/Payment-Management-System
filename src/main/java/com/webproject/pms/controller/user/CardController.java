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

	private final UserServiceImpl userService;
	private final AccountServiceImpl accountService;
	private final BankCardServiceImpl bankCardService;

	public CardController(UserServiceImpl userService,
						  AccountServiceImpl accountService,
						  BankCardServiceImpl bankCardService
	) {
		this.userService = userService;
		this.accountService = accountService;
		this.bankCardService = bankCardService;
	}
	
	/**
	 * Show attach card to account form
	 * @param model
	 * @param principal
	 * @return userAttachCard view
	 */
	@GetMapping("/attach-card")
	public String attachCardForm(Model model,
	                         Principal principal
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Account> accounts = accountService.findAllActivateAccountsByUserId(user.getUserId());
		
		model.addAttribute("user", user);
		model.addAttribute("accounts", accounts);
		model.addAttribute("bankCard", new BankCard());
		return "user/userAttachCard";
	}

	/**
	 * Attaching a bank card user accounts
	 * @param model
	 * @param principal
	 * @param bankCard
	 * @param accountNumber
	 * @return userAttachCard view
	 */
	@PostMapping("/attach-card")
	public String attachCard(Model model,
							 Principal principal,
							 @ModelAttribute("card") BankCard bankCard,
							 @RequestParam("accountNumber") String accountNumber
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Account> accounts = accountService.findAllActivateAccountsByUserId(user.getUserId());
		Account account = accountService.findAccountByAccountId(Long.parseLong(accountNumber));
		
		model.addAttribute("user", user);
		model.addAttribute("accounts", accounts);
		model.addAttribute("bankCard", bankCard);
		
		
		if (!bankCardService.addNewBankCard(bankCard, account)) {
			model.addAttribute("cardError", "Card attachment error");
		}
		else {
			model.addAttribute("cardError", "Card attached successfully");
		}
		model.addAttribute("user", user);
		return "user/userAttachCard";
	}
	
	/**
	 * Show attached cards
	 * @param model
	 * @param principal
	 * @param number
	 * @return userShowAccountCards view
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
	 * Show detach card form
	 * @param model
	 * @param principal
	 * @param accountNumber
	 * @param cardId
	 * @return userShowAccountCards view
	 */
	@GetMapping("/attached-cards/{accountNumber}/detach/{cardId}")
	public String showDetachCards(Model model,
								  Principal principal,
								  @PathVariable("cardId") Long cardId,
								  @PathVariable("accountNumber") String accountNumber
	) {
		List<BankCard> bankCardsList = bankCardService.findCardsByAccountId(
				accountService.findAccountByAccountNumber(accountNumber).getAccountId()
		);
		
		model.addAttribute("bankCardsList", bankCardsList);
		model.addAttribute("bankCardsEmpty", bankCardsList.isEmpty());
		model.addAttribute("bankCard", bankCardService.findCardByCardId(cardId));
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("account", accountService.findAccountByAccountNumber(accountNumber));
		return "user/userShowAccountCards";
	}
	
	/**
	 * Blocking and unblocking of the card by the user
	 * @param model
	 * @param principal
	 * @param accountNumber
	 * @param cardId
	 * @return userShowAccountCards view
	 */
	@PostMapping("/attached-cards/{accountNumber}")
	public String blockCard(Model model,
							Principal principal,
							@RequestParam("cardId") Long cardId,
							@PathVariable("accountNumber") String accountNumber
	) {
		BankCard bankCard = bankCardService.findCardByCardId(cardId);
		User user = userService.findUserByUsername(principal.getName());
		Account account = accountService.findAccountByAccountNumber(accountNumber);
		List<BankCard> bankCardsList = bankCardService.findCardsByAccountId(account.getAccountId());

		if (bankCard.getActive()) {
			bankCardService.blockCard(bankCard);
			model.addAttribute("alert", "cardBlockedSuccess");
		}
		else if (!bankCard.getActive()) {
			bankCardService.unblockCard(bankCard);
			model.addAttribute("alert", "cardUnblockedSuccess");
		}

		model.addAttribute("user", user);
		model.addAttribute("cardId", cardId);
		model.addAttribute("account", account);
		model.addAttribute("bankCard", bankCard);
		model.addAttribute("bankCardsList", bankCardsList);
		model.addAttribute("bankCardsEmpty", bankCardsList.isEmpty());
		return "user/userShowAccountCards";
	}
	
	/**
	 * Detaching the card by the user
	 * @param model
	 * @param principal
	 * @param accountNumber
	 * @param cardId
	 * @return userShowAccountCards view
	 */
	@PostMapping("/attached-cards/{accountNumber}/detach/{cardId}")
	public String deleteAccount(Model model,
	                            Principal principal,
								@PathVariable("cardId") Long cardId,
                                @PathVariable("accountNumber") String accountNumber
	) {
		BankCard bankCard = bankCardService.findCardByCardId(cardId);
		User user = userService.findUserByUsername(principal.getName());
		Account account = accountService.findAccountByAccountNumber(accountNumber);

		bankCardService.deleteCard(bankCard);
		List<BankCard> bankCardsList = bankCardService.findCardsByAccountId(account.getAccountId());

		model.addAttribute("user", user);
		model.addAttribute("account", account);
		model.addAttribute("bankCard", bankCard);
		model.addAttribute("bankCardsList", bankCardsList);
		model.addAttribute("alert", "cardDetachedSuccess");
		model.addAttribute("bankCardsEmpty", bankCardsList.isEmpty());
		return "user/userShowAccountCards";
	}
}
