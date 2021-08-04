package com.webproject.pms.controller.admin;

import com.webproject.pms.model.entities.*;
import com.webproject.pms.service.impl.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminAccountController {
	
	private final UserServiceImpl userService;
	private final AccountServiceImpl accountService;
	private final PaymentServiceImpl paymentService;
	private final BankCardServiceImpl cardService;
	private final LetterServiceImpl letterService;
	
	public AdminAccountController(AccountServiceImpl accountService,
	                              UserServiceImpl userService,
	                              PaymentServiceImpl paymentService,
	                              BankCardServiceImpl cardService,
	                              LetterServiceImpl letterService
	) {
		this.accountService = accountService;
		this.userService = userService;
		this.paymentService = paymentService;
		this.cardService = cardService;
		this.letterService = letterService;
	}
	
	/**
	 * Admin show all accounts in the system
	 *
	 * @param model
	 * @param principal
	 * @return adminSHowAccounts
	 */
	@GetMapping("/accounts")
	public String adminShowAllAccounts(Model model,
	                                   Principal principal
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<User> userList = userService.findAllUsers();
		List<Account> accountList = accountService.findAllAccounts();
		List<Letter> letterList = letterService.findUnprocessedLetters();
		
		model.addAttribute("user", user);
		model.addAttribute("userList", userList);
		model.addAttribute("accountList", accountList);
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		return "admin/adminShowAccounts";
	}
	
	@PostMapping("/accounts")
	public String adminSearchAccounts(Model model,
	                                  Principal principal,
	                                  @RequestParam("accountNumber") String accountNumber,
	                                  @RequestParam("min_value") String min_value,
	                                  @RequestParam("max_value") String max_value,
	                                  @RequestParam("currency") String currency
	
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<User> userList = userService.findAllUsers();
		List<Account> accountList = accountService.searchByCriteriaWithoutId(
				accountNumber,
				min_value,
				max_value,
				currency);
		
		model.addAttribute("user", user);
		model.addAttribute("currency", currency);
		model.addAttribute("userList", userList);
		model.addAttribute("min_value", min_value);
		model.addAttribute("max_value", max_value);
		model.addAttribute("accountList", accountList);
		model.addAttribute("accountNumber", accountNumber);
		model.addAttribute("numberOfAccounts", accountList.size());
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		model.addAttribute("response", "searchAccountsSuccess");
		return "admin/adminShowAccounts";
	}
	
	@GetMapping("/accountInfo/{accountId}")
	public String adminShowAccountInfo(Model model,
                                       Principal principal,
                                       @PathVariable("accountId") Long accountId
	) {
		User user = userService.findUserByUsername(principal.getName());
		Account viewableAccount = accountService.findAccountByAccountId(accountId);
		User viewableUser = userService.findUserByUserId(viewableAccount.getUser().getUserId());
		List<Payment> paymentList = paymentService.findAllPaymentsByAccountId(accountId);
		List<Letter> letterList = letterService.findUnprocessedLetters();
		List<BankCard> cardList = cardService.findCardsByAccountId(accountId);
		
		for (BankCard card : cardList) {
			model.addAttribute("card", card);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("cardList", cardList);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("cardsEmpty", cardList.isEmpty());
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("viewableAccount", viewableAccount);
		model.addAttribute("paymentsEmpty", paymentList.isEmpty());
//		model.addAttribute("response", "unableGetUserId");
		return "admin/adminShowAccountInfo";
	}
	
	/**
	 * Block unblock account
	 * @param model
	 * @param principal
	 * @param accountId
	 * @return show-accounts page
	 */
	@PostMapping("/accountInfo/{accountId}")
	public String blockAccount(Model model,
	                           Principal principal,
	                           @PathVariable("accountId") Long accountId
	) {
		Account viewableAccount = accountService.findAccountByAccountId(accountId);
		User user = userService.findUserByUsername(principal.getName());
		User viewableUser = viewableAccount.getUser();
		List<Payment> paymentList = paymentService.findAllPaymentsByAccountId(accountId);
		List<Letter> letterList = letterService.findUnprocessedLetters();
		List<BankCard> cardList = cardService.findCardsByAccountId(accountId);
		
		if (!viewableAccount.getBlocked()) {
			accountService.blockAccount(viewableAccount);
		}
		else if(viewableAccount.getBlocked()) {
			accountService.unblockAccount(viewableAccount);
		}
		model.addAttribute("user", user);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("viewableAccount", viewableAccount);
		model.addAttribute("response", "accountBlockedSuccess");
		model.addAttribute("cardList", cardList);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("totalLetters", letterList.size());
		return "admin/adminShowAccountInfo";
	}
	
	/**
	 * Admin attach account to user
	 * @param model
	 * @param principal
	 * @param userId
	 * @return admin/adminAttachAccount page
	 */
	@GetMapping("/attachAccount/{userId}")
	public String attachAccountToUserPage(Model model,
	                                      Principal principal,
	                                      @PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());
		User viewableUser = userService.findUserByUserId(userId);
		List<Letter> letterList = letterService.findUnprocessedLetters();
		
		model.addAttribute("user", user);
		model.addAttribute("account", new Account());
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("totalLetters", letterList.size());
		return "admin/adminAttachAccount";
	}
	
	/**
	 * Admin attach account to user
	 * @param model
	 * @param principal
	 * @param userId
	 * @param account
	 * @return admin/adminAttachAccount page
	 */
	@PostMapping("/attachAccount/{userId}")
	public String adminAttachAccountToUser(Model model,
	                                       Principal principal,
	                                       @PathVariable("userId") Long userId,
	                                       @ModelAttribute("account") Account account
	) {
		User user = userService.findUserByUsername(principal.getName());
		User viewableUser = userService.findUserByUserId(userId);
		
		if (!accountService.adminAttachAccount(account, model, userId)){
			model.addAttribute("user", user);
			model.addAttribute("response", "accountAttachError");
		} else {
			model.addAttribute("user", user);
			model.addAttribute("response", "accountAttachedSuccess");
		}
		
		model.addAttribute("user", user);
		model.addAttribute("viewableUser", viewableUser);
		return "admin/adminAttachAccount";
	}
	
	/**
	 * Admin show all user accounts
	 * @param model
	 * @param principal
	 * @param userId
	 * @return admin/adminShowUserAccounts page
	 */
	@GetMapping("/showUserAccounts/{userId}")
	public String adminShowAllUserAccountsPage(Model model,
	                                           Principal principal,
	                                           @PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());
		User viewableUser = userService.findUserByUserId(userId);
		List<Account> accounts = accountService.findAllAccountsByUserId(userId);
		List<Letter> letterList = letterService.findUnprocessedLetters();
		
		model.addAttribute("user", user);
		model.addAttribute("accounts", accounts);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("accountsEmpty", accounts.isEmpty());
		return "admin/adminShowUserAccounts";
	}
	
	/**
	 * Admin find account
	 * @param model
	 * @param principal
	 * @param userId
	 * @param accountNumber
	 * @param min_value
	 * @param max_value
	 * @param currency
	 * @return admin/adminShowUserAccounts page
	 */
	@PostMapping("/showUserAccounts/{userId}")
	public String showAdminFoundAccounts(Model model,
	                                     Principal principal,
	                                     @PathVariable("userId") Long userId,
	                                     @RequestParam("accountNumber") String accountNumber,
	                                     @RequestParam("min_value") String min_value,
	                                     @RequestParam("max_value") String max_value,
	                                     @RequestParam("currency") String currency
	) {
		User user = userService.findUserByUsername(principal.getName());
		User viewableUser = userService.findUserByUserId(userId);
		List<Letter> letterList = letterService.findUnprocessedLetters();
		List<Account> accounts = accountService.searchByCriteria(
				userId,
				accountNumber,
				min_value,
				max_value,
				currency);
		
		model.addAttribute("user", user);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("currency", currency);
		model.addAttribute("min_value", min_value);
		model.addAttribute("max_value", max_value);
		model.addAttribute("accounts", accounts);
		model.addAttribute("accountNumber", accountNumber);
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("accountsEmpty", accounts.isEmpty());
		model.addAttribute("response", "searchAccountsSuccess");
		return "admin/adminShowUserAccounts";
	}
	
	/**
	 * Admin block card
	 * @param model
	 * @param principal
	 * @param accountId
	 * @param cardId
	 * @return redirect:/attached-cards/{accountNumber} page
	 */
	@GetMapping("/accountInfo/{accountId}/block/{cardId}")
	public String adminBlockCardPage(Model model,
	                             Principal principal,
	                             @PathVariable("accountId") Long accountId,
	                             @PathVariable("cardId") Long cardId
	) {
		User user = userService.findUserByUsername(principal.getName());
		Account viewableAccount = accountService.findAccountByAccountId(accountId);
		BankCard card = cardService.findCardByCardId(cardId);
		User viewableUser = viewableAccount.getUser();
		List<Letter> letterList = letterService.findUnprocessedLetters();
		List<BankCard> cardList = cardService.findCardsByAccountId(accountId);
	
		model.addAttribute("card", card);
		model.addAttribute("user", user);
		model.addAttribute("cardList", cardList);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("cardsEmpty", cardList.isEmpty());
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("viewableAccount", viewableAccount);
		return "admin/adminShowAccountInfo";
	}
	
	/**
	 * Admin block card
	 * @param model
	 * @param principal
	 * @param accountId
	 * @param cardId
	 * @return redirect:/attached-cards/{accountNumber} page
	 */
	@PostMapping("/accountInfo/{accountId}/block/{cardId}")
	public String adminBlockCard(Model model,
	                         Principal principal,
	                         @PathVariable("accountId") Long accountId,
	                         @PathVariable("cardId") Long cardId
	) {
		Account account = accountService.findAccountByAccountId(accountId);
		BankCard card = cardService.findCardByCardId(cardId);
		List<BankCard> cardList = cardService.findCardsByAccountId(accountId);
		User user = userService.findUserByUsername(principal.getName());
		User viewableUser = account.getUser();
		
		if (card.getActive()) {
			cardService.blockCard(cardId);
			model.addAttribute("alert", "cardBlockedSuccess");
		}
		else if (!card.getActive()) {
			cardService.unblockCard(cardId);
			model.addAttribute("alert", "cardUnblockedSuccess");
		}
		model.addAttribute("user", user);
		model.addAttribute("account", account);
		model.addAttribute("cardList", cardList);
		model.addAttribute("viewableUser", viewableUser);
		return "admin/adminShowAccountInfo";
	}
	
	/**
	 * Show detach card
	 * @param model
	 * @param principal
	 * @param accountId
	 * @param cardId
	 * @return user/userShowAccountCards page
	 */
	@GetMapping("/accountInfo/{accountId}/detach/{cardId}")
	public String showDetachCards(Model model,
	                              Principal principal,
	                              @PathVariable("accountId") Long accountId,
	                              @PathVariable("cardId") Long cardId
	) {
		Account viewableAccount = accountService.findAccountByAccountId(accountId);
		List<BankCard> cardsList = cardService.findCardsByAccountId(accountId);
		
		model.addAttribute("cardsList", cardsList);
		model.addAttribute("cardsEmpty", cardsList.isEmpty());
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("card", cardService.findCardByCardId(cardId));
		model.addAttribute("viewableAccount", viewableAccount);
		return "admin/adminShowAccountInfo";
	}
	
	/**
	 * Detach card
	 * @param model
	 * @param principal
	 * @param accountId
	 * @param cardId
	 * @return redirect:/admin/accountInfo/{accountId} page
	 */
	@PostMapping("/accountInfo/{accountId}/detach/{cardId}")
	public String detachCard(Model model,
	                            Principal principal,
	                            @PathVariable("accountId") Long accountId,
	                            @PathVariable("cardId") Long cardId
	) {
		BankCard card = cardService.findCardByCardId(cardId);
		Account viewableAccount = accountService.findAccountByAccountId(accountId);
		User viewableUser = viewableAccount.getUser();
		
		cardService.deleteCard(card);
		model.addAttribute("card", card);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("viewableAccount", viewableAccount);
		model.addAttribute("response", "cardDetachedSuccess");
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		
		return "admin/adminShowAccountInfo";
	}
	
	/**
	 * Delete account
	 * @param model
	 * @param principal
	 * @param accountId
	 * @return show-accounts view page
	 */
	@PostMapping("/accountInfo/delete/{accountId}")
	public String deleteAccount(Model model,
	                            Principal principal,
	                            @PathVariable("accountId") Long accountId
	) {
		Account viewableAccount = accountService.findAccountByAccountId(accountId);
		User user = userService.findUserByUsername(principal.getName());
		
		if (viewableAccount != null) {
			accountService.deleteAccount(viewableAccount);
			model.addAttribute("user", user);
			model.addAttribute("viewableAccount", viewableAccount);
			model.addAttribute("response", "accountHasFundsError");
		}
		model.addAttribute("user", user);
		model.addAttribute("viewableAccount", viewableAccount);
		
		return "admin/adminShowAccountInfo";
	}
}
