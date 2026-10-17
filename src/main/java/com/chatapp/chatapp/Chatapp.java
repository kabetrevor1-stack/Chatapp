package com.chatapp.chatapp;

import com.chatapp.chatapp.auth.Login;
import java.util.Scanner;

// Console entry point for the Chat App.
// Part 1: register and log in.
// Part 2: after a successful login the user is taken to QuickChat.
public class Chatapp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login registeredUser = null; // will hold the account details once registered
        boolean userIsRegistered = false; // true once someone has registered successfully
        boolean running = true;

        System.out.println("=== Chat App ===");

        // Keep showing the menu until the user chooses to quit
        while (running) {
            System.out.println();
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    Login newUser = handleRegistration(scanner);
                    if (newUser != null) {
                        registeredUser = newUser;
                        userIsRegistered = true;
                    }
                    break;
                case "2":
                    boolean loginSuccessful = handleLogin(scanner, registeredUser, userIsRegistered);
                    // Messages can only be sent after a successful login
                    if (loginSuccessful) {
                        QuickChat.run(scanner);
                        running = false;
                        System.out.println("Goodbye!");
                    }
                    break;
                case "3":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, please enter 1, 2, or 3.");
            }
        }

        scanner.close();
    }

    // Asks for username, password, and cell phone number, then tries to
    // register the user. Returns the new Login object if it worked, or
    // null if one of the checks failed.
    private static Login handleRegistration(Scanner scanner) {
        System.out.println("--- Register ---");

        System.out.print("Enter a username (must contain '_' and be no more than 5 characters): ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter a password (8+ chars, capital letter, number, special character): ");
        String password = scanner.nextLine().trim();

        System.out.print("Enter your South African cell phone number (e.g. +27838968976): ");
        String cellPhoneNumber = scanner.nextLine().trim();

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine().trim();

        Login newUser = new Login(username, password, cellPhoneNumber, firstName, lastName);
        System.out.println(newUser.registerUser());

        // Only treat the user as registered if every check actually passed
        boolean success = newUser.checkUserName()
                && newUser.checkPasswordComplexity()
                && newUser.checkCellPhoneNumber();

        if (success) {
            return newUser;
        } else {
            return null;
        }
    }

    // Asks for login details and checks them against the account that was
    // registered earlier, if there is one. Returns true if the login worked.
    private static boolean handleLogin(Scanner scanner, Login registeredUser, boolean userIsRegistered) {
        System.out.println("--- Login ---");

        if (!userIsRegistered) {
            System.out.println("No user is registered yet. Please register first.");
            return false;
        }

        System.out.print("Username: ");
        String enteredUsername = scanner.nextLine().trim();

        System.out.print("Password: ");
        String enteredPassword = scanner.nextLine().trim();

        boolean loginSuccessful = registeredUser.loginUser(enteredUsername, enteredPassword);
        System.out.println(registeredUser.returnLoginStatus(enteredUsername, enteredPassword));
        return loginSuccessful;
    }
}