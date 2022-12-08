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
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
public class UrlService {
    @Qualifier("getInstance")
    @Autowired
    private DB myDb;

    private static final String Test_URL_sha256 = "1d5b5e452709e4d25b2181f69a6e54fd9ca145b654d253c9595777c0ee8318e1";
    private static final String Test_URL_longUrl = "https://github.com/Alex1516612806/comp539_Team5_Backend/tree/db";
    private static final String Test_URL_shortUrl ="https://tinyUrl/1d5b";
    private static final String Test_URL_expireTime = "123";
    private static final String Test_URL_userID = UserService.TEST_USER_ID;
    private static final int COMPRESSION_URL_SIZE = 7;
    private static final String SHORT_URL_PREFIX = "https://Mavericks/";
    private static final int SECONDS_IN_SEVEN_DAYS=7*24*3600;

    private String hashSha256Val(String longUrl) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(longUrl.getBytes(StandardCharsets.UTF_8));
        String hashVal = Base64.getEncoder().encodeToString(hash);
        System.out.println("current hash value for " + longUrl + " is "+hashVal);
        return hashVal;
    }

    private String generateRowkeyForLongUrl(String longUrl) throws NoSuchAlgorithmException, IOException {
        int size = COMPRESSION_URL_SIZE;
        String hash = hashSha256Val(longUrl);
        while (size <= hash.length()) {
            String rowKey = hash.substring(0,size);
            if (!containsUrlRecordForShortKey(rowKey)) {
                return rowKey;
            }
            size += 1;
        }
        return hash;
    }

    private String getRowKeyFromLongUrl(String longUrl) throws NoSuchAlgorithmException {
        return getRowKeyFromLongUrlExtend(longUrl,COMPRESSION_URL_SIZE);
    }

    private String getRowKeyFromLongUrlExtend(String longUrl, int size) throws NoSuchAlgorithmException {
        return hashSha256Val(longUrl).substring(0,size);
    }

    //generate short url according to the long url
    private String generateShortUrl(String longUrl) throws NoSuchAlgorithmException, IOException{
        return SHORT_URL_PREFIX+ generateRowkeyForLongUrl(longUrl);
    }

    private String getRowkeyFromShortUrl(String shortUrl) {
        return shortUrl.substring(SHORT_URL_PREFIX.length());
    }

    //create a new record for given long url
    public Url createUrl(String longUrl,String userName) throws IOException, NoSuchAlgorithmException{
        String expireTime = Instant.now().plusSeconds(SECONDS_IN_SEVEN_DAYS).toString();
        Url newUrl = new Url(generateRowkeyForLongUrl(longUrl), longUrl,generateShortUrl(longUrl),expireTime,userName);
        myDb.createUrl(newUrl);
        return newUrl;
    }

    public Url getUrlInfo(String longUrl)throws IOException, NoSuchAlgorithmException{
        return myDb.getUrl(getRowKeyFromLongUrl(longUrl));
    }

    public boolean containsUrl(String longUrl) throws NoSuchAlgorithmException, IOException {
        String rowKey = getRowKeyFromLongUrl(longUrl);
        return myDb.touchUrl(rowKey);
    }

    public boolean containsUrlRecord(String shortUrl) throws NoSuchAlgorithmException, IOException {
        String rowKey = getRowkeyFromShortUrl(shortUrl);
        return myDb.touchUrl(rowKey);
    }

    public Url getUrlInfoFromShortUrl(String shortUrl) throws IOException {
        return myDb.getUrl(getRowkeyFromShortUrl(shortUrl));
    }

    public boolean containsUrlRecordForShortKey(String rowKey) throws IOException {
        return myDb.touchUrl(rowKey);
    }

    public Url getUrlInfoFromShortKey(String rowKey) throws IOException {
        return myDb.getUrl(rowKey);
    }

    public List<Url> getUrlWithinLastOneHour() {
        return myDb.filterUrlLimitTimestampRange();
    }
}
