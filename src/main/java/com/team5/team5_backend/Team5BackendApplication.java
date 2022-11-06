package com.team5.team5_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping(value = "demo")
    public ResponseEntity<String> displayHelloMessage() {
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
    public String shorten(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("This is for shorten url" + name);
    }

    @GetMapping("/resolve")
    public String resolve() {
        return String.format("This is resolve");
    }

    @DeleteMapping("/delete")
    void delete() {

    }
}
