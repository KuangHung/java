package com.example.demo.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class HelloControllerMockTests {

	@InjectMocks
	private HelloController helloController;
	@Mock
	private HttpServletRequest request;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void getCookie_Test() {
		String username = "john_doe";

		// 創建一個模擬的 HttpServletRequest
		request = Mockito.mock(HttpServletRequest.class);

		// 模擬 getCookies 方法的返回值
		Cookie[] cookies = {new Cookie("username", username)};
		Mockito.when(request.getCookies()).thenReturn(cookies);

		String result = helloController.getCookie(request);

		Assert.assertNotNull(result);
		Assert.assertEquals(username, result);
    }

	@Test
	public void getSession_Test() {
		String username = "john_doe";

		HttpSession session = Mockito.mock(HttpSession.class);

		Mockito.when(request.getSession()).thenReturn(session);
		Mockito.when(session.getAttribute("username")).thenReturn(username);

		String result = helloController.getSession(request);

		Assert.assertNotNull(result);
		Assert.assertEquals(username, result);
	}
}