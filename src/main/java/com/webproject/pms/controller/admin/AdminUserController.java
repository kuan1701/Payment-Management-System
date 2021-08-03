package com.webproject.pms.controller.admin;

import com.webproject.pms.model.entities.Account;
import com.webproject.pms.model.entities.Letter;
import com.webproject.pms.model.entities.Payment;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.AccountServiceImpl;
import com.webproject.pms.service.impl.LetterServiceImpl;
import com.webproject.pms.service.impl.PaymentServiceImpl;
import com.webproject.pms.service.impl.UserServiceImpl;
import com.webproject.pms.util.MailSender.MailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@Controller
public class AdminUserController {
	
	private final UserServiceImpl userService;
	private final AccountServiceImpl accountService;
	private final PaymentServiceImpl paymentService;
	private final LetterServiceImpl letterService;
	
	public AdminUserController(UserServiceImpl userService,
	                           AccountServiceImpl accountService,
	                           PaymentServiceImpl paymentService,
	                           LetterServiceImpl letterService
	) {
		this.userService = userService;
		this.accountService = accountService;
		this.paymentService = paymentService;
		this.letterService = letterService;
	}
	
	/**
	 * Admin search users
	 * @param model
	 * @param principal
	 * @param name
	 * @param email
	 * @param phone
	 * @param surname
	 * @return admin/admin page
	 */
	@PostMapping("/my-account")
	public String adminSearchUsers(Model model,
	                               Principal principal,
	                               @RequestParam("name") String name,
	                               @RequestParam("email") String email,
	                               @RequestParam("phone") String phone,
	                               @RequestParam("surname") String surname
	) {
		User user = userService.findUserByUsername(principal.getName());
		List<User> userList = userService.searchByCriteria(name, surname, phone, email);
		
		model.addAttribute("user", user);
		model.addAttribute("name", name);
		model.addAttribute("phone", phone);
		model.addAttribute("email", email);
		model.addAttribute("surname", surname);
		model.addAttribute("userList", userList);
		return "admin/admin";
	}
	
	/**
	 * Admin show user info
	 * @param model
	 * @param principal
	 * @param userId
	 * @return admin/adminShowUser page
	 */
	@GetMapping("/admin/userInfo/{userId}")
	public String adminShowUserInfo(Model model,
	                                Principal principal,
	                                @PathVariable("userId") Long userId
	) {
		User viewableUser = userService.findUserByUserId(userId);
		User user = userService.findUserByUsername(principal.getName());
		List<Account> accountList = accountService.findAllAccountsByUserId(userId);
		List<Payment> paymentList = paymentService.findAllPaymentsByUserId(userId);
		List<Letter> letterList = letterService.findUnprocessedLetters();
		
		boolean userIsAdmin = false;
		if (viewableUser.getRole().getId() == 2) {
			userIsAdmin = true;
		}
		
		model.addAttribute("user", user);
		model.addAttribute("userIsAdmin", userIsAdmin);
		model.addAttribute("paymentList", paymentList);
		model.addAttribute("accountList", accountList);
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("paymentsEmpty", paymentList.isEmpty());
		model.addAttribute("accountsEmpty", accountList.isEmpty());
		return "admin/adminShowUser";
	}
	
	/**
	 * Admin update user data page
	 * @param model
	 * @param principal
	 * @param userId
	 * @return admin/adminUpdateUserData page
	 */
	@GetMapping("/admin/updateUserData/{userId}")
	public String adminUpdateUserDataPage(Model model,
                                          Principal principal,
                                          @PathVariable("userId") Long userId
	) {
		User user = userService.findUserByUsername(principal.getName());
		User updateUser = userService.findUserByUserId(userId);
		List<Letter> letterList = letterService.findUnprocessedLetters();
		
		model.addAttribute("user", user);
		model.addAttribute("updateUser", updateUser);
		model.addAttribute("totalLetters", letterList.size());
		return "admin/adminUpdateUserData";
	}
	
	/**
	 * Admin updates user data
	 * @param model
	 * @param principal
	 * @param user
	 * @param userId
	 * @return redirect:/my-account page
	 */
	@PostMapping("/admin/updateUserData/{userId}")
	public String adminUpdateUserData(Model model,
	                                  Principal principal,
	                                  @ModelAttribute("user") User user,
	                                  @PathVariable("userId") Long userId
	) {
		User userAdmin = userService.findUserByUsername(principal.getName());
		User updateUser = userService.findUserByUserId(userId);
		List<Letter> letterList = letterService.findUnprocessedLetters();
		
		if (!userService.updateUser(user, userId)) {
			model.addAttribute("response", "dataUpdatedError");
			return "user/userUpdatePersonalData";
		}
		userService.updateUser(user, userId);
		model.addAttribute("response", "dataUpdatedSuccess");
		model.addAttribute("userAdmin", userAdmin);
		model.addAttribute("updateUser", updateUser);
		model.addAttribute("totalLetters", letterList.size());
		return "redirect:/my-account";
	}
	
	/**
	 * Admin delete user
	 * @param model
	 * @param principal
	 * @param userId
	 * @return redirect:/my-account page
	 */
	@PostMapping("/admin/delete/{userId}")
	public String adminDeleteUser(Model model,
                                  Principal principal,
                                  @PathVariable("userId") Long userId
	) {
		User viewableUser = userService.findUserByUserId(userId);
		
		if (viewableUser != null) {
			userService.deleteUser(viewableUser);
		}
		model.addAttribute("viewableUser", viewableUser);
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		return "redirect:/my-account";
	}
	
	/**
	 * Admin create page user
	 * @param model
	 * @param principal
	 * @return admin/adminAddUser page
	 */
	@GetMapping("/admin/createUser")
	public String adminCreateUserPage(Model model,
                                  Principal principal
	) {
		List<Letter> letterList = letterService.findUnprocessedLetters();
		
		model.addAttribute("totalLetters", letterList.size());
		model.addAttribute("user", userService.findUserByUsername(principal.getName()));
		model.addAttribute("newUser", new User());
		return "admin/adminAddUser";
	}
	
	/**
	 * Admin create user
	 * @param model
	 * @param principal
	 * @param request
	 * @param newUser
	 * @return admin/adminAddUser page
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 */
	@PostMapping("/admin/createUser")
	public String adminCreateUser(Model model,
                                  Principal principal,
                                  HttpServletRequest request,
                                  @ModelAttribute("newUser") User newUser
	) throws UnsupportedEncodingException, MessagingException {
		User user = userService.findUserByUsername(principal.getName());
		
		if (!userService.adminCreateUser(newUser, model, MailSender.getSiteURL(request))){
			model.addAttribute("response", "addUserError");
			return "admin/adminAddUser";
		} else {
			model.addAttribute("response", "addUserSuccess");
		}
		model.addAttribute("user", user);
		return "admin/adminAddUser";
	}
}
