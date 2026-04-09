package com.example.demo.controller;

import com.example.demo.object.UserExcelDto;
import com.example.demo.service.ExcelParserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ExcelUploadController {

	@Autowired
	private ExcelParserService excelParserService;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadExcelFile(@RequestParam("file") MultipartFile file) {
		// 1. 檢查檔案是否為空
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("請選擇要上傳的檔案！");
		}

		// 2. 檢查副檔名是否為 xlsx
		String filename = file.getOriginalFilename();
		if (filename == null || !filename.endsWith(".xlsx")) {
			return ResponseEntity.badRequest().body("僅支援 .xlsx 格式的檔案！");
		}

		try {
			// 3. 呼叫 Service 解析檔案
			List<UserExcelDto> resultList = excelParserService.parseExcelFile(file);

			// 成功解析，回傳 JSON 格式的 List
			return ResponseEntity.ok(resultList);

		} catch (Exception e) {
			// 發生異常時的錯誤處理
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("檔案解析失敗: " + e.getMessage());
		}
	}
}