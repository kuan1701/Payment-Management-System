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
	 * Support form
	 * @param model Model
	 * @param principal Principal
	 * @return userSupport view
	 */
	@GetMapping("/support")
	public String supportFrom(Model model,
							  Principal principal
	) {
		model.addAttribute("support", new Letter());
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		return "user/userSupport";
	}

	/**
	 * Creating a letter for feedback
	 * @param model Model
	 * @param principal Principal
	 * @param letter Letter
	 * @param bindingResult BindingResult
	 * @return userSupport view
	 */
	@PostMapping("/support")
	public String createSupport(Model model,
	                            Principal principal,
                                @ModelAttribute("letter") Letter letter,
	                            BindingResult bindingResult
	) {
		User user = userService.findUserByUsername(principal.getName());
		
		if (bindingResult.hasErrors()){
			model.addAttribute("response", "unableGetData");
		}
		else if (!letterService.addNewLetter(letter, principal)){
			model.addAttribute("response", "letterSentError");
		}
		model.addAttribute("user", user);
		model.addAttribute("response", "letterSentSuccess");
		return "user/userSupport";
	}
}
