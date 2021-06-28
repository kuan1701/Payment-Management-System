package com.webproject.pms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
		
		@GetMapping("/error")
		public String viewBooks() {
			
			return "error";
		}
}
