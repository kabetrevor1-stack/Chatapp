package com.chatapp.chatapp;

import com.chatapp.chatapp.message.Message;
import java.util.Scanner;

// Handles the QuickChat menu and the sending of messages (Part 2).
public class QuickChat {

    // Shows the QuickChat menu and keeps running until the user chooses Quit
    public static void run(Scanner scanner) {
        System.out.println();
        System.out.println("Welcome to QuickChat.");

        // The user decides at the start how many messages they want to enter
        int maxMessages = readWholeNumber(scanner, "How many messages do you want to enter? ", 1, 1000);
        boolean messagesEntered = false;
        boolean chatRunning = true;

        while (chatRunning) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            int choice = readWholeNumber(scanner, "Choose an option: ", 1, 3);

            if (choice == 1) {
                if (messagesEntered) {
                    System.out.println("You have already entered all " + maxMessages + " messages.");
                } else {
                    enterMessages(scanner, maxMessages);
                    messagesEntered = true;
                }
            } else if (choice == 2) {
                System.out.println("Coming Soon");
            } else {
                chatRunning = false;
            }
        }
    }

    // A for loop lets the user enter only the number of messages they chose.
    // The loop counter (i) is also used as the message number.
    private static void enterMessages(Scanner scanner, int maxMessages) {
        for (int i = 0; i < maxMessages; i++) {
            System.out.println();
            System.out.println("--- Message " + (i + 1) + " of " + maxMessages + " ---");
            sendOneMessage(scanner, i);
        }

        System.out.println();
        System.out.println("Total messages sent: " + Message.returnTotalMessages());
    }

    // Asks for one message, then lets the user send, disregard or store it
    private static void sendOneMessage(Scanner scanner, int messageNumber) {
        String recipient = askForRecipient(scanner);
        String text = askForMessageText(scanner);

        Message message = new Message(recipient, text, messageNumber);
        System.out.println("Message ID generated: " + message.getMessageID());

        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        int choice = readWholeNumber(scanner, "Choose an option: ", 1, 3);

        System.out.println(message.sentMessage(choice));

        if (choice == 1) {
            // Show the full details after the message has been sent
            System.out.println(message.getMessageDetails());
        } else if (choice == 2) {
            String deleteInput = scanner.nextLine().trim();
            if (deleteInput.equals("0")) {
                System.out.println("Message deleted.");
            } else {
                System.out.println("Message disregarded.");
            }
        }
    }

    // Keeps asking until the recipient number is valid
    private static String askForRecipient(Scanner scanner) {
        String recipient = "";
        boolean valid = false;

        while (!valid) {
            System.out.print("Enter the recipient's cell number (e.g. +27718693002): ");
            recipient = scanner.nextLine().trim();
            System.out.println(Message.checkRecipientCell(recipient));
            valid = Message.isRecipientValid(recipient);
        }
        return recipient;
    }

    // Keeps asking until the message is not empty and not longer than 250 characters
    private static String askForMessageText(Scanner scanner) {
        String text = "";
        boolean valid = false;

        while (!valid) {
            System.out.print("Enter your message: ");
            text = scanner.nextLine().trim();

            if (text.isEmpty()) {
                System.out.println("Please enter a message.");
            } else {
                System.out.println(Message.checkMessageLength(text));
                valid = Message.isMessageLengthValid(text);
            }
        }
        return text;
    }

    // Keeps asking until the user types a whole number between min and max
    private static int readWholeNumber(Scanner scanner, String prompt, int min, int max) {
        int number = 0;
        boolean valid = false;

        while (!valid) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                number = Integer.parseInt(input);
                valid = number >= min && number <= max;
            } catch (NumberFormatException e) {
                valid = false;
            }

            if (!valid) {
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            }
        }
        return number;
    }
}