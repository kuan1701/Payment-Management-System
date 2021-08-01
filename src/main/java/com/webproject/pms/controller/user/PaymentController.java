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
		
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("paymentEmpty", paymentList.isEmpty());
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		return "user/userShowPayments";
	}
	
	@PostMapping("/show-payments")
	public String showFoundAccounts(Model model,
	                                Principal principal,
	                                @RequestParam("startDate") String startDate,
	                                @RequestParam("finalDate") String finalDate,
                                    @RequestParam("checkbox") String checkbox
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Payment> paymentList = new ArrayList<>();
		
		if (checkbox.equals("0,1")) {
			paymentList = paymentService.searchByCriteria(user.getUserId(), true, startDate, finalDate);
		}
		else if (checkbox.equals("1,0")) {
			paymentList = paymentService.searchByCriteriaWithoutOutgoing(user.getUserId(), startDate, finalDate);
		}
		else {
			paymentList = paymentService.findAllPaymentsByUserId(user.getUserId());
		}
		
		model.addAttribute("user", user);
		model.addAttribute("finalDate", finalDate);
		model.addAttribute("startDate", startDate);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("paymentsEmpty", paymentList.isEmpty());
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
	@GetMapping("/make-payment")
	public String makePaymentPage(Model model,
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
	 * Make payment
	 * @param model
	 * @param principal
	 * @param payment
	 * @param bindingResult
	 * @param accountFromId
	 * @param accountToNumber
	 * @param amount
	 * @param appointment
	 * @return redirect:/make-payment
	 */
	@PostMapping("/make-payment")
	public String makePayment(Model model,
	                          Principal principal,
	                          @ModelAttribute("payment") @Valid Payment payment,
	                          BindingResult bindingResult,
	                          @RequestParam("accountFromId") Long accountFromId,
	                          @RequestParam("accountToNumber") String accountToNumber,
	                          @RequestParam("amount") BigDecimal amount,
	                          @RequestParam("appointment") String appointment
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Account> accounts = accountService.findAllActivateAccountsByUserId(user.getUserId());
		if (bindingResult.hasErrors()) {
			model.addAttribute("paymentError", "invalidData");
			return "user/userMakePayment";
		}
		if (!paymentService.makePaymentOnAccount(accountFromId, accountToNumber, amount, appointment, model, principal)) {
			model.addAttribute("paymentError", "errorMakePayment");
			return "user/userMakePayment";
		} else {
			model.addAttribute("paymentError", "paymentCompletedSuccess");
		}
		
		model.addAttribute("accountFromId", accountFromId);
		model.addAttribute("accountToNumber", accountToNumber);
		model.addAttribute("amount", amount);
		model.addAttribute("appointment", appointment);
		model.addAttribute("user", user);
		model.addAttribute("accounts", accounts);
		
		return "redirect:/make-payment";
	}
	
	/**
	 * Show payment info
	 * @param model
	 * @param principal
	 * @param paymentId
	 * @return user/userShowPaymentInfo
	 */
	@GetMapping("/paymentInfo/{paymentId}")
	public String showPaymentInfo(Model model,
	                              Principal principal,
	                              @PathVariable("paymentId") Long paymentId
	) {
		Payment payment = paymentService.findPaymentByPaymentId(paymentId);
		Account senderAccount = accountService.findAccountByAccountNumber(payment.getSenderNumber());
		Account recipientAccount = accountService.findAccountByAccountNumber(payment.getRecipientNumber());
		User recipientUser = recipientAccount.getUser();
		User senderUser = senderAccount.getUser();
		
		model.addAttribute("senderUser", senderUser);
		model.addAttribute("recipientUser", recipientUser);
		model.addAttribute("recipientIsAccount", true);
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("payment", payment);
		return "user/userShowPaymentInfo";
	}
	
	
}
