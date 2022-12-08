package com.team5.team5_backend;

import com.team5.team5_backend.entity.*;
import com.team5.team5_backend.request.LoginReq;
import com.team5.team5_backend.request.UserCreateReq;
import com.team5.team5_backend.response.CreationResponse;
import com.team5.team5_backend.service.UrlService;
import com.team5.team5_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginReq loginReq) {
        try {
            User user=userService.getUserInfo(loginReq.getUsername());
            if (user==null){
                return ResponseEntity.ok("the username doesn't exist");
            } else if(!userService.loginUser(user,loginReq.getPassword())){
                return ResponseEntity.ok("The password is wrong");
            } else {
                return ResponseEntity.ok("Log in successfully");
            }
        } catch (IOException e) {
            System.err.println("Exception while getting user info: " + e.getMessage());
            return ResponseEntity.ok("exception while getting user info");
        }
    }

    @PostMapping("/user")
    public ResponseEntity<CreationResponse> createUser(@RequestBody UserCreateReq userCreateReq) throws IOException {
        CreationResponse response=userService.createUser(
                userCreateReq.getUserName(),
                userCreateReq.getEmail(),
                userCreateReq.getPassword()
        );
        // a user is created in the bigtable
        if(response.getUser()!=null){
            return new ResponseEntity<>(response,new HttpHeaders(), HttpStatus.CREATED);
        }
        // a user is not be able to created in the bigtable
        return new ResponseEntity<>(response,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/shorten")
    public ResponseEntity<Url> shorten(@RequestParam(value = "url") String longUrl,
                                       @RequestParam(value = "username") String userName) throws NoSuchAlgorithmException {
        try{
            Url returnUrl =null;
            if (urlService.containsUrl(longUrl)){
                returnUrl= urlService.getUrlInfo(longUrl);
            } else {
                returnUrl=urlService.createUrl(longUrl,userName);
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

    //get all urls within last one hour
    @GetMapping("/url/hour/1")
    public ResponseEntity<List<Url>> getRecordsOfUrl() {
        List<Url> urlList = urlService.getUrlWithinLastOneHour();
        return ResponseEntity.ok(urlList);
    }

    @GetMapping("/route")
    public RedirectView redirectWithUsingRedirectView(@RequestParam(value = "id") String rowKey) throws IOException {
         try{
              if (urlService.containsUrlRecordForShortKey(rowKey)){
                  Url returnUrl=urlService.getUrlInfoFromShortKey(rowKey);
                  return new RedirectView(returnUrl.getLongUrl());
              } else {
                  return new RedirectView("https://www.google.com/");
              }
         }catch(IOException e){
             System.err.println("Exception while compressing the Url: " + e.getMessage());
             return new RedirectView("https://www.google.com/");
         }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam(value = "shortUrl") String shortUrl,
                                         @RequestParam(value = "userName") String userName) throws NoSuchAlgorithmException, IOException {
        if(urlService.containsUrlRecord(shortUrl)){
            if (urlService.deleteUrlRecord(shortUrl,userName)){
                return ResponseEntity.ok("Delete successfully");
            } {
                return ResponseEntity.ok("Delete error");
            }
        } else {
            return ResponseEntity.ok("The short url doesn't exist");
        }
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "names", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
}
