package com.chatapp.chatapp;

import com.chatapp.chatapp.auth.Login;
import java.util.Scanner;

// Console entry point for the Chat App.
// Part 1: shows a menu to register a user, then log in, then quit.
public class Chatapp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login registeredUser = null; // holds the account once registration succeeds
        boolean running = true;

        System.out.println("=== Chat App - Part 1: Registration and Login ===");

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
                    registeredUser = handleRegistration(scanner);
                    break;
                case "2":
                    handleLogin(scanner, registeredUser);
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
        String result = newUser.registerUser();
        System.out.println(result);

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
    // registered earlier, if there is one.
    private static void handleLogin(Scanner scanner, Login registeredUser) {
        System.out.println("--- Login ---");

        if (registeredUser == null) {
            System.out.println("No user is registered yet. Please register first.");
            return;
        }

        System.out.print("Username: ");
        String enteredUsername = scanner.nextLine().trim();

        System.out.print("Password: ");
        String enteredPassword = scanner.nextLine().trim();

        String status = registeredUser.returnLoginStatus(enteredUsername, enteredPassword);
        System.out.println(status);
    }
}