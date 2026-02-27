package com.example.demo.service;

import com.example.demo.object.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CheckDataServiceTests {

	// 1. 模擬 (Mock) CheckDataService 所依賴的底層 Service
	@Mock
	private SelectDataService selectDataService;

	// 2. 注入 (Inject) 模擬物件到我們真正要測試的目標中
	@InjectMocks
	private CheckDataService checkDataService;

	// 執行 Unit Test 前，初始化所有的 Mock [2]
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	@Ignore
	public void dataExists_GetUserInfo_Test() throws Exception {
		UserInfo userInfo = new UserInfo("Admin", 18);

		UserInfo result = checkDataService.getUserInfoData("User01");

		Assert.assertEquals(userInfo.toString(), result.toString());
	}

	/**
	 * 測試案例 1：Happy Path (成功案例)
	 * 驗證 void 方法是否有正確呼叫相依物件的特定方法
	 */
	@Test
	public void testProcessUserData_Success() {
		// Arrange (準備參數)
		String targetUser = "john_doe";

		// Act (執行無回傳值的 void 方法)
		checkDataService.processUserData(targetUser);

		// Assert (行為驗證)
		// 驗證 selectDataService 的 loadData 方法是否確實被傳入 "john_doe" 並精準呼叫了 1 次
		Mockito.verify(selectDataService, Mockito.times(1)).loadData(targetUser);

		// 驗證危險的 deleteData 方法「絕對沒有」被呼叫過，確保邏輯沒有誤觸
		Mockito.verify(selectDataService, Mockito.never()).deleteData(Mockito.anyString());
	}

	/**
	 * 測試案例 2：Edge Cases & Errors (異常案例)
	 * 結合您先前提到的嚴謹 Exception 測試寫法 [3, 4]
	 */
	@Test
	public void testProcessUserData_EmptyUser_ThrowsException() {
		// Arrange
		String invalidUser = "";

		try {
			// Act
			checkDataService.processUserData(invalidUser);

			// 如果跑到這裡代表沒拋出例外，測試失敗 [4]
			Assert.fail("輸入空字串應該要拋出 IllegalArgumentException，但卻沒有發生。");
		} catch (IllegalArgumentException e) {
			// Assert (狀態驗證：檢查錯誤訊息) [4]
			Assert.assertEquals("Username cannot be empty.", e.getMessage());
		}

		// Assert (行為驗證：確保發生例外時，底層的載入資料行為「絕對沒有」被執行)
		Mockito.verify(selectDataService, Mockito.never()).loadData(Mockito.anyString());
	}

}