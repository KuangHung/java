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
}
