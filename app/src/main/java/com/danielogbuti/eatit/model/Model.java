package com.danielogbuti.eatit.model;

public class Model {
    private String Name;
    private String Password;
    private String Phone;
    private String IsStaff;

    public Model() {

    }

    public Model(String name, String password) {
        Name = name;
        Password = password;
        IsStaff = "false";

    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
