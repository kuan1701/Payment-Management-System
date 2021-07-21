package com.webproject.pms.controller;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.Letter;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.AccountServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
	
	private final UserServiceImpl userService;
	private final AccountServiceImpl accountService;
	private final PasswordEncoder passwordEncoder;
	
	public UserController(UserServiceImpl userService, AccountServiceImpl accountService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.accountService = accountService;
		this.passwordEncoder = passwordEncoder;
	}
	
	/**
	 *  Show user account
	 * @param model
	 * @param principal
	 * @return page user
	 */
	@GetMapping("/my-account")
	public String accountPage(Model model,
	                          Principal principal
	) {
		List<Account> accountList = accountService.findAllAccountsByUserId(userService.findUserByUsername(principal.getName()).getUserId());
		
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		model.addAttribute("accountList", accountList);
		return "user/user";
	}
	
	/**
	 * Show profile info for update
	 *
	 * @param model
	 * @param userId
	 * @return page userUpdatePersonalData
	 */
	@GetMapping("/profile-info/{userId}")
	public String showUserInfo(Model model,
	                           @PathVariable("userId") Long userId
	) {
		model.addAttribute("user", userService.findUserByUserId(userId));
		return "user/userUpdatePersonalData";
	}
	
	@PostMapping("/profile-info/{userId}")
	public String updateUser(@ModelAttribute("user") @Valid User user,
	                         BindingResult bindingResult,
	                         @PathVariable("userId") Long userId,
	                         Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("updateInfo", "invalidData");
			return "user/userUpdatePersonalData";
		}
		if (!userService.updateUser(user, userId)) {
			model.addAttribute("updateInfo", "Update data error");
			return "user/userUpdatePersonalData";
		}
		userService.updateUser(user, userId);
		model.addAttribute("updateInfo", "Update data successfully");
		return "redirect:/profile-info/{userId}";
	}
	
	/**
	 * Forgot password
	 *
	 * @return
	 */
	@GetMapping("/forgot-password")
	public String recoveryPage() {
		
		return "recovery";
	}
	
	/**
	 * Update password
	 *
	 * @param model
	 * @param userId
	 * @param principal
	 * @return userUpdatePassword page
	 */
	@GetMapping("/update-password/{userId}")
	public String updatePasswordPage(Model model,
	                                 Principal principal,
	                                 @PathVariable("userId") Long userId
	                                 ) {
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		return "user/userUpdatePassword";
	}
	
	@PostMapping("/update-password/{userId}")
	public String updatePassword(Model model,
	                             Principal principal,
	                             @RequestParam String newPassword,
	                             @RequestParam String oldPassword,
	                             @PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());
		model.addAttribute("user", user);
		
		if (oldPassword.equals(newPassword)) {
			model.addAttribute("passwordError", "You new password must be different than the old one.");
			return "user/userUpdatePassword";
		}
		if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
			model.addAttribute("passwordError", "Your old password is incorrect.");
			return "user/userUpdatePassword";
		} else {
			userService.updatePassword(user, newPassword);
			model.addAttribute("passwordError", "Update password successfully.");
		}
		return "user/userUpdatePassword";
	}
	
	/**
	 * Support
	 *
	 * @param model
	 * @param principal
	 * @return userSupport page
	 */
	@GetMapping("/support")
	public String support(Model model,
	                      Principal principal) {
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("support", new Letter());
		return "user/userSupport";
	}
}
