package com.webproject.pms.controller.user;

import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.UserServiceImpl;
import com.webproject.pms.util.MailSender.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
public class RegistrationUserController {
	
	private final UserServiceImpl userService;
	
	@Autowired
	public RegistrationUserController(UserServiceImpl userService) {
		this.userService = userService;
	}
	
	/**
	 * Registration user form
	 * @param model
	 * @return registraion page
	 */
	@GetMapping("/registration")
	public String registrationForm(Model model)
	{
		model.addAttribute("user", new User());
		return "registration";
	}

	/**
	 * User registration process
	 * @param model
	 * @param request
	 * @param user
	 * @param bindingResult
	 * @return registration view
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	@PostMapping("/registration")
	public String registrationUser(Model model,
	                               HttpServletRequest request,
	                               @ModelAttribute("user") @Valid User user,
	                               BindingResult bindingResult
	) throws UnsupportedEncodingException, MessagingException {

		if (bindingResult.hasErrors()){
			model.addAttribute("registrationError", "Invalid data");
		}
		if (!user.getPassword().equals(user.getRepeatedPassword())){
			model.addAttribute("registrationError", "Password mismatch");
		}
		if (!userService.registrationUser(user, model, MailSender.getSiteURL(request))){
			model.addAttribute("registrationError", "Registration error");
		}
		else {
			model.addAttribute("registrationError", "Registration success");
		}
		return "registration";
	}
	
	/**
	 * Activate user via mail sender
	 * @param model
	 * @param code
	 * @return activationSuccess or activationFail view
	 */
	@GetMapping("/verify")
	public String activateUser(Model model,
	                           @Param("code") String code
	) {
		if (userService.activateUser(code)) {
			model.addAttribute("message", "User successfully activated!");
			return "activationSuccess";
		}
		else {
			model.addAttribute("message", "Activation code is not found!");
			return "activationFail";
		}
	}
}
