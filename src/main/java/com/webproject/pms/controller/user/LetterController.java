package com.webproject.pms.controller.user;

import com.webproject.pms.model.entities.Letter;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.LetterServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class LetterController {
	
	private final UserServiceImpl userService;
	private final LetterServiceImpl letterService;
	
	public LetterController(UserServiceImpl userService, LetterServiceImpl letterService) {
		this.userService = userService;
		this.letterService = letterService;
	}
	
	/**
	 * Support
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
	
	@PostMapping("/support")
	public String createSupport(Model model,
	                            Principal principal,
                                @ModelAttribute("letter") Letter letter,
	                            BindingResult bindingResult
	                            ) {
		if (bindingResult.hasErrors()){
			model.addAttribute("response", "unableGetData");
			return "user/userSupport";
		}
		if (!letterService.addNewLetter(letter, principal)){
			model.addAttribute("response", "letterSentError");
			return "user/userSupport";
		} else {
			model.addAttribute("response", "letterSentSuccess");
		}
		return "redirect:/my-account";
	}
}
