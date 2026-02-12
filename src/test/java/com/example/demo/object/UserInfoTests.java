package com.example.demo.object;

import com.example.demo.controller.HelloController;
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

public class UserInfoTests {

	/**
	 * 測試目標：驗證物件建立與基本屬性存取
	 * 使用到的 Assert：assertNotNull, assertEquals
	 */
	@Test
	public void testUserInfo_Construction_And_Getter() {
		// Arrange (準備)
		String expectedName = "Alice";
		int expectedAge = 25;

		// Act (執行)
		UserInfo userInfo = new UserInfo(expectedName, expectedAge);

		// Assert (驗證)
		// 1. 驗證物件確實被建立出來了 (不為 null) -> 對應 assertNotNull
		Assert.assertNotNull("UserInfo 物件不應該為 null", userInfo);

		// 2. 驗證屬性是否正確寫入 -> 對應 assertEquals
		Assert.assertEquals("姓名應該要是 Alice", expectedName, userInfo.getName());
		Assert.assertEquals("年齡應該要是 25", expectedAge, userInfo.getAge());
	}

	/**
	 * 測試目標：驗證當傳入 null 時的行為
	 * 使用到的 Assert：assertNull
	 */
	@Test
	public void testUserInfo_Name_Is_Null() {
		// Arrange: 模擬一個姓名為 null 的使用者
		UserInfo userInfo = new UserInfo(null, 20);

		// Act & Assert
		// 驗證取得的姓名確實是 null -> 對應 assertNull
		Assert.assertNull("姓名應該要是 null", userInfo.getName());
	}

	/**
	 * 測試目標：驗證 canRegister 邏輯
	 * 使用到的 Assert：assertEquals (測試 boolean 回傳值)
	 */
	@Test
	public void testCanRegister_Success() {
		// Case 1: 剛好 18 歲 (邊界值)
		UserInfo user18 = new UserInfo("Bob", 18);
        Assert.assertTrue("18歲應該可以註冊 (true)", user18.canRegister());
	}

	/**
	 * 測試目標：驗證 canRegister 邏輯
	 * 使用到的 Assert：assertFalse (測試 boolean 回傳值)
	 */
	@Test
	public void testCanRegister_Success2() {
		// Case 2: 未滿 18 歲
		UserInfo user17 = new UserInfo("Charlie", 17);
		Assert.assertFalse("17歲不應該可以註冊 (false)", user17.canRegister());
	}

	/**
	 * 測試目標：驗證異常狀況 (Exception) - 負數年齡
	 * 使用到的 Assert：fail, assertEquals
	 * 說明：這是 JUnit 4 測試 Exception 最嚴謹的寫法 (Try-Catch-Fail)
	 */
	@Test
	public void testCanRegister_NegativeAge_ThrowsException() {
		// Arrange
		UserInfo invalidUser = new UserInfo("ErrorUser", -5);

		try {
			// Act: 執行會拋出例外的動作
			invalidUser.canRegister();

			// 如果程式跑到這一行，代表沒有拋出例外，這是不對的！
			// 因此使用 fail() 強制讓測試失敗 -> 對應 fail
			Assert.fail("輸入負數年齡應該要拋出 IllegalArgumentException，但卻沒有發生。");

		} catch (IllegalArgumentException e) {
			// Assert: 捕捉到例外後，驗證錯誤訊息是否正確
			Assert.assertEquals("錯誤訊息不符合預期", "Age cannot be negative.", e.getMessage());
		}
	}
}