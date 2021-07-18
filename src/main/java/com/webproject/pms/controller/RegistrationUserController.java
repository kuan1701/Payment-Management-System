package com.webproject.pms.controller;

import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationUserController {
	
	private final UserServiceImpl userService;
	
	@Autowired
	public RegistrationUserController(UserServiceImpl userService) {
		this.userService = userService;
	}
	
	/**
	 * Registration user
	 * @param model
	 * @return registraion page
	 */
	@GetMapping("/registration")
	public String registrationPage(Model model)
	{
		model.addAttribute("user", new User());
		return "registration";
	}
	
	@PostMapping("/registration")
	public String registrationUser(
			@ModelAttribute("user") @Valid User user,
			BindingResult bindingResult,
			Model model) {
		
		if (bindingResult.hasErrors()){
			model.addAttribute("registrationError", "Invalid data");
			return "registration";
		}
		if (!user.getPassword().equals(user.getRepeatedPassword())){
			model.addAttribute("registrationError", "Password mismatch");
			return "registration";
		}
		if (!userService.registrationUser(user, model)){
			model.addAttribute("registrationError", "Registration error");
			return "registration";
		} else {
			model.addAttribute("registrationError", "Registration success");
		}
		return "registration";
	}
	
	/**
	 * Activate user via mail sender
	 * @param model
	 * @param code
	 * @return activationSuccess page
	 */
	@GetMapping("/activate/{code}")
	public String activate(Model model, @PathVariable String code) {
		
		boolean isActivated = userService.activateUser(code);
		
		if (isActivated) {
			model.addAttribute("message", "User successfully activated!");
		} else {
			model.addAttribute("message", "Activation code is not found!");
		}
		return "activationSuccess";
	}
	
	/**
	 * Registration message
	 * @return activationMessage page
	 */
	@GetMapping("/registration-message")
	public String registrationMessage()
	{
		return "activationMessage";
	}
}
