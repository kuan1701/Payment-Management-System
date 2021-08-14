package com.webproject.pms.controller.user;

import com.webproject.pms.exception.UserNotFoundException;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.UserServiceImpl;
import com.webproject.pms.util.MailSender.MailSender;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
	
	private final MailSender mailSender;
	private final UserServiceImpl userService;
	
	public ForgotPasswordController(MailSender mailSender, UserServiceImpl userService) {
		this.mailSender = mailSender;
		this.userService = userService;
	}
	
	/**
	 * Forgot password form
	 *
	 * @return recovery view
	 */
	@GetMapping("/forgot-password")
	public String showForgotPasswordForm() {
		return "recovery";
	}

	/**
	 * Password recovery process
	 * @param request HttpServletRequest
	 * @param model Model
	 * @return recovery view
	 */
	@PostMapping("/forgot-password")
	public String processForgotPassword(HttpServletRequest request,
	                                    Model model
	) {
		String email = request.getParameter("email");
		String token = RandomString.make(30);
		
		try {
			userService.updateResetPasswordToken(token, email);
			String resetPasswordLink = MailSender.getSiteURL(request) + "/reset_password?token=" + token;
			mailSender.sendEmailForResetPassword(email, resetPasswordLink);
			model.addAttribute("response", "passwordSent");
		}
		catch (UserNotFoundException ex) {
			model.addAttribute("error", ex.getMessage());
		}
		catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("response", "loginNotExist");
		}
		return "recovery";
	}

	/**
	 * Reset password form
	 * @param token String
	 * @param model Model
	 * @return userResetPassword view
	 */
	@GetMapping("/reset_password")
	public String showResetPasswordForm(@Param(value = "token") String token,
	                                    Model model
	) {
		User user = userService.getByResetPasswordToken(token);
		model.addAttribute("token", token);
		
		if (user == null) {
			model.addAttribute("response", "userNotExist");
			return "recovery";
		}
	return "user/userResetPassword";
	}

	/**
	 * Password reset process
	 * @param model Model
	 * @param token String
	 * @param newPassword String
	 * @return userResetPassword view
	 */
	@PostMapping("/reset_password")
	public String processResetPassword(Model model,
	                                   @RequestParam("token") String token,
	                                   @RequestParam("newPassword") String newPassword
	) {
		User user = userService.getByResetPasswordToken(token);
		model.addAttribute("title", "Reset your password");
		
		if (user == null) {
			model.addAttribute("response", "userNotExist");
			return "recovery";
		}
		else {
			userService.updatePassword(user, newPassword);
			model.addAttribute("passwordError", "Update password successfully");
		}
		return "user/userResetPassword";
	}
}
