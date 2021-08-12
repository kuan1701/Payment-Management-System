package com.webproject.pms.controller.user;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.AccountServiceImpl;
import com.webproject.pms.service.impl.PaymentServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PaymentController {
	
	private final PaymentServiceImpl paymentService;
	private final AccountServiceImpl accountService;
	private final UserServiceImpl userService;
	
	public PaymentController(PaymentServiceImpl paymentService,
							 AccountServiceImpl accountService,
							 UserServiceImpl userService
	) {
		this.paymentService = paymentService;
		this.accountService = accountService;
		this.userService = userService;
	}
	
	/**
	 * Show user payments
	 * @param model
	 * @param principal
	 * @return userShowPayments view
	 */
	@GetMapping("/show-payments/{userId}")
	public String showPayments(Model model,
	                           Principal principal,
							   @PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Payment> paymentList = paymentService.findAllPaymentsByUserId(userId);

		model.addAttribute("user", user);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("paymentEmpty", paymentList.isEmpty());
		return "user/userShowPayments";
	}

	/**
	 * User show found payments
	 * @param model - model
	 * @param principal - principal
	 * @param startDate - startDate
	 * @param finalDate - finalDate
	 * @param isIncoming - isIncoming
	 * @param isOutgoing - isOutgoing
	 * @return userShowPayments
	 */
	@PostMapping("/show-payments/{userId}")
	public String showFoundAccounts(Model model,
	                                Principal principal,
	                                @PathVariable("userId") Long userId,
	                                @RequestParam("startDate") String startDate,
	                                @RequestParam("finalDate") String finalDate,
									@RequestParam("isIncoming") String isIncoming,
									@RequestParam("isOutgoing") String isOutgoing
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Payment> paymentList;

		if (isIncoming.equals("1") && isOutgoing.equals("0")) {
			paymentList = paymentService.searchByCriteria(userId, false, startDate, finalDate);
		}
		else if (isIncoming.equals("0") && isOutgoing.equals("1")) {
			paymentList = paymentService.searchByCriteriaOutgoingFalse(userId, true, startDate, finalDate);
		}
		else {
			paymentList = paymentService.searchByCriteriaWithoutOutgoing(userId, startDate, finalDate);
		}

		model.addAttribute("user", user);
		model.addAttribute("finalDate", finalDate);
		model.addAttribute("startDate", startDate);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("paymentEmpty", paymentList.isEmpty());
		model.addAttribute("numberOfPayments", paymentList.size());
		model.addAttribute("response", "searchPaymentsSuccess");
		return "user/userShowPayments";
	}
	
	/**
	 * Show account payments
	 * @param model
	 * @param principal
	 * @param number
	 * @return userShowAccountPayments view
	 */
	@GetMapping("/show-account-payments/{accountNumber}")
	public String showAccountPayments(Model model,
	                                  Principal principal,
	                                  @PathVariable("accountNumber") String number
	){
		User user = userService.findUserByUsername(principal.getName());
		Account account = accountService.findAccountByAccountNumber(number);
		List<Payment> paymentList = paymentService.findAllPaymentsByAccountId(account.getAccountId());
		
		model.addAttribute("user", user);
		model.addAttribute("account", account);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("paymentsEmpty", paymentList.isEmpty());
		return "user/userShowAccountPayments";
	}
	
	/**
	 * Make payment form
	 * @param model
	 * @param principal
	 * @return userMakePayment view
	 */
	@GetMapping("/make-payment")
	public String makePaymentForm(Model model,
	                              Principal principal,
                                  @ModelAttribute("account") Account account,
                                  @ModelAttribute("payment") Payment payment
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Account> accounts = accountService.findAllActivateAccountsByUserId(user.getUserId());
		
		model.addAttribute("user", user);
		model.addAttribute("accounts", accounts);
		model.addAttribute("payment", new Payment());
		return "user/userMakePayment";
	}
	
	/**
	 * Payment creation process
	 * @param model
	 * @param principal
	 * @param payment
	 * @param bindingResult
	 * @param accountFromId
	 * @param accountToNumber
	 * @param amount
	 * @param appointment
	 * @return userMakePayment view
	 */
	@PostMapping("/make-payment")
	public String makePayment(Model model,
	                          Principal principal,
							  @RequestParam("amount") BigDecimal amount,
							  @RequestParam("appointment") String appointment,
							  @RequestParam("accountFromId") Long accountFromId,
							  @RequestParam("accountToNumber") String accountToNumber,
							  @ModelAttribute("payment") @Valid Payment payment,
							  BindingResult bindingResult
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Account> accounts = accountService.findAllActivateAccountsByUserId(user.getUserId());

		if (bindingResult.hasErrors()) {
			model.addAttribute("paymentError", "invalidData");
		}
		else if (!paymentService.makePaymentOnAccount(
				accountFromId, accountToNumber, amount, appointment, model, user)) {
			model.addAttribute("paymentError", "invalidData");
		}
		else {
			model.addAttribute("paymentError", "paymentCompletedSuccess");
		}

//		if (paymentType.equals("0")) {
//			paymentService.makePaymentOnAccount(
//					accountFromId, accountToNumber, amount, appointment, model, principal);
//			model.addAttribute("paymentError", "paymentCompletedSuccess");
//		}
//		else if (paymentType.equals("1")) {
//			paymentService.makePaymentOnCard(
//					accountFromId, accountToNumber, amount, appointment, model, principal);
//			model.addAttribute("paymentError", "paymentCompletedSuccess");
//		}
//		else {
//			model.addAttribute("paymentError", "paymentCompletedSuccess");
//		}

		model.addAttribute("user", user);
		model.addAttribute("amount", amount);
		model.addAttribute("accounts", accounts);
		model.addAttribute("appointment", appointment);
		model.addAttribute("accountFromId", accountFromId);
		model.addAttribute("accountToNumber", accountToNumber);
		return "user/userMakePayment";
	}
	
	/**
	 * Show payment info
	 * @param model
	 * @param principal
	 * @param paymentId
	 * @return userShowPaymentInfo view
	 */
	@GetMapping("/paymentInfo/{paymentId}")
	public String showPaymentInfo(Model model,
	                              Principal principal,
	                              @PathVariable("paymentId") Long paymentId
	) {
		Payment payment = paymentService.findPaymentByPaymentId(paymentId);
		Account senderAccount = accountService.findAccountByAccountNumber(payment.getSenderNumber());
		Account recipientAccount = accountService.findAccountByAccountNumber(payment.getRecipientNumber());
		User senderUser = senderAccount.getUser();
		User recipientUser = recipientAccount.getUser();

		model.addAttribute("payment", payment);
		model.addAttribute("senderUser", senderUser);
		model.addAttribute("recipientIsAccount", true);
		model.addAttribute("recipientUser", recipientUser);
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		return "user/userShowPaymentInfo";
	}
}
