package com.webproject.pms.controller.user;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.Letter;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.AccountServiceImpl;
import com.webproject.pms.service.impl.ActionLogServiceImpl;
import com.webproject.pms.service.impl.LetterServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

	private final UserServiceImpl userService;
	private final LetterServiceImpl letterService;
	private final PasswordEncoder passwordEncoder;
	private final AccountServiceImpl accountService;
	private final ActionLogServiceImpl actionLogService;
	private static final Logger LOGGER = LogManager.getLogger(UserController.class);

	public UserController(ActionLogServiceImpl actionLogService, UserServiceImpl userService,
						  PasswordEncoder passwordEncoder,
						  LetterServiceImpl letterService,
						  AccountServiceImpl accountService
	) {
		this.actionLogService = actionLogService;
		this.userService = userService;
		this.letterService = letterService;
		this.accountService = accountService;
		this.passwordEncoder = passwordEncoder;
	}
	
	/**
	 *  Show user account depending on the role
	 * @param model Model
	 * @param principal Principal
	 * @return user or admin view
	 */
	@GetMapping("/my-account")
	public String accountPage(Model model,
	                          Principal principal
	) {
		List<User> userList = userService.findAllUsers();
		List<Account> accountList = accountService.findAllAccounts();
		User user = userService.findUserByUsername(principal.getName());
		List<Letter> unprocessedLetters = letterService.findUnprocessedLetters();
		List<Account> userAccountList = accountService.findAllAccountsByUserId(user.getUserId());

		model.addAttribute("user", user);
		model.addAttribute("userList", userList);
		model.addAttribute("userAccountList", userAccountList);
		model.addAttribute("totalAccounts", accountList.size());
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		model.addAttribute("totalLetters", unprocessedLetters.size());
		model.addAttribute("userAccountEmpty", userAccountList.isEmpty());

		actionLogService.createLog("SESSION_STARTED", user);
		LOGGER.info("SESSION_STARTED");

		if (user.getRole().getId() == 2) {
			return "admin/admin";
		}
		return "user/user";
	}
	
	/**
	 * Show profile info for update
	 * @param model Model
	 * @param userId Long
	 * @return page userUpdatePersonalData
	 */
	@GetMapping("/profile-info/{userId}")
	public String showUserInfo(Model model,
	                           @PathVariable("userId") Long userId
	) {
		model.addAttribute("user", userService.findUserByUserId(userId));
		return "user/userUpdatePersonalData";
	}

	/**
	 * Updating user data
	 * @param model Model
	 * @param principal Principal
	 * @param name input String
	 * @param surname input String
	 * @param phone input String
	 * @param email input String
	 * @param password input String
	 * @param userId Long
	 * @return userUpdatePersonalData view
	 */
	@PostMapping("/profile-info/{userId}")
	public String updateUser(Model model,
							 Principal principal,
	                         @RequestParam("name") String name,
	                         @RequestParam("surname") String surname,
	                         @RequestParam("phone") String phone,
	                         @RequestParam("email") String email,
	                         @RequestParam("password") String password,
	                         @PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());

		if (!userService.updateUser(user, userId, name, surname, phone, email, password)) {
			model.addAttribute("updateInfo", "Update data error");
		}
		else {
			userService.updateUser(user, userId, name, surname, phone, email, password);
			model.addAttribute("updateInfo", "Update data successfully");
		}
		model.addAttribute("user", user);
		return "user/userUpdatePersonalData";
	}
	
	/**
	 * Updating password
	 * @param model Model
	 * @param userId Long
	 * @param principal Principal
	 * @return userUpdatePassword view
	 */
	@GetMapping("/update-password/{userId}")
	public String updatePasswordForm(Model model,
	                                 Principal principal,
	                                 @PathVariable("userId") Long userId
	) {
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		return "user/userUpdatePassword";
	}

	/**
	 * Password update process
	 * @param model Model
	 * @param principal Principal
	 * @param newPassword input String
	 * @param oldPassword input String
	 * @param userId Long
	 * @return userUpdatePassword view
	 */
	@PostMapping("/update-password/{userId}")
	public String updatePassword(Model model,
	                             Principal principal,
	                             @RequestParam String newPassword,
	                             @RequestParam String oldPassword,
	                             @PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());
		
		if (oldPassword.equals(newPassword)) {
			model.addAttribute("passwordError", "You new password must be different than the old one.");
		}
		else if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			model.addAttribute("passwordError", "Your old password is incorrect.");
		}
		else {
			userService.updatePassword(user, newPassword);
			model.addAttribute("passwordError", "Update password successfully.");
		}
		model.addAttribute("user", user);
		return "user/userUpdatePassword";
	}
}
