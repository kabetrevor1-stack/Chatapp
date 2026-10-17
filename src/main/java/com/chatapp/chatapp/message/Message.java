package com.chatapp.chatapp.message;

import com.chatapp.chatapp.auth.Login;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

// This class represents one chat message. It also keeps track of all the
// messages that were sent or stored while the program is running.
public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    // These belong to the whole program, not to one message
    private static ArrayList<Message> sentMessages = new ArrayList<>();
    private static ArrayList<Message> storedMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    // Used by the app: the ID is generated and the number comes from the loop counter
    public Message(String recipient, String messageText, int messageNumber) {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    // Used by the unit tests so we can give a fixed ID
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // Creates a random ten-digit ID.
    // A loop builds a longer string of random digits and substring() then
    // cuts it to exactly ten characters (it can start with 0).
    private static String generateMessageID() {
        Random random = new Random();
        String digits = "";
        for (int i = 0; i < 15; i++) {
            digits = digits + random.nextInt(10);
        }
        return digits.substring(0, 10);
    }

    // Checks that the message ID is not more than ten characters
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // Checks that the cell number has an international code and is not too long.
    // This reuses the cell phone check from Part 1 (the regex and its reference are in Login).
    public static boolean isRecipientValid(String cellNumber) {
        Login loginChecker = new Login("", "", cellNumber, "", "");
        return loginChecker.checkCellPhoneNumber();
    }

    // Returns the message to show for the recipient number
    public static String checkRecipientCell(String cellNumber) {
        if (isRecipientValid(cellNumber)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an "
                    + "international code. Please correct the number and try again.";
        }
    }

    // Checks that the message is not longer than 250 characters
    public static boolean isMessageLengthValid(String text) {
        return text != null && text.length() <= 250;
    }

    // Returns the message to show for the message length
    public static String checkMessageLength(String text) {
        if (isMessageLengthValid(text)) {
            return "Message ready to send.";
        } else {
            int tooMany = text.length() - 250;
            return "Message exceeds 250 characters by " + tooMany + "; please reduce the size.";
        }
    }

    // Creates the hash: first two digits of the ID (substring), the message number,
    // and the first and last word of the message, all in capital letters.
    // Example: 00:0:HITONIGHT
    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        String[] words = messageText.trim().split(" ");
        String firstWord = removeSymbols(words[0]);
        String lastWord = removeSymbols(words[words.length - 1]);

        String hash = firstTwo + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // Removes anything that is not a letter or a number (like , ? or !)
    private String removeSymbols(String word) {
        String cleaned = "";
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            if (Character.isLetterOrDigit(currentChar)) {
                cleaned = cleaned + currentChar;
            }
        }
        return cleaned;
    }

    // Lets the user send (1), disregard (2) or store (3) the message
    public String sentMessage(int choice) {
        if (choice == 1) {
            sentMessages.add(this);
            totalMessagesSent++;
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage();
            return "Message successfully stored.";
        } else {
            return "Invalid option.";
        }
    }

    // Returns the details of this one message in the order the brief asks for
    public String getMessageDetails() {
        return "Message ID: " + messageID + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + messageText;
    }

    // Returns the details of every message sent while the program is running
    public static String printMessages() {
        String allMessages = "";
        for (int i = 0; i < sentMessages.size(); i++) {
            allMessages = allMessages + sentMessages.get(i).getMessageDetails() + "\n\n";
        }
        return allMessages;
    }

    // Returns how many messages have been sent
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Adds this message to the stored list and saves the whole list to a JSON file.
    // References:
    // The JSON layout (an array of objects with "name": "value" pairs, and the
    // escaping of quotes and backslashes) follows the JSON standard:
    //   Bray, T. (ed.) (2017) The JavaScript Object Notation (JSON) Data Interchange
    //   Format. RFC 8259. Available at: https://www.rfc-editor.org/rfc/rfc8259
    //   (Accessed: 15 October 2026).
    // The file is written with FileWriter:
    //   Oracle (n.d.) FileWriter (Java SE 9 and JDK 9). Available at:
    //   https://docs.oracle.com/javase/9/docs/api/java/io/FileWriter.html
    //   (Accessed: 15 October 2026).
    public void storeMessage() {
        storedMessages.add(this);

        String json = "[\n";
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json = json + "  {\n"
                    + "    \"messageID\": \"" + cleanForJson(m.messageID) + "\",\n"
                    + "    \"messageHash\": \"" + cleanForJson(m.messageHash) + "\",\n"
                    + "    \"recipient\": \"" + cleanForJson(m.recipient) + "\",\n"
                    + "    \"message\": \"" + cleanForJson(m.messageText) + "\"\n"
                    + "  }";
            if (i < storedMessages.size() - 1) {
                json = json + ",";
            }
            json = json + "\n";
        }
        json = json + "]";

        try {
            FileWriter writer = new FileWriter("storedMessages.json");
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not save the message to the JSON file.");
        }
    }

    // Puts a backslash before quotes, backslashes and tabs so the JSON stays valid
    private String cleanForJson(String text) {
        text = text.replace("\\", "\\\\");
        text = text.replace("\"", "\\\"");
        text = text.replace("\t", "\\t");
        return text;
    }

    // Clears the saved lists and the counter (used by the unit tests)
    public static void resetMessages() {
        sentMessages.clear();
        storedMessages.clear();
        totalMessagesSent = 0;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }
}