package com.webproject.pms.controller.admin;

import com.webproject.pms.model.entities.*;
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

	private List<User> userList;
	private List<Letter> letterList;
	private List<Account> accountList;
	private List<Payment> paymentList;
	private List<LogEntry> logEntryList;


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
	 * @param model Model
	 * @param principal Principal
	 * @param name String
	 * @param email String
	 * @param phone String
	 * @param surname String
	 * @return admin/admin view
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
		accountList = accountService.findAllAccounts();
		userList = userService.searchByCriteria(name, surname, phone, email);

		if(!userList.isEmpty()) {
			model.addAttribute("response", "searchUsersSuccess");
		}

		model.addAttribute("user", user);
		model.addAttribute("name", name);
		model.addAttribute("phone", phone);
		model.addAttribute("email", email);
		model.addAttribute("surname", surname);
		model.addAttribute("userList", userList);
		model.addAttribute("totalAccounts", accountList.size());
		return "admin/admin";
	}
	
	/**
	 * Admin show user info
	 * @param model Model
	 * @param principal Principal
	 * @param userId Long
	 * @return admin/adminShowUser view
	 */
	@GetMapping("/admin/userInfo/{userId}")
	public String adminShowUserInfo(Model model,
	                                Principal principal,
	                                @PathVariable("userId") Long userId
	) {
		User viewableUser = userService.findUserByUserId(userId);
		User user = userService.findUserByUsername(principal.getName());
		letterList = letterService.findUnprocessedLetters();
		accountList = accountService.findAllAccountsByUserId(userId);
		paymentList = paymentService.findAllPaymentsByUserId(userId);

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
	 * @param model Model
	 * @param principal Principal
	 * @param userId Long
	 * @return admin/adminUpdateUserData view
	 */
	@GetMapping("/admin/updateUserData/{userId}")
	public String adminUpdateUserDataPage(Model model,
                                          Principal principal,
                                          @PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());
		User updateUser = userService.findUserByUserId(userId);
		letterList = letterService.findUnprocessedLetters();
		
		model.addAttribute("user", user);
		model.addAttribute("updateUser", updateUser);
		model.addAttribute("totalLetters", letterList.size());
		return "admin/adminUpdateUserData";
	}
	
	/**
	 * Admin updates user data
	 * @param model Model
	 * @param principal Principal
	 * @param userId Long
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
		letterList = letterService.findUnprocessedLetters();
		
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
	 * @param model Model
	 * @param principal Principal
	 * @param userId Long
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
		model.addAttribute("user", user);
		model.addAttribute("viewableUser", viewableUser);
		actionLogService.createLog("DELETED: Successful attempt to delete user", user);
		return "redirect:/my-account";
	}
	
	/**
	 * Admin create page user
	 * @param model Model
	 * @param principal Principal
	 * @return admin/adminAddUser view
	 */
	@GetMapping("/admin/createUser")
	public String adminCreateUserPage(Model model,
                                  Principal principal
	) {
		List<Letter> letterList = letterService.findUnprocessedLetters();

		model.addAttribute("newUser", new User());
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		return "admin/adminAddUser";
	}
	
	/**
	 * Admin create user
	 * @param model Model
	 * @param principal Principal
	 * @param request HttpServletRequest
	 * @param newUser User
	 * @return admin/adminAddUser page
	 * @throws UnsupportedEncodingException Exception
	 * @throws MessagingException Exception
	 */
	@PostMapping("/admin/createUser")
	public String adminCreateUser(Model model,
                                  Principal principal,
                                  HttpServletRequest request,
                                  @ModelAttribute("newUser") User newUser
	) throws UnsupportedEncodingException, MessagingException {
		User user = userService.findUserByUsername(principal.getName());
		
		if (!userService.adminCreateUser(newUser, MailSender.getSiteURL(request))){
			model.addAttribute("user", user);
			model.addAttribute("response", "addUserError");
			actionLogService.createLog("ERROR: Unsuccessful attempt to create a new user", user);
		}
		else {
			model.addAttribute("user", user);
			model.addAttribute("response", "addUserSuccess");
			actionLogService.createLog("CREATED: Successful attempt to create a new user", user);

		}
		model.addAttribute("user", user);
		return "admin/adminAddUser";
	}
}
