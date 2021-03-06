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
    public int currentSteps;
    public int calorieIntake;
    public int currentCalorieTaken;

    public Customer() {
    }

    public Customer(String name, String regNo, String password, int wallet, int age,
                    int calorieTarget, String gender, String phone, int height,
                    int weight, int currentSteps, int calorieIntake, int currentCalorieTaken) {
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
        this.currentSteps = currentSteps;
        this.calorieIntake = calorieIntake;
        this.currentCalorieTaken = currentCalorieTaken;
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

    public int getCurrentSteps() {
        return currentSteps;
    }

    public void setCurrentSteps(int currentSteps) {
        this.currentSteps = currentSteps;
    }

    public int getCalorieIntake() {
        return calorieIntake;
    }

    public void setCalorieIntake(int calorieIntake) {
        this.calorieIntake = calorieIntake;
    }

    public int getCurrentCalorieTaken() {
        return currentCalorieTaken;
    }

    public void setCurrentCalorieTaken(int currentCalorieTaken) {
        this.currentCalorieTaken = currentCalorieTaken;
    }
}
