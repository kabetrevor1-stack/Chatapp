package com.chatapp.chatapp.message;

import com.chatapp.chatapp.auth.Login;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
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

    // Part 3 arrays. They are filled while the program runs (nothing is hard-coded).
    private static ArrayList<String> sentMessageTexts = new ArrayList<>();
    private static ArrayList<String> disregardedMessageTexts = new ArrayList<>();
    private static ArrayList<String> storedMessageTexts = new ArrayList<>();
    private static ArrayList<String> allMessageHashes = new ArrayList<>();
    private static ArrayList<String> allMessageIDs = new ArrayList<>();

    // Parallel arrays: the same position in each list is the same message.
    // They let us search by ID, hash or recipient and find the other details.
    private static ArrayList<String> allRecipients = new ArrayList<>();
    private static ArrayList<String> allMessageTexts = new ArrayList<>();
    private static ArrayList<String> allFlags = new ArrayList<>();

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

    // Lets the user send (1), disregard (2) or store (3) the message.
    // Each choice also adds the message to the Part 3 arrays.
    public String sentMessage(int choice) {
        if (choice == 1) {
            sentMessages.add(this);
            totalMessagesSent++;
            addToArrays("Sent");
            return "Message successfully sent.";
        } else if (choice == 2) {
            addToArrays("Disregard");
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage();
            addToArrays("Stored");
            return "Message successfully stored.";
        } else {
            return "Invalid option.";
        }
    }

    // Adds this message to the Part 3 arrays. The flag is "Sent", "Disregard" or "Stored".
    private void addToArrays(String flag) {
        // Parallel arrays: every message is added at the same position in each list
        allMessageIDs.add(messageID);
        allMessageHashes.add(messageHash);
        allRecipients.add(recipient);
        allMessageTexts.add(messageText);
        allFlags.add(flag);

        // Each message also goes into the array that matches its flag
        if (flag.equals("Sent")) {
            sentMessageTexts.add(messageText);
        } else if (flag.equals("Disregard")) {
            disregardedMessageTexts.add(messageText);
        } else {
            storedMessageTexts.add(messageText);
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
        writeStoredMessagesFile();
    }

    // Writes every message in the stored list to storedMessages.json.
    // It is also used after a stored message is deleted, so the file stays up to date.
    private static void writeStoredMessagesFile() {
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
    private static String cleanForJson(String text) {
        text = text.replace("\\", "\\\\");
        text = text.replace("\"", "\\\"");
        text = text.replace("\t", "\\t");
        return text;
    }
    
    // Reads storedMessages.json and adds every message in it to the stored arrays.
    // Returns how many messages were loaded. A message that is already in the
    // arrays is skipped, so calling this twice never creates duplicates.
    // References:
    // The file is read with Gson's JsonParser, which turns the JSON text into
    // JsonArray and JsonObject objects that we can loop through:
    //   Google (2024) Gson (Version 2.11.0) [Java library]. Available at:
    //   https://github.com/google/gson (Accessed: 29 October 2026).
    //   Google (n.d.) Gson User Guide. Available at:
    //   https://github.com/google/gson/blob/main/UserGuide.md
    //   (Accessed: 29 October 2026).
    public static int loadStoredMessages() {
        File jsonFile = new File("storedMessages.json");
        int loadedCount = 0;

        // Nothing to read if no message has been stored yet
        if (!jsonFile.exists()) {
            return 0;
        }

        // try-with-resources closes the file for us when we are done reading
        try (FileReader reader = new FileReader(jsonFile)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject item = jsonArray.get(i).getAsJsonObject();
                String id = item.get("messageID").getAsString();
                String hash = item.get("messageHash").getAsString();
                String recipient = item.get("recipient").getAsString();
                String text = item.get("message").getAsString();

                // Skip a message that is already in the arrays
                if (!allMessageIDs.contains(id)) {
                    // The message number is the middle part of the hash (e.g. 00:0:HITONIGHT)
                    int number = Integer.parseInt(hash.split(":")[1]);
                    Message loadedMessage = new Message(id, number, recipient, text);
                    storedMessages.add(loadedMessage);
                    loadedMessage.addToArrays("Stored");
                    loadedCount++;
                }
            }
        } catch (IOException | RuntimeException e) {
            System.out.println("Could not read the stored messages from the JSON file.");
        }
        return loadedCount;
    }
    
        // ---------- Part 3: Stored Messages features ----------

    // The name shown as the sender. The app only has one user, so every message has the same sender.
    private static String senderName = "You";

    public static void setSenderName(String name) {
        senderName = name;
    }

    // 1. Returns the sender and recipient of every stored message
    public static String getStoredSendersAndRecipients() {
        String result = "";
        for (int i = 0; i < allFlags.size(); i++) {
            if (allFlags.get(i).equals("Stored")) {
                result = result + "Sender: " + senderName + " | Recipient: " + allRecipients.get(i) + "\n";
            }
        }
        if (result.isEmpty()) {
            return "No stored messages.";
        }
        return result;
    }

    // 2. Returns the longest stored message (the first one found if two are the same length)
    public static String getLongestStoredMessage() {
        String longest = "";
        for (int i = 0; i < storedMessageTexts.size(); i++) {
            if (storedMessageTexts.get(i).length() > longest.length()) {
                longest = storedMessageTexts.get(i);
            }
        }
        if (longest.isEmpty()) {
            return "No stored messages.";
        }
        return longest;
    }

    // 3. Searches every message for a message ID and returns the recipient and message
    public static String searchByMessageID(String messageID) {
        for (int i = 0; i < allMessageIDs.size(); i++) {
            if (allMessageIDs.get(i).equals(messageID)) {
                return "Recipient: " + allRecipients.get(i) + "\n"
                        + "Message: " + allMessageTexts.get(i);
            }
        }
        return "Message ID not found.";
    }

    // 4. Returns every message that was sent or stored for one recipient.
    // Disregarded messages are left out because they were never sent or stored.
    public static ArrayList<String> searchByRecipient(String recipientNumber) {
        ArrayList<String> found = new ArrayList<>();
        for (int i = 0; i < allRecipients.size(); i++) {
            if (allRecipients.get(i).equals(recipientNumber) && !allFlags.get(i).equals("Disregard")) {
                found.add(allMessageTexts.get(i));
            }
        }
        return found;
    }

    // 5. Deletes a message using its hash. The message is removed from every
    // array, and from the JSON file if it was a stored message.
    public static String deleteByHash(String hash) {
        int position = allMessageHashes.indexOf(hash);
        if (position == -1) {
            return "Message hash not found.";
        }

        // Read the details before removing them (the parallel arrays share the same position)
        String id = allMessageIDs.get(position);
        String text = allMessageTexts.get(position);
        String flag = allFlags.get(position);

        // Remove from the message array that matches the flag
        if (flag.equals("Sent")) {
            sentMessageTexts.remove(text);
            removeFromMessageList(sentMessages, id);
        } else if (flag.equals("Disregard")) {
            disregardedMessageTexts.remove(text);
        } else {
            storedMessageTexts.remove(text);
            removeFromMessageList(storedMessages, id);
            writeStoredMessagesFile();
        }

        // Remove from the parallel arrays
        allMessageIDs.remove(position);
        allMessageHashes.remove(position);
        allRecipients.remove(position);
        allMessageTexts.remove(position);
        allFlags.remove(position);

        return "Message: \"" + text + "\" successfully deleted.";
    }

    // Removes the message with this ID from a list of Message objects
    private static void removeFromMessageList(ArrayList<Message> list, String id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).messageID.equals(id)) {
                list.remove(i);
                return;
            }
        }
    }

    // 6. Returns a report with the full details of every message that has this flag
    // ("Sent", "Stored" or "Disregard").
    public static String getReport(String flag) {
        String report = "";
        for (int i = 0; i < allFlags.size(); i++) {
            if (allFlags.get(i).equals(flag)) {
                report = report + "Message ID: " + allMessageIDs.get(i) + "\n"
                        + "Message Hash: " + allMessageHashes.get(i) + "\n"
                        + "Recipient: " + allRecipients.get(i) + "\n"
                        + "Message: " + allMessageTexts.get(i) + "\n\n";
            }
        }
        if (report.isEmpty()) {
            return "No messages to show.";
        }
        return report;
    }
    

    // Clears the saved lists and the counter (used by the unit tests)
    public static void resetMessages() {
        sentMessages.clear();
        storedMessages.clear();
        totalMessagesSent = 0;

        sentMessageTexts.clear();
        disregardedMessageTexts.clear();
        storedMessageTexts.clear();
        allMessageHashes.clear();
        allMessageIDs.clear();
        allRecipients.clear();
        allMessageTexts.clear();
        allFlags.clear();
    }

    // Getters for the Part 3 arrays (used by the menu and the unit tests)
    public static ArrayList<String> getSentMessageTexts() {
        return sentMessageTexts;
    }

    public static ArrayList<String> getDisregardedMessageTexts() {
        return disregardedMessageTexts;
    }

    public static ArrayList<String> getStoredMessageTexts() {
        return storedMessageTexts;
    }

    public static ArrayList<String> getAllMessageHashes() {
        return allMessageHashes;
    }

    public static ArrayList<String> getAllMessageIDs() {
        return allMessageIDs;
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