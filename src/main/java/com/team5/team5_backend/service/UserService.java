package com.team5.team5_backend.service;

import com.team5.team5_backend.database.DB;
import com.team5.team5_backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserService {
    @Qualifier("getInstance")
    @Autowired
    private DB myDb;

    public static String TEST_USER_ID = "test_1";

    public boolean createUser() throws IOException {
        User newUser = new User(TEST_USER_ID,"dfdf@rice.edu", "1", "2022-11-19", "1", "2");
        return myDb.createUser(newUser);
    }
    public User getUserInfo() throws IOException {
        return myDb.getUser(TEST_USER_ID);
    }
}
