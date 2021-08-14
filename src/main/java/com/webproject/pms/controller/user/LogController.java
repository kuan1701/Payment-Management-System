package com.webproject.pms.controller.user;

import com.webproject.pms.model.entities.LogEntry;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.ActionLogServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class LogController {

	private final UserServiceImpl userService;
	private final ActionLogServiceImpl actionLogService;

	private List<LogEntry> logEntryList;

	public LogController(UserServiceImpl userService,
						 ActionLogServiceImpl actionLogService)
	{
		this.userService = userService;
		this.actionLogService = actionLogService;
	}
	
	/**
	 * Show logs of user
	 * @param model Model
	 * @param principal Principal
	 * @return userShowActionLog view
	 */
	@GetMapping("/action-log/{userId}")
	public String actionLogPage(Model model,
	                            Principal principal,
								@PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());
		logEntryList = actionLogService.findLogEntriesByUserId(userId);

		model.addAttribute("user", user);
		model.addAttribute("logEntries", logEntryList);
		return "user/userShowActionLog";
	}

	/**
	 * Clearing log history
	 * @param model Model
	 * @param principal Principal
	 * @param userId Long
	 * @return redirect:/action-log/{userId} page
	 */
	@PostMapping("/action-log/{userId}")
	public String clearAllLogs(Model model,
							   Principal principal,
							   @PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());
		actionLogService.clearActionLog(userId);
		model.addAttribute("user", user);
		return "redirect:/action-log/{userId}";
	}

	/**
	 *
	 * @param model Model
	 * @param principal Principal
	 * @param userId Long
	 * @param startDate String
	 * @param finalDate String
	 * @return user/userShowActionLog view
	 */
	@PostMapping("/action-log/{userId}/search")
	public String searchByCriteria(Model model,
								   Principal principal,
								   @PathVariable("userId") Long userId,
								   @RequestParam("start-date") String startDate,
								   @RequestParam("final-date") String finalDate
	){
		User user = userService.findUserByUsername(principal.getName());
		logEntryList = actionLogService.searchByCriteria(userId, startDate, finalDate);

		model.addAttribute("user", user);
		model.addAttribute("logEntries", logEntryList);
		model.addAttribute("response", "searchLogEntriesSuccess");
		return "user/userShowActionLog";
	}
}
