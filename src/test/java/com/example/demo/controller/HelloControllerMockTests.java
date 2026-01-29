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

	// 自動將模擬的 Mock 物件注入到測試目標 HelloController 中
	@InjectMocks
	private HelloController helloController;

	// 模擬 HttpServletRequest 物件，避免依賴真實的伺服器請求
	@Mock
	private HttpServletRequest request;

	// 在每個測試案例執行前，初始化所有的 Mock 註解
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * 測試用途：驗證 Controller 是否能正確從 Cookie 中提取 username
	 */
	@Test
	public void getCookie_Test() {
		String username = "john_doe";

		// 1. 建立一個模擬的 Request 物件
		request = Mockito.mock(HttpServletRequest.class);

		// 2. 定義行為：當呼叫 getCookies() 時，回傳內含指定 username 的 Cookie 陣列
		Cookie[] cookies = {new Cookie("username", username)};
		Mockito.when(request.getCookies()).thenReturn(cookies);

		// 3. 執行測試目標方法
		String result = helloController.getCookie(request);

		// 4. 驗證：結果不應為空，且必須等於預期的內容
		Assert.assertNotNull(result);
		Assert.assertEquals(username, result);
	}

	/**
	 * 測試用途：驗證 Controller 是否能正確從 Session 屬性中取得資料
	 */
	@Test
	public void getSession_Test() {
		String username = "john_doe";

		// 1. 建立一個模擬的 HttpSession 物件
		HttpSession session = Mockito.mock(HttpSession.class);

		// 2. 定義串連行為：呼叫 request.getSession() 時回傳上述 session，
		//    且呼叫 session.getAttribute("username") 時回傳指定字串
		Mockito.when(request.getSession()).thenReturn(session);
		Mockito.when(session.getAttribute("username")).thenReturn(username);

		// 3. 執行測試目標方法
		String result = helloController.getSession(request);

		// 4. 驗證：確保從 Session 取得的值與預期相符
		Assert.assertNotNull(result);
		Assert.assertEquals(username, result);
	}
}