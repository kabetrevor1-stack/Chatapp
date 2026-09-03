package com.chatapp.chatapp.auth;

// This class handles registering a new user and logging them in.
public class Login {

    private String username;
    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    public Login(String username, String password, String phoneNumber,
                 String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Checks that the username has an underscore and is no more than 5 characters long
    public boolean checkUserName() {
        if (username == null) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    // Checks that the password is at least 8 characters and has a capital letter,
    // a number, and a special character
    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;
        String specialChars = "!@#$%^&*()|-+=";

        for (int i = 0; i < password.length(); i++) {
            char currentChar = password.charAt(i);

            if (Character.isUpperCase(currentChar)) {
                hasUpperCase = true;
            }
            if (Character.isDigit(currentChar)) {
                hasNumber = true;
            }
            if (specialChars.indexOf(currentChar) >= 0) {
                hasSpecialChar = true;
            }
        }

        return hasUpperCase && hasNumber && hasSpecialChar;
    }

    // Checks that the cell phone number is in South African international format,
    // e.g. +27838968976
    // Regex reference: ansman, 2017. South African mobile number regex.
    // GitHub Issue #235, validate.js project.
    // Available at: https://github.com/ansman/validate.js/issues/235
    public boolean checkCellPhoneNumber() {
        if (phoneNumber == null) {
            return false;
        }
        return phoneNumber.matches("^\\+27[6-8][0-9]{8}$");
    }

    // Registers the user and returns a message saying what went wrong,
    // or that everything was captured successfully
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        } else if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, "
                    + "and a special character.";
        } else if (!checkCellPhoneNumber()) {
            return "Cell number is incorrectly formatted or does not contain an international "
                    + "code; please correct the number and try again.";
        } else {
            return "Username successfully captured. Password successfully captured. "
                    + "Cell number successfully captured.";
        }
    }

    // Checks that the entered username and password match the ones stored for this user
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return username != null && username.equals(enteredUsername)
                && password != null && password.equals(enteredPassword);
    }

    // Returns a welcome message if login worked, or an error message if it did not
    public String returnLoginStatus(String enteredUsername, String enteredPassword) {
        if (loginUser(enteredUsername, enteredPassword)) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}