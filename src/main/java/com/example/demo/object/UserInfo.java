package com.example.demo.object;

public class UserInfo {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public UserInfo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * 判斷是否可以註冊 (需滿 18 歲)
     * @return 如果 >= 18 回傳 true, 否則 false
     * @throws IllegalArgumentException 如果年齡為負數或超過 150 歲
     */
    public boolean canRegister() {
        if (this.age < 0) {
            throw new IllegalArgumentException("Age cannot be negative.");
        }
        if (this.age > 150) {
            // 假設超過 150 歲為不合理輸入
            throw new IllegalArgumentException("Age is unreasonably high.");
        }
        // 核心業務邏輯：滿 18 歲才符合資格
        return this.age >= 18;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
