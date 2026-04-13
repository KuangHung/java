package com.example.demo.controller;

import com.example.demo.object.UserExcelDto;
import com.example.demo.service.ExcelParserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUploadControllerMockTests {

	// 自動將模擬的 Mock 物件注入到測試目標 HelloController 中
	@InjectMocks
	private ExcelUploadController excelUploadController;

	@Mock
	private ExcelParserService excelParserService;

	// 在每個測試案例執行前，初始化所有的 Mock 註解
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testUploadExcelFile_Success() {
		// 1. 讀取 src/test/resources 下的測試用 Excel 檔案
		ClassPathResource resource = new ClassPathResource("UserExcelSample.xlsx");
		try (InputStream inputStream = resource.getInputStream()) {
			MockMultipartFile mockFile = new MockMultipartFile(
					"file",               // 參數 1: 對應 Controller 中 @RequestParam("file") 的名稱 (最重要！)
					"UserExcelSample.xlsx",                // 參數 2: 原始檔案名稱
					"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // Excel 的 MIME Type
					inputStream // 檔案串流
			);

			// 2. 定義行為：當呼叫 parseExcelFile() 時，回傳內含指定陣列
			Mockito.when(excelParserService.parseExcelFile(mockFile)).thenReturn(new ArrayList<>());

			// 3. 執行測試目標方法
			ResponseEntity<?> result = excelUploadController.uploadExcelFile(mockFile);

			// 4. 驗證：結果不應為空，且必須等於預期的內容
			Assert.assertNotNull(result);
			Assert.assertEquals("[]", result.getBody().toString());
		} catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}