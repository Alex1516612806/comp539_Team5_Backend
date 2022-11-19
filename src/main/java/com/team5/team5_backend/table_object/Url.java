package com.team5.team5_backend.table_object;

public class Url {
    private String sha256Val;
    private String longUrl;
    private String shortUrl;
    private String expireTime; // TODO: type?
    private String userID;


    public Url(String sha256Val, String longUrl, String shortUrl, String expireTime, String userID) {
        this.sha256Val = sha256Val;
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.expireTime = expireTime;
        this.userID = userID;
    }

    public String getSha256Val() {
        return sha256Val;
    }

    public void setSha256Val(String sha256Val) {
        this.sha256Val = sha256Val;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
