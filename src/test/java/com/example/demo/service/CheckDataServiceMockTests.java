package com.example.demo.service;

import com.example.demo.object.UserInfo;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CheckDataServiceMockTests {

	//	標註一個物件是被注入的對象，並且標記的對象的相依性（dependencies）應該被模擬並注入這個對象。
	@InjectMocks
	private CheckDataService checkDataService;

	//	標註一個物件是被模擬的（mocked）對象。
	@Mock
	private SelectDataService selectDataService;

	//	標註一個方法在每個測試方法執行之前被執行。
	@Before
	public void setup() {
		// 是 Mockito 框架提供的方法之一，用於初始化被標記為 @Mock 和 @InjectMocks 的成員變數。
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void dataExists_GetUserInfo_Test() throws Exception {
		String name = "王大明";
		int age = 18;

		Mockito.when(selectDataService.getUserInfo(name)).thenReturn(new UserInfo(name, age));
//		Mockito.when(selectDataService.getUserInfo(Mockito.anyString())).thenReturn(new UserIn。fo(name, age));

		UserInfo result = checkDataService.getUserInfoData("王大明");

		System.out.println(result);
		Assert.assertEquals("UserInfo{name='王大明', age=18}", result.toString());
	}

	//	標註一個方法是一個測試方法。當你執行測試套件時，測試框架會執行標註為 @Test 的方法。
	@Test
	public void dataConformsRules_ruleIsA1_Test() throws Exception {
		boolean result = checkDataService.dataConformsRules("A1");

		Assert.assertTrue(result);
	}

	@Test
	public void dataConformsRules_ruleIsA2_Test() throws Exception {
		boolean result = checkDataService.dataConformsRules("A2");

		Assert.assertTrue(result);
	}

	@Test
	public void dataConformsRules_Exception_Test() {
		Exception exception = Assert.assertThrows( Exception.class, () ->{
			boolean result = checkDataService.dataConformsRules("A3");
		});

		Assert.assertEquals("Not found Rule!", exception.getMessage());
	}


}