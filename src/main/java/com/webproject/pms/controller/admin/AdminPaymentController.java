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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminPaymentController {
	
	private final UserServiceImpl userService;
	private final LetterServiceImpl letterService;
	private final AccountServiceImpl accountService;
	private final PaymentServiceImpl paymentService;

	private List<Payment> paymentList;

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
	 * @param model Model
	 * @param principal Principal
	 * @param paymentId Long
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
		User recipientUser = null;
		List<Letter> letterList = letterService.findUnprocessedLetters();

		Boolean recipientIsAccount = false;
		if (recipientAccount != null) {
			recipientUser = recipientAccount.getUser();
			recipientIsAccount = true;
		}

		model.addAttribute("user", user);
		model.addAttribute("payment", payment);
		model.addAttribute("senderUser", senderUser);
		model.addAttribute("recipientIsAccount", recipientIsAccount);
		model.addAttribute("recipientUser", recipientUser);
		model.addAttribute("totalLetters", letterList.size());
		return "admin/adminShowPaymentInfo";
	}

	/**
	 *  Admin Show User Payments
	 * @param model Model
	 * @param principal Principal
	 * @param userId Long
	 * @return adminShowUserPayments view
	 */
	@GetMapping("/showPayments/{userId}")
	public String adminShowUserPayments(Model model,
                                        Principal principal,
                                        @PathVariable("userId") Long userId
	) {
		User viewableUser = userService.findUserByUserId(userId);
		User user = userService.findUserByUsername(principal.getName());
		paymentList = paymentService.findAllPaymentsByUserId(userId);
		
		model.addAttribute("user", user);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("paymentsEmpty", paymentList.isEmpty());
		return "admin/adminShowUserPayments";
	}

	/**
	 *
	 * @param model Model
	 * @param principal Principal
	 * @param userId Long
	 * @param startDate String
	 * @param finalDate String
	 * @param isIncoming String
	 * @param isOutgoing String
	 * @return adminShowUserPayments view
	 */
	@PostMapping("/showPayments/{userId}")
	public String showFoundAccounts(Model model,
									Principal principal,
									@PathVariable("userId") Long userId,
									@RequestParam("startDate") String startDate,
									@RequestParam("finalDate") String finalDate,
									@RequestParam("isIncoming") String isIncoming,
									@RequestParam("isOutgoing") String isOutgoing
	) {
		String checked = "1";
		String unchecked = "0";
		User user = userService.findUserByUsername(principal.getName());

		if (isIncoming.equals(checked) && isOutgoing.equals(unchecked)) {
			paymentList = paymentService.searchByCriteria(userId, false, startDate, finalDate);
		}
		else if (isIncoming.equals(unchecked) && isOutgoing.equals(checked)) {
			paymentList = paymentService.searchByCriteriaOutgoingFalse(userId, true, startDate, finalDate);
		}
		else {
			paymentList = paymentService.searchByCriteriaWithoutOutgoing(userId, startDate, finalDate);
		}

		model.addAttribute("user", user);
		model.addAttribute("finalDate", finalDate);
		model.addAttribute("startDate", startDate);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("paymentsEmpty", paymentList.isEmpty());
		model.addAttribute("numberOfPayments", paymentList.size());
		model.addAttribute("response", "searchPaymentsSuccess");
		return "admin/adminShowUserPayments";
	}
}
