package com.webproject.pms.controller;

import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
	
	private final UserServiceImpl userService;
	
	
	public UserController(UserServiceImpl userService) {
		this.userService = userService;
	}
	
	@GetMapping("/my-account")
	public String recoveryPage(Model model, @AuthenticationPrincipal User user) {
		
		model.addAttribute("user", user);
		return "user/user";
	}
	
	@GetMapping("/forgot-password")
	public String recoveryPage() {
		
		return "recovery";
	}
}
