package com.ayon.austmart.Models;

import com.google.firebase.database.ServerValue;

public class Post {

    private String postKey;
    private String productName;
    private String description;
    private String price;
    private String userID;
    private String productPhoto;
    private String userName;
    private String userPhoto;
    private Object timeStamp;

    public Post(String productName, String description, String price, String userID, String productPhoto, String userPhoto, String userName) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.userID = userID;
        this.productPhoto = productPhoto;
        this.userPhoto = userPhoto;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.userName = userName;
    }

    public Post() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getUserID() {
        return userID;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }




}
