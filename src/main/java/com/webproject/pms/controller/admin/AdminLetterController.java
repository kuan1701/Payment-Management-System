package com.webproject.pms.controller.admin;

import com.webproject.pms.model.entities.Letter;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.ActionLogServiceImpl;
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

	private final UserServiceImpl userService;
	private final LetterServiceImpl letterService;
	private final ActionLogServiceImpl actionLogService;

	private List<Letter> letterList;

	public AdminLetterController(UserServiceImpl userService,
								 LetterServiceImpl letterService,
								 ActionLogServiceImpl actionLogService
	) {
		this.userService = userService;
		this.letterService = letterService;
		this.actionLogService = actionLogService;
	}
	
	/**
	 * Admin support page
	 * @param model Model
	 * @param principal Principal
	 * @return admin/adminSupport view
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
	 * @param model Model
	 * @param principal Principal
	 * @param letterId Long
	 * @return adminShowLetterInfo view
	 */
	@GetMapping("/support/letter/{letterId}")
	public String responseLetter(Model model,
                                 Principal principal,
                                 @PathVariable("letterId") Long letterId
			
	) {
		User user = userService.findUserByUsername(principal.getName());
		Letter letter = letterService.findLetterByLetterId(letterId);
		User userLetter = letter.getUser();
		letterList = letterService.findUnprocessedLetters();
		
		model.addAttribute("user", user);
		model.addAttribute("letter", letter);
		model.addAttribute("userLetter", userLetter);
		model.addAttribute("totalLetters", letterList.size());
		return "admin/adminShowLetterInfo";
	}
	
	/**
	 * Admin processing the letter
	 * @param model Model
	 * @param principal Principal
	 * @param letterId Long
	 * @return admin/adminShowLetterInfo view
	 */
	@PostMapping("/support/letter/{letterId}/processed")
	public String processingTheLetter(Model model,
                                      Principal principal,
                                      @PathVariable("letterId") Long letterId
	) {
		User user = userService.findUserByUsername(principal.getName());
		Letter letter = letterService.findLetterByLetterId(letterId);
		User userLetter = letter.getUser();
		letterList = letterService.findUnprocessedLetters();
		
		letterService.updateLetterByLetterId(letterId);
		model.addAttribute("user", user);
		model.addAttribute("letter", letter);
		model.addAttribute("userLetter", userLetter);
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("response", "letterProcessedSuccess");
		actionLogService.createLog("PROCESSED: Successful attempt to process the question", user);
		return "admin/adminShowLetterInfo";
	}
}
