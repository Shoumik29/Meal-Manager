package com.example.mealmanager;

public class mealModel {
    String mealName, managerName;

    public mealModel(){

    }

    public mealModel(String mealName, String managerName) {
        this.mealName = mealName;
        this.managerName = managerName;
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
}
