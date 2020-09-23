package com.example.grabit;

public class FoodItem {
    public String name;
    public int price;
    public int calorie;
    public int carbohydrate;
    public int fat;
    public int protein;

    public FoodItem(){
    }

    public FoodItem(String name, int price, int calorie, int carbohydrate, int fat, int protein) {
        this.name = name;
        this.price = price;
        this.calorie = calorie;
        this.carbohydrate = carbohydrate;
        this.fat = fat;
        this.protein = protein;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public int getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(int carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }
}
