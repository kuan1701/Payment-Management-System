package com.webproject.pms.controller.admin;

import com.webproject.pms.model.entities.Letter;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.LetterServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminLetterController {
	
	private final LetterServiceImpl letterService;
	private final UserServiceImpl userService;
	
	public AdminLetterController(LetterServiceImpl letterService,
	                             UserServiceImpl userService
	) {
		this.letterService = letterService;
		this.userService = userService;
	}
	
	/**
	 * Admin support page
	 * @param model
	 * @param principal
	 * @return admin/adminSupport page
	 */
	@GetMapping("/support")
	public String adminSupportPage(Model model,
	                               Principal principal
			
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<Letter> unprocessedLetters = letterService.findUnprocessedLetters();
		
		model.addAttribute("user", user);
		model.addAttribute("unprocessedLetters", unprocessedLetters);
		model.addAttribute("totalLetters", unprocessedLetters.size());
		model.addAttribute("lettersEmpty", unprocessedLetters.isEmpty());
		
		return "admin/adminSupport";
	}
	
	/**
	 * Admin is considering a question submitted by the user
	 * @param model
	 * @param principal
	 * @param letterId
	 * @return admin/adminShowLetterInfo page
	 */
	@GetMapping("/support/letter/{letterId}")
	public String responseLetter(Model model,
                                 Principal principal,
                                 @PathVariable("letterId") Long letterId
			
	) {
		User user = userService.findUserByUsername(principal.getName());
		Letter letter = letterService.findLetterByLetterId(letterId);
		User userLetter = letter.getUser();
		List<Letter> letterList = letterService.findUnprocessedLetters();
		
		model.addAttribute("user", user);
		model.addAttribute("userLetter", userLetter);
		model.addAttribute("letter", letter);
		model.addAttribute("totalLetters", letterList.size());
		return "admin/adminShowLetterInfo";
	}
	
	/**
	 * Admin processing the letter
	 * @param model
	 * @param principal
	 * @param letterId
	 * @return redirect:/admin/support page
	 */
	@PostMapping("/support/letter/{letterId}/processed")
	public String processingTheLetter(Model model,
                                      Principal principal,
                                      @PathVariable("letterId") Long letterId
	) {
		User user = userService.findUserByUsername(principal.getName());
		
		letterService.updateLetterByLetterId(letterId);
		model.addAttribute("response", "letterProcessedSuccess");
		model.addAttribute("user", user);
		return "redirect:/admin/support";
	}
}
