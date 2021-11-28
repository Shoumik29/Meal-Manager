package com.example.mealmanager;

public class cashModel {
    String userID, userName, date;

    public cashModel(){

    }

    public cashModel(String userID, String userName, String date) {
        this.userID = userID;
        this.userName = userName;
        this.date = date;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }
}
