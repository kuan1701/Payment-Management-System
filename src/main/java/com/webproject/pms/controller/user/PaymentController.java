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
import java.util.List;

@Controller
public class PaymentController {

	private final UserServiceImpl userService;
	private final PaymentServiceImpl paymentService;
	private final AccountServiceImpl accountService;

	private List<Account> accounts;
	private List<Payment> paymentList;

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
	 * @param model Model
	 * @param principal Principal
	 * @return userShowPayments view
	 */
	@GetMapping("/show-payments/{userId}")
	public String showPayments(Model model,
	                           Principal principal,
							   @PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());
		paymentList = paymentService.findAllPaymentsByUserId(userId);

		model.addAttribute("user", user);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("paymentEmpty", paymentList.isEmpty());
		return "user/userShowPayments";
	}

	/**
	 * User show found payments
	 * @param model Model
	 * @param principal Principal
	 * @param startDate String input
	 * @param finalDate String input
	 * @param isIncoming String checkbox
	 * @param isOutgoing String checkbox
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
		model.addAttribute("paymentEmpty", paymentList.isEmpty());
		model.addAttribute("numberOfPayments", paymentList.size());
		model.addAttribute("response", "searchPaymentsSuccess");
		return "user/userShowPayments";
	}
	
	/**
	 * Show account payments
	 * @param model Model
	 * @param principal Principal
	 * @param number String
	 * @return userShowAccountPayments view
	 */
	@GetMapping("/show-account-payments/{accountNumber}")
	public String showAccountPayments(Model model,
	                                  Principal principal,
	                                  @PathVariable("accountNumber") String number
	){
		User user = userService.findUserByUsername(principal.getName());
		Account account = accountService.findAccountByAccountNumber(number);
		paymentList = paymentService.findAllPaymentsByAccountId(account.getAccountId());
		
		model.addAttribute("user", user);
		model.addAttribute("account", account);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("paymentsEmpty", paymentList.isEmpty());
		return "user/userShowAccountPayments";
	}
	
	/**
	 * Make payment form
	 * @param model Model
	 * @param principal Principal
	 * @return userMakePayment view
	 */
	@GetMapping("/make-payment")
	public String makePaymentForm(Model model,
	                              Principal principal,
                                  @ModelAttribute("account") Account account,
                                  @ModelAttribute("payment") Payment payment
	) {
		User user = userService.findUserByUsername(principal.getName());
		accounts = accountService.findAllActivateAccountsByUserId(user.getUserId());
		
		model.addAttribute("user", user);
		model.addAttribute("accounts", accounts);
		model.addAttribute("payment", new Payment());
		return "user/userMakePayment";
	}
	
	/**
	 * Payment creation process
	 * @param model Model
	 * @param principal Principal
	 * @param payment Payment
	 * @param bindingResult BindingResult
	 * @param accountFromId Long
	 * @param accountToNumber String
	 * @param amount BigDecimal
	 * @param appointment String
	 * @param paymentType String checkbox
	 * @return userMakePayment view
	 */
	@PostMapping("/make-payment")
	public String makePayment(Model model,
	                          Principal principal,
							  @RequestParam("amount") BigDecimal amount,
							  @RequestParam("appointment") String appointment,
							  @RequestParam("accountFromId") Long accountFromId,
							  @RequestParam("accountToNumber") String accountToNumber,
							  @RequestParam("paymentType") String paymentType,
							  @ModelAttribute("payment") @Valid Payment payment,
							  BindingResult bindingResult
	) {
		User user = userService.findUserByUsername(principal.getName());
		accounts = accountService.findAllActivateAccountsByUserId(user.getUserId());

		if (bindingResult.hasErrors()) {
			model.addAttribute("paymentError", "invalidData");
		}
		else if (paymentType.equals("on")) {
			paymentService.makePaymentOnAccount(
					accountFromId, accountToNumber, amount, appointment, model, user);
			model.addAttribute("paymentError", "paymentCompletedSuccess");
		}
		else if (paymentType.equals("off")) {
			paymentService.makePaymentOnCard(
					accountFromId, accountToNumber, amount, appointment, model, user);
			model.addAttribute("paymentError", "paymentCompletedSuccess");
		}
		else {
			model.addAttribute("paymentError", "paymentCompletedSuccess");
		}

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
	 * @param model Model
	 * @param principal Principal
	 * @param paymentId Long
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

		User recipientUser = null;
		User senderUser = senderAccount.getUser();

		Boolean recipientIsAccount = false;
		if (recipientAccount != null) {
			recipientUser = recipientAccount.getUser();
			recipientIsAccount = true;
		}

		model.addAttribute("payment", payment);
		model.addAttribute("senderUser", senderUser);
		model.addAttribute("recipientUser", recipientUser);
		model.addAttribute("recipientIsAccount", recipientIsAccount);
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		return "user/userShowPaymentInfo";
	}
}
