package com.ayon.austmart.Models;

public class User {
    private String userId;
    private String userPhoto;
    private String userName;
    private String status;

    public User(String userId, String userPhoto, String userName, String status) {
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.userName = userName;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
