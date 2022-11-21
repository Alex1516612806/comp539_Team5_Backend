package com.team5.team5_backend.table_object;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    // public void setSha256Val() throws NoSuchAlgorithmException{
    //     this.sha256Val = hash.toString();
    // }

    public String getLongUrl() {
        return longUrl;
    }

    // public void setLongUrl(String longUrl) {
    //     this.longUrl = longUrl;
    // }

    public String getShortUrl() {
        return this.shortUrl;
    }

    // public void setShortUrl(String inputLongUrl) throws NoSuchAlgorithmException {
        
    //     // TODO: still need to heck the duplication.
    //     MessageDigest digest = MessageDigest.getInstance("SHA-256");
    //     byte[] hash = digest.digest(inputLongUrl.getBytes(StandardCharsets.UTF_8));
    //     this.shortUrl = hash.toString().substring(0,compression_url_size);
    // }

    public String getExpireTime() {
        // Java get current time.
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
