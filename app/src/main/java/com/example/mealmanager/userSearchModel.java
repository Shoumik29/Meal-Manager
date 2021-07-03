package com.example.mealmanager;

public class userSearchModel {
    public String imageURL, userName, userInstitution;

    public userSearchModel(){

    }

    public userSearchModel(String imageURL, String userName, String userInstitution) {
        this.imageURL = imageURL;
        this.userName = userName;
        this.userInstitution = userInstitution;
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


}
