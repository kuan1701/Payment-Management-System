package com.webproject.pms.controller.admin;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.Letter;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.*;
import com.webproject.pms.util.MailSender.MailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@Controller
public class AdminUserController {
	
	private final UserServiceImpl userService;
	private final LetterServiceImpl letterService;
	private final AccountServiceImpl accountService;
	private final PaymentServiceImpl paymentService;
	private final ActionLogServiceImpl actionLogService;


	public AdminUserController(UserServiceImpl userService,
							   LetterServiceImpl letterService,
							   AccountServiceImpl accountService,
							   PaymentServiceImpl paymentService,
							   ActionLogServiceImpl actionLogService) {
		this.userService = userService;
		this.letterService = letterService;
		this.accountService = accountService;
		this.paymentService = paymentService;
		this.actionLogService = actionLogService;
	}
	
	/**
	 * Admin search users
	 * @param model
	 * @param principal
	 * @param name
	 * @param email
	 * @param phone
	 * @param surname
	 * @return admin/admin page
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
		List<Account> accountList = accountService.findAllAccounts();
		List<User> userList = userService.searchByCriteria(name, surname, phone, email);

		model.addAttribute("user", user);
		model.addAttribute("name", name);
		model.addAttribute("phone", phone);
		model.addAttribute("email", email);
		model.addAttribute("surname", surname);
		model.addAttribute("userList", userList);
		model.addAttribute("totalAccounts", accountList.size());
		model.addAttribute("response", "searchUsersSuccess");
		return "admin/admin";
	}
	
	/**
	 * Admin show user info
	 * @param model
	 * @param principal
	 * @param userId
	 * @return admin/adminShowUser page
	 */
	@GetMapping("/admin/userInfo/{userId}")
	public String adminShowUserInfo(Model model,
	                                Principal principal,
	                                @PathVariable("userId") Long userId
	) {
		User viewableUser = userService.findUserByUserId(userId);
		User user = userService.findUserByUsername(principal.getName());
		List<Letter> letterList = letterService.findUnprocessedLetters();
		List<Account> accountList = accountService.findAllAccountsByUserId(userId);
		List<Payment> paymentList = paymentService.findAllPaymentsByUserId(userId);

		boolean userIsAdmin = false;
		if (viewableUser.getRole().getId() == 2) {
			userIsAdmin = true;
		}
		
		model.addAttribute("user", user);
		model.addAttribute("userIsAdmin", userIsAdmin);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("accountList", accountList);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("paymentsEmpty", paymentList.isEmpty());
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		return "admin/adminShowUser";
	}
	
	/**
	 * Admin update user data page
	 * @param model
	 * @param principal
	 * @param userId
	 * @return admin/adminUpdateUserData page
	 */
	@GetMapping("/admin/updateUserData/{userId}")
	public String adminUpdateUserDataPage(Model model,
                                          Principal principal,
                                          @PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());
		User updateUser = userService.findUserByUserId(userId);
		List<Letter> letterList = letterService.findUnprocessedLetters();
		
		model.addAttribute("user", user);
		model.addAttribute("updateUser", updateUser);
		model.addAttribute("totalLetters", letterList.size());
		return "admin/adminUpdateUserData";
	}
	
	/**
	 * Admin updates user data
	 * @param model
	 * @param principal
	 * @param userId
	 * @return redirect:/my-account page
	 */
	@PostMapping("/admin/updateUserData/{userId}")
	public String adminUpdateUserData(Model model,
	                                  Principal principal,
									  @RequestParam("name") String name,
									  @RequestParam("surname") String surname,
									  @RequestParam("phone") String phone,
									  @RequestParam("email") String email,
	                                  @PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());
		User updateUser = userService.findUserByUserId(userId);
		List<Letter> letterList = letterService.findUnprocessedLetters();
		
		if (!userService.updateUser(updateUser, userId, name, surname, phone, email, null)) {
			model.addAttribute("response", "dataUpdatedError");
			actionLogService.createLog("ERROR: Unsuccessful attempt to update personal data", user);
		}
		userService.updateUser(updateUser, userId, name, surname, phone, email, null);
		model.addAttribute("user", user);
		model.addAttribute("updateUser", updateUser);
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("response", "dataUpdatedSuccess");
		actionLogService.createLog("UPDATED: Successful attempt to update personal data", user);
		return "admin/adminUpdateUserData";
	}
	
	/**
	 * Admin delete user
	 * @param model
	 * @param principal
	 * @param userId
	 * @return redirect:/my-account page
	 */
	@PostMapping("/admin/delete/{userId}")
	public String adminDeleteUser(Model model,
                                  Principal principal,
                                  @PathVariable("userId") Long userId
	) {
		User viewableUser = userService.findUserByUserId(userId);
		User user = userService.findUserByUsername(principal.getName());

		if (viewableUser != null) {
			userService.deleteUser(viewableUser);
		}
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("user", user);
		actionLogService.createLog("DELETED: Successful attempt to delete user", user);
		return "redirect:/my-account";
	}
	
	/**
	 * Admin create page user
	 * @param model
	 * @param principal
	 * @return admin/adminAddUser page
	 */
	@GetMapping("/admin/createUser")
	public String adminCreateUserPage(Model model,
                                  Principal principal
	) {
		List<Letter> letterList = letterService.findUnprocessedLetters();
		
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("newUser", new User());
		return "admin/adminAddUser";
	}
	
	/**
	 * Admin create user
	 * @param model
	 * @param principal
	 * @param request
	 * @param newUser
	 * @return admin/adminAddUser page
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	@PostMapping("/admin/createUser")
	public String adminCreateUser(Model model,
                                  Principal principal,
                                  HttpServletRequest request,
                                  @ModelAttribute("newUser") User newUser
	) throws UnsupportedEncodingException, MessagingException {
		User user = userService.findUserByUsername(principal.getName());
		
		if (!userService.adminCreateUser(newUser, model, MailSender.getSiteURL(request))){
			model.addAttribute("user", user);
			model.addAttribute("response", "addUserError");
			actionLogService.createLog("ERROR: Unsuccessful attempt to create a new user", user);
		} else {
			model.addAttribute("user", user);
			model.addAttribute("response", "addUserSuccess");
			actionLogService.createLog("CREATED: Successful attempt to create a new user", user);

		}
		model.addAttribute("user", user);
		return "admin/adminAddUser";
	}
}
