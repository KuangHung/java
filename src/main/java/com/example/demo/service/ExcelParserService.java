package com.example.demo.service;

import com.example.demo.object.UserExcelDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelParserService {
    public List<UserExcelDto> parseExcelFile(MultipartFile file) throws Exception {
        List<UserExcelDto> dataList = new ArrayList<>();

        // 使用 try-with-resources 確保檔案流(InputStream)與活頁簿(Workbook)一定會被關閉
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            // 取得第一個工作表 (Sheet1)
            Sheet sheet = workbook.getSheetAt(0);

            // 走訪每一行 (跳過第 0 行的標題列)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                // 防呆：如果遇到空白列就跳過
                if (row == null) {
                    continue;
                }

                // 讀取第一欄 (姓名: 字串)
                Cell nameCell = row.getCell(0);
                String name = (nameCell != null) ? nameCell.getStringCellValue() : "";

                // 讀取第二欄 (年齡: 數字)
                Cell ageCell = row.getCell(1);
                int age = (ageCell != null) ? (int) ageCell.getNumericCellValue() : 0;

                dataList.add(new UserExcelDto(name, age));
            }
        }
        return dataList;
    }
}
