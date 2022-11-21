package com.team5.team5_backend;

import com.team5.team5_backend.table_object.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


// To run the application use control + r
// Minimize the run window by using command + 4
// Stop the application use command + F2
@SpringBootApplication
@RestController
public class Team5BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(Team5BackendApplication.class, args);
    }

    @Value("${message}")
    String message;

    @Autowired
    Controller controller;

    @GetMapping(value = "demo")
    public ResponseEntity<String> displayHelloMessage() {
        try {
            if (controller.createUser()){
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
            User user = controller.getUserInfo();
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

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestParam(value = "url") String longUrl) throws NoSuchAlgorithmException {
        try{
            if(controller.createUrl(longUrl)){
                message = controller.generateShortUrl(longUrl);
            }
        }catch(IOException e){
            System.err.println("Exception while compressing the Url: " + e.getMessage());
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/resolve")
    public ResponseEntity<String> resolve(@RequestParam(value = "url") String shortUrl) throws NoSuchAlgorithmException {
        try{
            Url longUrl_Info = controller.getUrlInfo(shortUrl);
            String shortenUrl = longUrl_Info.getShortUrl();
            message = shortenUrl;
        }catch(IOException e){
            System.err.println("Exception while compressing the Url: " + e.getMessage());
        }
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete")
    void delete() {

    }
}
