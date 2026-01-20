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


	private CheckDataService checkDataService = new CheckDataService();

	@Test
	@Ignore
	public void dataExists_GetUserInfo_Test() throws Exception {
		UserInfo userInfo = new UserInfo("Admin", 18);

		UserInfo result = checkDataService.getUserInfoData("User01");

		Assert.assertEquals(userInfo.toString(), result.toString());
	}
}