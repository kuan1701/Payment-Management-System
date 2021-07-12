package com.webproject.pms.controller;

import com.webproject.pms.model.entities.Letter;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {
	
	private final UserServiceImpl userService;
	
	
	public UserController(UserServiceImpl userService) {
		this.userService = userService;
	}
	
	/**
	 *  Show user account
	 * @param model
	 * @param principal
	 * @return page user
	 */
	@GetMapping("/my-account")
	public String accountPage(
			Model model,
			Principal principal
	) {
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		return "user/user";
	}
	
	/**
	 * Show profile info for update
	 * @param model
	 * @param id
	 * @return page userUpdatePersonalData
	 */
	@GetMapping("/profile-info/{id}")
	public String showUserInfo(Model model,
	                           @PathVariable("id") Long id
	) {
		model.addAttribute("user", userService.findUserByUserId(id));
		return "user/userUpdatePersonalData";
	}
	
	@PostMapping("/profile-info/{id}")
	public String updateUser(@ModelAttribute("user") @Valid User user,
	                         BindingResult bindingResult,
	                         @PathVariable("id") Long id,
	                         Model model) {
		if (bindingResult.hasErrors()) {
			return "user/userUpdatePersonalData";
		}
		if (!userService.updateUser(user, id)) {
			model.addAttribute("registrationError", "Update data error");
			return "user/userUpdatePersonalData";
		}
		userService.updateUser(user, id);
		return "redirect:user/userUpdatePersonalData";
	}
	
	@GetMapping("/forgot-password")
	public String recoveryPage() {
		
		return "recovery";
	}
	
	@GetMapping("/support")
	public String support(Model model,
	                      @AuthenticationPrincipal User user)
	{
		model.addAttribute("user", user);
		model.addAttribute("support", new Letter());
		return "user/userSupport";
	}
}
