package com.webproject.pms.controller.admin;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.AccountServiceImpl;
import com.webproject.pms.service.impl.PaymentServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminAccountController {
	
	private final UserServiceImpl userService;
	private final AccountServiceImpl accountService;
	private final PaymentServiceImpl paymentService;
	
	
	public AdminAccountController(AccountServiceImpl accountService, UserServiceImpl userService, PaymentServiceImpl paymentService) {
		this.accountService = accountService;
		this.userService = userService;
		this.paymentService = paymentService;
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
		
		model.addAttribute("user", user);
		model.addAttribute("userList", userList);
		model.addAttribute("accountList", accountList);
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
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		return "admin/adminShowAccounts";
	}
	
	@GetMapping("/accountInfo/{accountId}")
	public String adminShowAccountInfo(Model model,
                                       Principal principal,
                                       @PathVariable("accountId") Long accountId
	) {
		User user = userService.findUserByUsername(principal.getName());
		Account account = accountService.findAccountByAccountId(accountId);
		
		model.addAttribute("user", user);
		model.addAttribute("account", account);
		return "admin/adminShowAccountInfo";
	}
}
