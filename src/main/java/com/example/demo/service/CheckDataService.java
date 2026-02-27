package com.example.demo.service;

import com.example.demo.DemoApplication;
import com.example.demo.object.UserInfo;
import jakarta.servlet.http.Cookie;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.SpringApplication;

public class CheckDataService {

    private SelectDataService selectDataService = new SelectDataService();

    public boolean dataConformsRules(String value) throws Exception {
        if(value.equals("A1")){
            return true;
        }else if(value.equals("A2")){
            return true;
        }else{
            throw new Exception("Not found Rule!");
        }
    }

    public UserInfo getUserInfoData(String name) throws Exception {
        return selectDataService.getUserInfo(name);
    }

    /**
     * 這是一個 void 方法，沒有回傳值。
     * 主要邏輯：檢查傳入的帳號，如果有效則呼叫 SelectDataService 載入資料；若無效則拋出例外。
     */
    public void processUserData(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        selectDataService.loadData(username);
    }
}
