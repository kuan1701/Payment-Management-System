package com.webproject.pms.controller;

import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class PublicPagesController {
	
	private UserServiceImpl userService;
	
	@Autowired
	public PublicPagesController(UserServiceImpl userService) {
		this.userService = userService;
	}
	
	@GetMapping("/registration")
	public String registrationPage(Model model)
	{
		model.addAttribute("userForm", new User());
		return "registration";
	}
	
	@PostMapping("/registration")
	public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {
		
		if (bindingResult.hasErrors()) {
			return "registration";
		}
		if (!userForm.getPassword().equals(userForm.getRepeatedPassword())){
			model.addAttribute("passwordError", "Пароли не совпадают");
			return "registration";
		}
		if (!userService.registrationUser(userForm)){
			model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
			return "registration";
		}
		return "redirect:/";
	}
	
	@GetMapping("/recovery")
	public String recoveryPage(){
		return "recovery";
	}
}
