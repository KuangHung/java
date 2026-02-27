package com.example.demo.service;

import com.example.demo.object.UserInfo;

public class SelectDataService {
    public UserInfo getUserInfo(String name) throws Exception {
        System.out.println("getUserInfo : "+name);
        if(name.equals("User01")){
            return new UserInfo("Admin", 18);
        }else if(name.equals("User02")){
            return new UserInfo("User02", 13);
        }else{
            throw new Exception("Not found User!");
        }
    }

    public void loadData(String username) {
        System.out.println("載入使用者資料: " + username);
    }

    public void deleteData(String username) {
        System.out.println("刪除使用者資料: " + username);
    }
}
