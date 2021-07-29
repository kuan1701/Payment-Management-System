package com.webproject.pms.controller.user;

import com.webproject.pms.model.entities.LogEntry;
import com.webproject.pms.service.impl.ActionLogServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class LogController {
	
	private final ActionLogServiceImpl actionLogService;
	private final UserServiceImpl userService;
	
	public LogController(ActionLogServiceImpl actionLogService, UserServiceImpl userService) {
		this.actionLogService = actionLogService;
		this.userService = userService;
	}
	
	/**
	 * Show logs of user
	 * @param model
	 * @param principal
	 * @return userShowActionLog page
	 */
	@GetMapping("/action-log")
	public String actionLogPage(Model model,
	                            Principal principal
			
	) {
		List<LogEntry> logEntryList = actionLogService.findLogEntriesByUserId(
				userService.findUserByUsername(principal.getName()).getUserId());
		
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("logEntries", logEntryList);
		return "user/userShowActionLog";
	}
}
