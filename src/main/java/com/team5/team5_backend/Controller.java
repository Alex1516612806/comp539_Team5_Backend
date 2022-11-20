package com.team5.team5_backend;

import com.team5.team5_backend.table_object.User;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Controller {
    public static String TEST_USER_ID = "test_1";
    public DB myDb = null;

    public Controller() throws IOException {
        myDb = new DB("rice-comp-539-spring-2022", "rice-comp-539-shared-table");
        System.out.println("controller constructor works");
    }

    public boolean createUser() throws IOException {
        User newUser = new User(TEST_USER_ID,"dfdf@rice.edu", "1", "2022-11-19", "1", "2");
        return myDb.createUser(newUser);
    }

    public User getUserInfo() throws IOException {
        return myDb.getUser(TEST_USER_ID);
    }
}
