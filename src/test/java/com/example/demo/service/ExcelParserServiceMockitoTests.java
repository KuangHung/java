package com.example.demo.service;

import com.example.demo.object.UserExcelDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;
import java.util.List;

public class ExcelParserServiceMockitoTests {

	// 直接實例化要測試的 Service (不依賴 Spring 容器也能測)
	private final ExcelParserService excelParserService = new ExcelParserService();

	@Test
	public void testParseExcelFile_Success() throws Exception {
		// 1. 讀取 src/test/resources 下的測試用 Excel 檔案
		ClassPathResource resource = new ClassPathResource("UserExcelSample.xlsx");

		// 2. 將真實檔案包裝成 Spring 的 MockMultipartFile (模擬前端上傳的檔案)
		try (InputStream inputStream = resource.getInputStream()) {
			MockMultipartFile mockMultipartFile = new MockMultipartFile(
					"file",                       // 前端傳遞的參數名稱 (需與 Controller 的 @RequestParam 一致)
					"UserExcelSample.xlsx",       // 原始檔案名稱
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // Excel 的 MIME Type
					inputStream                   // 檔案串流
			);

			// 3. 呼叫 Service 執行真實解析
			List<UserExcelDto> resultList = excelParserService.parseExcelFile(mockMultipartFile);

			// 4. Assert 驗證結果
			Assertions.assertNotNull(resultList, "解析結果不應為 null");
			Assertions.assertFalse(resultList.isEmpty(), "解析出來的清單不應為空");
			Assertions.assertEquals(3, resultList.size(), "解析結果應該要有2筆資料");
			// 確認 Excel 資料與預期相符
			UserExcelDto row1 = resultList.get(0);
			Assertions.assertEquals("User1", row1.getName(), "第一筆姓名字段錯誤");
			Assertions.assertEquals(18, row1.getAge(), "第一筆年齡字段錯誤");
			UserExcelDto row2 = resultList.get(1);
			Assertions.assertEquals("User2", row2.getName(), "第二筆姓名字段錯誤");
			Assertions.assertEquals(32, row2.getAge(), "第二筆年齡字段錯誤");
		}
	}


}