package com.webproject.pms.controller.admin;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.AccountServiceImpl;
import com.webproject.pms.service.impl.PaymentServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class AdminUserController {
	
	private final UserServiceImpl userService;
	private final AccountServiceImpl accountService;
	private final PaymentServiceImpl paymentService;
	
	public AdminUserController(UserServiceImpl userService, AccountServiceImpl accountService, PaymentServiceImpl paymentService) {
		this.userService = userService;
		this.accountService = accountService;
		this.paymentService = paymentService;
	}
	
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminPage(Model model,
	                        Principal principal
	) {
		List<User> userList = userService.findAllUsers();
		
		return "admin/admin";
	};
	
	/**
	 * Admin search users
	 * @param model
	 * @param principal
	 * @param name
	 * @param email
	 * @param phone
	 * @param surname
	 * @return admin/admin
	 */
	@PostMapping("/my-account")
	public String adminSearchUsers(Model model,
	                               Principal principal,
	                               @RequestParam("name") String name,
	                               @RequestParam("email") String email,
	                               @RequestParam("phone") String phone,
	                               @RequestParam("surname") String surname
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<User> userList = userService.searchByCriteria(name, surname, phone, email);
		
		model.addAttribute("user", user);
		model.addAttribute("name", name);
		model.addAttribute("phone", phone);
		model.addAttribute("email", email);
		model.addAttribute("surname", surname);
		model.addAttribute("userList", userList);
		return "admin/admin";
	}
	
	@GetMapping("/userInfo/{userId}")
	public String adminShowUserInfo(Model model,
	                                Principal principal,
	                                @PathVariable("userId") Long userId
	) {
		User viewableUser = userService.findUserByUserId(userId);
		User user = userService.findUserByUsername(principal.getName());
		Role userIsAdmin = user.getRole();
		List<Account> accountList = accountService.findAllAccountsByUserId(userId);
		List<Payment> paymentList = paymentService.findAllPaymentsByUserId(userId);
		
		model.addAttribute("user", user);
		model.addAttribute("userIsAdmin", userIsAdmin);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("accountList", accountList);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("paymentsEmpty", paymentList.isEmpty());
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		return "admin/adminShowUser";
	}
}
