package com.team5.team5_backend.table_object;

public class User {

    private String userID, emailAddress, userType, salt, hash;
    private String createdTime; // TODO: type?

    public User(String userID, String emailAddress, String userType, String createdTime, String salt, String hash) {
        this.userID = userID;
        this.emailAddress = emailAddress;
        this.salt = salt;
        this.hash = hash;
        this.userType = userType;
        this.createdTime = createdTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
