package com.example.demo.object;

public class UserExcelDto {
    private String name;
    private int age;

    public UserExcelDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getter 與 Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}
