package com.example.demo.controller;

import com.example.demo.object.UserInfo;
import com.example.demo.service.CheckDataService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class HelloController {

	private CheckDataService checkDataService = new CheckDataService();
	@GetMapping("/")
	public String index() {
		return "Greetings from Spring Boot!";
	}

	@GetMapping("/getUserInfo")
	public String hello(@RequestParam(name = "name", defaultValue = "") String name) {
		String result = "";
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			UserInfo userInfo = checkDataService.getUserInfoData(name);
			result = objectMapper.writeValueAsString(userInfo);
		}catch (Exception e){
			System.out.println("getUserInfo Error : "+e.getMessage());
		}
		System.out.println("getUserInfo result : "+result);
		return result;

	}

	@GetMapping("/getCookie")
	public String getCookie(HttpServletRequest request) {
		String username = "";
		System.out.println("getCookie");
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				System.out.println(cookie.getName());
				if ("username".equals(cookie.getName())) {
					username = cookie.getValue();
					// 在這裡處理 Cookie 的值
					break;
				}
			}
		}
		System.out.println("getCookie username : "+username);
		return username;
	}

	@GetMapping("/getSession")
	public String getSession(HttpServletRequest request) {
		HttpSession session = request.getSession();

		String username = (String) session.getAttribute("username");

		System.out.println("getSession username : "+username);
		return username;
	}
}