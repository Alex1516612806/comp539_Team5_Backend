package com.team5.team5_backend.service;

import com.team5.team5_backend.database.DB;
import com.team5.team5_backend.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UrlService {
    @Qualifier("getInstance")
    @Autowired
    private DB myDb;

    private static String Test_URL_sha256 = "1d5b5e452709e4d25b2181f69a6e54fd9ca145b654d253c9595777c0ee8318e1";
    private static String Test_URL_longUrl = "https://github.com/Alex1516612806/comp539_Team5_Backend/tree/db";
    private static String Test_URL_shortUrl ="https://tinyUrl/1d5b";
    private static String Test_URL_expireTime = "123";
    private static String Test_URL_userID = UserService.TEST_USER_ID;

    private String hashsha256Val(String longUrl) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(longUrl.getBytes(StandardCharsets.UTF_8));
        return hash.toString();
    }
    public String generateShortUrl(String longUrl) throws NoSuchAlgorithmException{
        int compression_url_size = 4;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(longUrl.getBytes(StandardCharsets.UTF_8));
        return "https://tinyUrl/"+hash.toString().substring(0,compression_url_size);
    }
    public boolean createUrl(String longUrl) throws IOException, NoSuchAlgorithmException{
        // TODO: Get the user info first.
        Url newUrl = new Url(hashsha256Val(longUrl), longUrl,generateShortUrl(longUrl),Test_URL_expireTime,Test_URL_userID);
        return myDb.createUrl(newUrl);
    }
    public Url getUrlInfo(String longUrl)throws IOException, NoSuchAlgorithmException{
        return myDb.getUrl(hashsha256Val(longUrl));
    }
}
