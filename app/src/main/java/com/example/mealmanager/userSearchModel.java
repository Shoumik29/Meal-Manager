package com.example.mealmanager;

public class userSearchModel {
    public String imageURL, userName, userInstitution, userId, mealName, userMobile;

    public userSearchModel(){

    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public userSearchModel(String imageURL, String userName, String userInstitution, String userId, String mealName, String userMobile) {
        this.imageURL = imageURL;
        this.userName = userName;
        this.userInstitution = userInstitution;
        this.userId = userId;
        this.mealName = mealName;
        this.userMobile = userMobile;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserInstitution() {
        return userInstitution;
    }

    public void setUserInstitution(String userInstitution) {
        this.userInstitution = userInstitution;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
