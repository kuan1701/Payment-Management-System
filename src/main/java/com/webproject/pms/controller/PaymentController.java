package com.webproject.pms.controller;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.service.impl.AccountServiceImpl;
import com.webproject.pms.service.impl.PaymentServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class PaymentController {
	
	private final PaymentServiceImpl paymentService;
	private final AccountServiceImpl accountService;
	private final UserServiceImpl userService;
	
	public PaymentController(PaymentServiceImpl paymentService, AccountServiceImpl accountService, UserServiceImpl userService) {
		this.paymentService = paymentService;
		this.accountService = accountService;
		this.userService = userService;
	}
	
	/**
	 * Show payments
	 * @param model
	 * @param principal
	 * @return userShowPayments page
	 */
	@GetMapping("/show-payments")
	public String showPayments(Model model,
	                           Principal principal
	) {
		List<Payment> paymentList = paymentService.findAllPaymentsByUserId(
				userService.findUserByUsername(principal.getName()).getUserId());
		
		model.addAttribute("paymentEmpty", paymentList.isEmpty());
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		return "user/userShowPayments";
	}
	
	/**
	 * Show Account payments
	 * @param model
	 * @param principal
	 * @param number
	 * @return userShowAccountPayments page
	 */
	@GetMapping("/show-account-payments/{accountNumber}")
	public String showAccountPayments(Model model,
	                                  Principal principal,
	                                  @PathVariable("accountNumber") String number
	){
		Account account = accountService.findAccountByAccountNumber(number);
		List<Payment> paymentList = paymentService.findAllPaymentsByAccountId(account.getAccountId());
		
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("account", account);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("paymentsEmpty", paymentList.isEmpty());
		return "user/userShowAccountPayments";
	}
	
	/**
	 * Make payment
	 * @param model
	 * @param principal
	 * @return userMakePayment
	 */
	@GetMapping("/make-payments")
	public String makePaymentPage(Model model,
	                              Principal principal
			
	) {
		userService.findUserByUsername(principal.getName());
		
		return "user/userMakePayment";
	}
}
