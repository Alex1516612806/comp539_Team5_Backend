package com.team5.team5_backend.service;

import com.team5.team5_backend.authentication.PasswordAuthentication;
import com.team5.team5_backend.database.DB;
import com.team5.team5_backend.entity.User;
import com.team5.team5_backend.response.CreationResponse;
import com.team5.team5_backend.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Qualifier("getInstance")
    @Autowired
    private DB myDb;

    public static String TEST_USER_ID = "test_1";

    private final PasswordAuthentication passwordAuthentication = new PasswordAuthentication();


    public CreationResponse createUser(String username, String email, String password) throws IOException {
        User newUser = null;
        String outcome = "";
        if (UserValidator.validateUsername(username)) {
            if (myDb.getUser(username) == null) {
                if (UserValidator.validateEmail(email)) {
                    if (PasswordAuthentication.validatePassword(password)) {
                        List<String> saltAndHash = passwordAuthentication.hash(password.toCharArray());
                        String salt = saltAndHash.get(0);
                        String hash = saltAndHash.get(1);
                        String curTime = Instant.now().toString();
                        newUser = new User(username, email, "0", curTime, salt, hash);
                        if(myDb.createUser(newUser)){
                            outcome += "new user created successfully";
                        } else {
                            outcome += "new user not be created successfully";
                        }
                    } else
                        outcome += ("Invalid password: must be at least 8 characters, contain a number, at least one uppercase, one lowercase and a special character");
                } else outcome += ("Invalid email address");
            } else outcome += ("Username already exists");
        } else outcome += ("Invalid username");
        return new CreationResponse(outcome, newUser);
    }

    public User getUserInfo(String username) throws IOException {
        return myDb.getUser(username);
    }

    public boolean loginUser(User user, String password) throws IOException {
        return passwordAuthentication.authenticate(password.toCharArray(),user.getSalt(),user.getHash());
    }
}
