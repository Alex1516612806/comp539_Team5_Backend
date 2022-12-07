package com.team5.team5_backend.validators;

public class UserValidator {
    // at least three characters with lowercase or uppercase letters or numbers
    public static boolean validateUsername(String username) {
        return username.matches("^[a-zA-Z0-9._-]{3,}$");
    }

    //use abcde@rice.edu //
    public static boolean validateEmail(String email) {
        return email.matches("^([a-zA-Z0-9_\\-.]+)@([a-zA-ZÀ-ȕ0-9_\\-.]+)\\.([a-zA-ZÀ-ȕ]{2,5})$");
    }
}
