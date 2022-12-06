package com.team5.team5_backend;

import com.team5.team5_backend.entity.*;
import com.team5.team5_backend.service.UrlService;
import com.team5.team5_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


// To run the application use control + r
// Minimize the run window by using command + 4
// Stop the application use command + F2
@CrossOrigin(origins = {"http://localhost:3000", "https://comp539-team5.surge.sh"}) // allow origins from the frontend.
@SpringBootApplication
@RestController
public class Team5BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Team5BackendApplication.class, args);
    }

    @Value("${message}")
    String message;

    @Autowired
    UrlService urlService;
    @Autowired
    UserService userService;

    @GetMapping(value = "demo")
    public ResponseEntity<String> displayHelloMessage() {
        try {
            if (userService.createUser()){
                return ResponseEntity.ok("Successfully create a row in the user table");
            } else {
                return ResponseEntity.ok("Not able to create a row in the user table");
            }
        } catch (IOException e) {
            System.err.println("Exception while running bigtable connection and creation: " + e.getMessage());
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/getUser")
    public ResponseEntity<String> getUser(@RequestParam(value = "userId") String userId) {
        try {
            User user = userService.getUserInfo();
            return ResponseEntity.ok(user.getEmailAddress());
        } catch (IOException e) {
            System.err.println("Exception while getting user info: " + e.getMessage());
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "names", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/teamInfo")
    public String teamInfo() {
        return String.format("This is team 5");
    }

    @GetMapping("/shorten")
    public ResponseEntity<Url> shorten(@RequestParam(value = "url") String longUrl) throws NoSuchAlgorithmException {
        try{
            Url returnUrl =null;
            if (urlService.containsUrl(longUrl)){
                returnUrl= urlService.getUrlInfo(longUrl);
            } else {
                returnUrl=urlService.createUrl(longUrl);
            }
            return ResponseEntity.ok(returnUrl);
        }catch(IOException e){
            System.err.println("Exception while compressing the Url: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/resolve")
    public ResponseEntity<Url> resolve(@RequestParam(value = "url") String shortUrl) throws NoSuchAlgorithmException {
        try{
            Url returnUrl = null;
            if (urlService.containsUrlRecord(shortUrl)){
                returnUrl=urlService.getUrlInfoFromShortUrl(shortUrl);
            }
            return ResponseEntity.ok(returnUrl);
        }catch(IOException e){
            System.err.println("Exception while compressing the Url: " + e.getMessage());
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllUrlWithinLastOneHour")
    public ResponseEntity<List<Url>> getRecordsOfUrl() {
        List<Url> urlList = urlService.getUrlWithinLastOneHour();
        return ResponseEntity.ok(urlList);
    }

    @DeleteMapping("/delete")
    void delete() {

    }
}
