package com.example.demo.object;

// 全面改用 jupiter 包的 Imports
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserInfoJUnit5Tests {

	@Test
	public void testUserInfo_Construction_And_Getter() {
		String expectedName = "Alice";
		int expectedAge = 25;
		UserInfo userInfo = new UserInfo(expectedName, expectedAge);

		// 【避坑 2】JUnit 5 的自訂錯誤提示訊息 (Message) 移到了「最後一個參數」
		Assertions.assertNotNull(userInfo, "UserInfo 物件不應該為 null");
		Assertions.assertEquals(expectedName, userInfo.getName(), "姓名應該要是 Alice");
		Assertions.assertEquals(expectedAge, userInfo.getAge(), "年齡應該要是 25");
	}

	/**
	 * 升級亮點一：參數化測試 (ParameterizedTest)
	 * 一次把 18 歲 (邊界)、25 歲的成功案例測完，不用寫兩個 @Test
	 */
	@ParameterizedTest
	@ValueSource(ints = {18, 25})
	public void testCanRegister_Success(int validAge) {
		UserInfo user = new UserInfo("ValidUser", validAge);
		Assertions.assertTrue(user.canRegister(), validAge + "歲應該可以註冊 (true)");
	}

	/**
	 * 升級亮點二：優雅異常處理 + 參數化
	 * 一次測完負數年齡與超過 150 歲的異常，徹底告別 try-catch-fail！
	 */
	@ParameterizedTest
	@ValueSource(ints = {-5, 151, 999})
	public void testCanRegister_InvalidAge_ThrowsException(int invalidAge) {
		UserInfo invalidUser = new UserInfo("ErrorUser", invalidAge);

		// 使用 assertThrows，乾淨俐落攔截 Exception
		IllegalArgumentException exception = Assertions.assertThrows(
				IllegalArgumentException.class,
				() -> invalidUser.canRegister()
		);

		// 可選：驗證例外是否包含正確的關鍵字
		Assertions.assertTrue(
				exception.getMessage().contains("Age"),
				"錯誤訊息應該包含 Age 關鍵字"
		);
	}
}