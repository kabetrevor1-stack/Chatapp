package com.chatapp.chatapp.auth;

/**
 * Handles user registration and login validation for the Chat App.
 *
 * Validation rules (from the assignment brief):
 *  - Username: must contain an underscore and be no more than 5 characters long.
 *  - Password: at least 8 characters, containing a capital letter, a number,
 *    and a special character.
 *  - Cell phone number: must be a South African mobile number in
 *    international format (e.g. "+27838968976").
 *  - First name and last name are captured for use in the login welcome
 *    message, per the brief's section 2a and section 4 test data, both of
 *    which specify "Welcome <user first name>, <user last name> it is
 *    great to see you again." Note: step 1 of the brief does not explicitly
 *    list first/last name as registration inputs, so they are captured
 *    alongside the other fields here to satisfy the message requirement.
 */
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

    Login(String kyl_1, String chsecke99, String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Ensures username contains an underscore and is no more than 5 characters long
    public boolean checkUserName() {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    // Validates password complexity: 8+ chars, capital letter, number, special character
    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()|\\-+=].*");

        return hasUppercase && hasNumber && hasSpecial;
    }

    /**
     * Validates South African mobile number format: "+27" followed by a
     * valid SA mobile prefix digit (6, 7, or 8) and 8 further digits.
     *
     * Regex adapted from a South African mobile number pattern shared by
     * a contributor to the validate.js project on GitHub:
     * ansman, validate.js, GitHub Issue #235, "south african mobile number
     * regex" (2017). Available at: https://github.com/ansman/validate.js/issues/235
     * Original pattern: /^(\+?27|0)[6-8][0-9]{8}$/ - adapted here to
     * require the "+27" international code specifically.
     */
    public boolean checkCellPhoneNumber() {
        if (phoneNumber == null) {
            return false;
        }
        return phoneNumber.matches("^\\+27[6-8][0-9]{8}$");
    }

    // Returns exact messaging required by the assessment's assertEquals test data
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

    public boolean loginUser(String user, String pass) {
        return this.username != null && this.username.equals(user)
                && this.password != null && this.password.equals(pass);
    }

    /**
     * Per brief section 2a: "Welcome <user first name>, <user last name>
     * it is great to see you again."
     */
    public String returnLoginStatus(String enteredUsername, String enteredPassword) {
        if (loginUser(enteredUsername, enteredPassword)) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}