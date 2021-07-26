package com.example.mealmanager;

public class mealModel {
    String mealName, managerName, date;

    public mealModel(){

    }


    public mealModel(String mealName, String managerName, String date) {
        this.mealName = mealName;
        this.managerName = managerName;
        this.date = date;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
