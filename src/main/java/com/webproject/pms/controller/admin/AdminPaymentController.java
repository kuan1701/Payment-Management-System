package com.webproject.pms.controller.admin;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.Letter;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.AccountServiceImpl;
import com.webproject.pms.service.impl.LetterServiceImpl;
import com.webproject.pms.service.impl.PaymentServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPaymentController {
	
	private final UserServiceImpl userService;
	private final LetterServiceImpl letterService;
	private final AccountServiceImpl accountService;
	private final PaymentServiceImpl paymentService;

	public AdminPaymentController(UserServiceImpl userService,
	                              LetterServiceImpl letterService,
								  PaymentServiceImpl paymentService,
								  AccountServiceImpl accountService
	) {
		this.userService = userService;
		this.letterService = letterService;
		this.paymentService = paymentService;
		this.accountService = accountService;
	}

	/**
	 * Admin show payment info
	 * @param model
	 * @param principal
	 * @param paymentId
	 * @return adminShowPaymentInfo view
	 */
	@GetMapping("/paymentInfo/{paymentId}")
	public String adminPaymentInfo(Model model,
	                               Principal principal,
	                               @PathVariable("paymentId") Long paymentId
	) {
		User user = userService.findUserByUsername(principal.getName());
		Payment payment = paymentService.findPaymentByPaymentId(paymentId);
		Account senderAccount = accountService.findAccountByAccountNumber(payment.getSenderNumber());
		User senderUser = senderAccount.getUser();
		Account recipientAccount = accountService.findAccountByAccountNumber(payment.getRecipientNumber());
		User recipientUser = recipientAccount.getUser();
		List<Letter> letterList = letterService.findUnprocessedLetters();

		model.addAttribute("user", user);
		model.addAttribute("payment", payment);
		model.addAttribute("senderUser", senderUser);
		model.addAttribute("recipientIsAccount", true);
		model.addAttribute("recipientUser", recipientUser);
		model.addAttribute("totalLetters", letterList.size());
		return "admin/adminShowPaymentInfo";
	}

	/**
	 *  Admin Show User Payments
	 * @param model
	 * @param principal
	 * @param userId
	 * @return adminShowUserPayments view
	 */
	@GetMapping("/showPayments/{userId}")
	public String adminShowUserPayments(Model model,
                                        Principal principal,
                                        @PathVariable("userId") Long userId
	) {
		User viewableUser = userService.findUserByUserId(userId);
		User user = userService.findUserByUsername(principal.getName());
		List<Payment> paymentList = paymentService.findAllPaymentsByUserId(userId);
		
		model.addAttribute("user", user);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("paymentsEmpty", paymentList.isEmpty());
		return "admin/adminShowUserPayments";
	}
}
