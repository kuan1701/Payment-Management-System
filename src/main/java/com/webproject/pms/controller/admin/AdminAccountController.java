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
	private final ActionLogServiceImpl actionLogService;

	private List<User> userList;
	private List<Letter> letterList;
	private List<BankCard> cardsList;
	private List<Account> accountList;
	private List<Payment> paymentList;

	public AdminAccountController(AccountServiceImpl accountService,
								  UserServiceImpl userService,
								  PaymentServiceImpl paymentService,
								  BankCardServiceImpl cardService,
								  LetterServiceImpl letterService,
								  ActionLogServiceImpl actionLogService
	) {
		this.accountService = accountService;
		this.userService = userService;
		this.paymentService = paymentService;
		this.cardService = cardService;
		this.letterService = letterService;
		this.actionLogService = actionLogService;
	}
	
	/**
	 * Admin show all accounts in the system
	 * @param model Model
	 * @param principal Principal
	 * @return adminSHowAccounts view
	 */
	@GetMapping("/accounts")
	public String adminShowAllAccounts(Model model,
	                                   Principal principal
	) {
		userList = userService.findAllUsers();
		accountList = accountService.findAllAccounts();
		User user = userService.findUserByUsername(principal.getName());
		letterList = letterService.findUnprocessedLetters();
		
		model.addAttribute("user", user);
		model.addAttribute("userList", userList);
		model.addAttribute("accountList", accountList);
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		return "admin/adminShowAccounts";
	}

	/**
	 * Withdrawing found accounts
	 * @param model Model
	 * @param principal Principal
	 * @param accountNumber String input
	 * @param min_value String input
	 * @param max_value String input
	 * @param currency String input
	 * @return adminShowAccounts view
	 */
	@PostMapping("/accounts")
	public String adminSearchAccounts(Model model,
	                                  Principal principal,
									  @RequestParam("currency") String currency,
	                                  @RequestParam("min_value") String min_value,
									  @RequestParam("max_value") String max_value,
									  @RequestParam("accountNumber") String accountNumber

	) {
		userList = userService.findAllUsers();
		User user = userService.findUserByUsername(principal.getName());
		accountList = accountService.searchByCriteriaWithoutId(
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

	/**
	 * Show account info
	 * @param model Model
	 * @param principal Principal
	 * @param accountId Long
	 * @return adminShowAccountInfo view
	 */
	@GetMapping("/accountInfo/{accountId}")
	public String adminShowAccountInfo(Model model,
                                       Principal principal,
                                       @PathVariable("accountId") Long accountId
	) {
		User user = userService.findUserByUsername(principal.getName());
		Account viewableAccount = accountService.findAccountByAccountId(accountId);
		User viewableUser = userService.findUserByUserId(viewableAccount.getUser().getUserId());
		letterList = letterService.findUnprocessedLetters();
		cardsList = cardService.findCardsByAccountId(accountId);
		paymentList = paymentService.findAllPaymentsByAccountId(accountId);

		for (BankCard card : cardsList) {
			model.addAttribute("card", card);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("cardsList", cardsList);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("cardsEmpty", cardsList.isEmpty());
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("viewableAccount", viewableAccount);
		model.addAttribute("paymentsEmpty", paymentList.isEmpty());
		return "admin/adminShowAccountInfo";
	}
	
	/**
	 * Blocking and unblocking of the user account by the administrator
	 * @param model Model
	 * @param principal Principal
	 * @param accountId Long
	 * @return adminShowAccountInfo view
	 */
	@PostMapping("/accountInfo/{accountId}")
	public String blockAccount(Model model,
	                           Principal principal,
	                           @PathVariable("accountId") Long accountId
	) {
		Account viewableAccount = accountService.findAccountByAccountId(accountId);
		User user = userService.findUserByUsername(principal.getName());
		User viewableUser = viewableAccount.getUser();
		paymentList = paymentService.findAllPaymentsByAccountId(accountId);
		letterList = letterService.findUnprocessedLetters();
		cardsList = cardService.findCardsByAccountId(accountId);
		
		if (!viewableAccount.getBlocked()) {
			accountService.blockAccount(viewableAccount);
			model.addAttribute("response", "accountBlockedSuccess");
			actionLogService.createLog("BLOCKED: Account [" + viewableAccount.getNumber() + "]", user);
		}
		else if(viewableAccount.getBlocked()) {
			accountService.unblockAccount(viewableAccount);
			model.addAttribute("response", "accountUnblockedSuccess");
			actionLogService.createLog("UNBLOCKED: Account [" + viewableAccount.getNumber() + "]", user);
		}
		model.addAttribute("user", user);
		model.addAttribute("cardList", cardsList);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("viewableAccount", viewableAccount);
		return "admin/adminShowAccountInfo";
	}
	
	/**
	 * Admin attach account to user form
	 * @param model Model
	 * @param principal Principal
	 * @param userId Long
	 * @return adminAttachAccount view
	 */
	@GetMapping("/attachAccount/{userId}")
	public String attachAccountToUserPage(Model model,
	                                      Principal principal,
	                                      @PathVariable("userId") Long userId
	) {
		User viewableUser = userService.findUserByUserId(userId);
		User user = userService.findUserByUsername(principal.getName());
		letterList = letterService.findUnprocessedLetters();
		
		model.addAttribute("user", user);
		model.addAttribute("account", new Account());
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("totalLetters", letterList.size());
		return "admin/adminAttachAccount";
	}
	
	/**
	 * Admin attach account to user
	 * @param model Model
	 * @param principal Principal
	 * @param userId Long
	 * @param account Account
	 * @return adminAttachAccount view
	 */
	@PostMapping("/attachAccount/{userId}")
	public String adminAttachAccountToUser(Model model,
	                                       Principal principal,
	                                       @PathVariable("userId") Long userId,
	                                       @ModelAttribute("account") Account account
	) {
		letterList = letterService.findUnprocessedLetters();
		User viewableUser = userService.findUserByUserId(userId);
		User user = userService.findUserByUsername(principal.getName());

		if (!accountService.adminAttachAccount(account, userId)){
			model.addAttribute("response", "accountAttachError");
			actionLogService.createLog("ERROR: Unsuccessful attempt to create a new account", user);
		}
		else {
			model.addAttribute("response", "accountAttachedSuccess");
			actionLogService.createLog("CREATED: Account [" + account.getNumber() + ", " + account.getCurrency() + "]", user);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("totalLetters", letterList.size());
		return "admin/adminAttachAccount";
	}
	
	/**
	 * Admin show all user accounts
	 * @param model Model
	 * @param principal Principal
	 * @param userId Long
	 * @return adminShowUserAccounts view
	 */
	@GetMapping("/showUserAccounts/{userId}")
	public String adminShowAllUserAccountsPage(Model model,
	                                           Principal principal,
	                                           @PathVariable("userId") Long userId
	) {
		User viewableUser = userService.findUserByUserId(userId);
		User user = userService.findUserByUsername(principal.getName());
		letterList = letterService.findUnprocessedLetters();
		List<Account> accounts = accountService.findAllAccountsByUserId(userId);

		model.addAttribute("user", user);
		model.addAttribute("accounts", accounts);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("accountsEmpty", accounts.isEmpty());
		return "admin/adminShowUserAccounts";
	}
	
	/**
	 * Admin find account
	 * @param model Model
	 * @param principal Principal
	 * @param userId Long
	 * @param accountNumber String input
	 * @param min_value String input
	 * @param max_value String input
	 * @param currency String input
	 * @return adminShowUserAccounts view
	 */
	@PostMapping("/showUserAccounts/{userId}")
	public String showAdminFoundAccounts(Model model,
	                                     Principal principal,
	                                     @PathVariable("userId") Long userId,
										 @RequestParam("currency") String currency,
	                                     @RequestParam("min_value") String min_value,
										 @RequestParam("max_value") String max_value,
										 @RequestParam("accountNumber") String accountNumber
										 ) {
		User viewableUser = userService.findUserByUserId(userId);
		User user = userService.findUserByUsername(principal.getName());
		letterList = letterService.findUnprocessedLetters();
		List<Account> accounts = accountService.searchByCriteria(
				userId,
				accountNumber,
				min_value,
				max_value,
				currency);
		
		model.addAttribute("user", user);
		model.addAttribute("accounts", accounts);
		model.addAttribute("currency", currency);
		model.addAttribute("min_value", min_value);
		model.addAttribute("max_value", max_value);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("accountNumber", accountNumber);
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("accountsEmpty", accounts.isEmpty());
		model.addAttribute("response", "searchAccountsSuccess");
		return "admin/adminShowUserAccounts";
	}
	
	/**
	 * Admin block card form
	 * @param model Model
	 * @param principal Principal
	 * @param accountId Long
	 * @param cardId Long
	 * @return adminShowAccountInfo view
	 */
	@GetMapping("/accountInfo/{accountId}/block/{cardId}")
	public String adminBlockCardForm(Model model,
									 Principal principal,
									 @PathVariable("cardId") Long cardId,
									 @PathVariable("accountId") Long accountId
	) {
		User user = userService.findUserByUsername(principal.getName());
		Account viewableAccount = accountService.findAccountByAccountId(accountId);
		BankCard card = cardService.findCardByCardId(cardId);
		User viewableUser = viewableAccount.getUser();
		letterList = letterService.findUnprocessedLetters();
		cardsList = cardService.findCardsByAccountId(accountId);
	
		model.addAttribute("card", card);
		model.addAttribute("user", user);
		model.addAttribute("cardList", cardsList);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("cardsEmpty", cardsList.isEmpty());
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("viewableAccount", viewableAccount);
		return "admin/adminShowAccountInfo";
	}
	
	/**
	 * Blocking and unblocking of the user account by the administrator
	 * @param model Model
	 * @param principal Principal
	 * @param accountId Long
	 * @param cardId Long
	 * @return adminShowAccountInfo view
	 */
	@PostMapping("/accountInfo/{accountId}/block/{cardId}")
	public String adminBlockCard(Model model,
	                         Principal principal,
	                         @PathVariable("accountId") Long accountId,
	                         @PathVariable("cardId") Long cardId
	) {
		BankCard card = cardService.findCardByCardId(cardId);
		User user = userService.findUserByUsername(principal.getName());
		Account viewableAccount = accountService.findAccountByAccountId(accountId);
		cardsList = cardService.findCardsByAccountId(accountId);
		User viewableUser = viewableAccount.getUser();
		
		if (card.getActive()) {
			cardService.blockCard(card);
			model.addAttribute("alert", "cardBlockedSuccess");
			actionLogService.createLog("BLOCKED: Card [" + card.getNumber() + "]", user);
		}
		else if (!card.getActive()) {
			cardService.unblockCard(card);
			model.addAttribute("alert", "cardUnblockedSuccess");
			actionLogService.createLog("UNBLOCKED: Card [" + card.getNumber() + "]", user);
		}
		model.addAttribute("user", user);
		model.addAttribute("viewableAccount", viewableAccount);
		model.addAttribute("cardList", cardsList);
		model.addAttribute("viewableUser", viewableUser);
		return "admin/adminShowAccountInfo";
	}
	
	/**
	 * Show detach card form
	 * @param model Model
	 * @param principal Principal
	 * @param accountId Long
	 * @param cardId Long
	 * @return adminShowAccountInfo view
	 */
	@GetMapping("/accountInfo/{accountId}/detach/{cardId}")
	public String showDetachCards(Model model,
	                              Principal principal,
								  @PathVariable("cardId") Long cardId,
	                              @PathVariable("accountId") Long accountId
	) {
		BankCard card = cardService.findCardByCardId(cardId);
		User user = userService.findUserByUsername(principal.getName());
		cardsList = cardService.findCardsByAccountId(accountId);
		Account viewableAccount = accountService.findAccountByAccountId(accountId);

		model.addAttribute("card", card);
		model.addAttribute("user", user);
		model.addAttribute("cardsList", cardsList);
		model.addAttribute("cardsEmpty", cardsList.isEmpty());
		model.addAttribute("viewableAccount", viewableAccount);
		return "admin/adminShowAccountInfo";
	}
	
	/**
	 * Admin detaching card
	 * @param model Model
	 * @param principal Principal
	 * @param accountId Long
	 * @param cardId Long
	 * @return adminShowAccountInfo view
	 */
	@PostMapping("/accountInfo/{accountId}/detach/{cardId}")
	public String detachCard(Model model,
							 Principal principal,
							 @PathVariable("cardId") Long cardId,
							 @PathVariable("accountId") Long accountId
	) {
		BankCard card = cardService.findCardByCardId(cardId);
		User user = userService.findUserByUsername(principal.getName());
		Account viewableAccount = accountService.findAccountByAccountId(accountId);
		User viewableUser = viewableAccount.getUser();

		cardService.deleteCard(card);
		model.addAttribute("card", card);
		model.addAttribute("user", user);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("viewableAccount", viewableAccount);
		model.addAttribute("response", "cardDetachedSuccess");
		actionLogService.createLog("DETACHED: Card [" + card.getNumber() + "]", user);
		return "admin/adminShowAccountInfo";
	}
	
	/**
	 * Admin deleting account
	 * @param model Model
	 * @param principal Principal
	 * @param accountId Long
	 * @return adminShowAccountInfo view
	 */
	@PostMapping("/accountInfo/delete/{accountId}")
	public String deleteAccount(Model model,
	                            Principal principal,
	                            @PathVariable("accountId") Long accountId
	) {
		User user = userService.findUserByUsername(principal.getName());
		Account viewableAccount = accountService.findAccountByAccountId(accountId);

		if (viewableAccount != null) {
			accountService.deleteAccount(viewableAccount);
			model.addAttribute("response", "accountHasFundsError");
		}
		model.addAttribute("user", user);
		model.addAttribute("viewableAccount", viewableAccount);
		actionLogService.createLog("DELETED: ACCOUNT [" + viewableAccount.getNumber() + "]", user);
		return "admin/adminShowAccountInfo";
	}
}
