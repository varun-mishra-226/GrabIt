package com.example.grabit;

public class Customer {
    public String name;
    public String regNo;
    public String password;

    public Customer(){
    }

    public Customer(String name, String regNo, String password) {
        this.name = name;
        this.regNo = regNo;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
