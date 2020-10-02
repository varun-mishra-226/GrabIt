package com.example.grabit;

public class Customer {
    public String name;
    public String regNo;
    public String password;
    public int wallet;
    public int age;
    public int calorieTarget;
    public String gender;
    public String phone;
    public int height;
    public int weight;

    public Customer() {
    }

    public Customer(String name, String regNo, String password, int wallet, int age,
                    int calorieTarget, String gender, String phone, int height,
                    int weight) {
        this.name = name;
        this.regNo = regNo;
        this.password = password;
        this.wallet = wallet;
        this.age = age;
        this.calorieTarget = calorieTarget;
        this.gender = gender;
        this.phone = phone;
        this.height = height;
        this.weight = weight;
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

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getCalorieTarget() {
        return calorieTarget;
    }

    public void setCalorieTarget(int calorieTarget) {
        this.calorieTarget = calorieTarget;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
