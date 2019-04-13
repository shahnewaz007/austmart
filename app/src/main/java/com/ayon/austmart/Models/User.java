package com.ayon.austmart.Models;

public class User {
    private String userId;
    private String userPhoto;
    private String userName;

    public User(String userId, String userPhoto, String userName) {
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.userName = userName;
    }


    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getUserName() {
        return userName;
    }
}
