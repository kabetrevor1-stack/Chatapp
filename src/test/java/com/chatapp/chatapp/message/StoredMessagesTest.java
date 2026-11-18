package com.chatapp.chatapp.message;

import com.chatapp.chatapp.message.Message;
import com.chatapp.chatapp.message.Message;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// These tests use the test data given in the Part 3 brief.
class StoredMessagesTest {

    // The five test messages. Fixed IDs make the hashes the same every time.
    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    private static final String LONGEST_TEXT =
            "Where are you? You are late! I have asked you to be on time.";

    // Fills the arrays with the test data, the same way the app does it
    @BeforeEach
    void setUp() {
        Message.resetMessages();

        message1 = new Message("1000000001", 0, "+27834557896", "Did you get the cake?");
        message2 = new Message("2000000002", 1, "+27838884567", LONGEST_TEXT);
        message3 = new Message("3000000003", 2, "+27834484567", "Yohoooo, I am at your gate.");
        message4 = new Message("4000000004", 3, "0838884567", "It is dinner time!");
        message5 = new Message("5000000005", 4, "+27838884567", "Ok, I am leaving without you.");

        message1.sentMessage(1); // Sent
        message2.sentMessage(3); // Stored
        message3.sentMessage(2); // Disregard
        message4.sentMessage(1); // Sent
        message5.sentMessage(3); // Stored
    }

    // Remove the JSON file the tests create
    @AfterEach
    void tearDown() {
        new File("storedMessages.json").delete();
    }

    // ---------- Arrays are populated correctly ----------

    @Test
    void sentMessagesArray_containsTheSentTestMessages() {
        List<String> expected = List.of("Did you get the cake?", "It is dinner time!");
        assertEquals(expected, Message.getSentMessageTexts());
    }

    @Test
    void disregardedMessagesArray_containsTheDisregardedTestMessage() {
        List<String> expected = List.of("Yohoooo, I am at your gate.");
        assertEquals(expected, Message.getDisregardedMessageTexts());
    }

    @Test
    void storedMessagesArray_containsTheStoredTestMessages() {
        List<String> expected = List.of(LONGEST_TEXT, "Ok, I am leaving without you.");
        assertEquals(expected, Message.getStoredMessageTexts());
    }

    // A loop checks every hash and ID against the message it belongs to
    @Test
    void hashAndIdArrays_matchEveryTestMessage() {
        Message[] messages = {message1, message2, message3, message4, message5};

        assertEquals(5, Message.getAllMessageHashes().size());
        assertEquals(5, Message.getAllMessageIDs().size());

        for (int i = 0; i < messages.length; i++) {
            assertEquals(messages[i].getMessageHash(), Message.getAllMessageHashes().get(i));
            assertEquals(messages[i].getMessageID(), Message.getAllMessageIDs().get(i));
        }
        assertEquals("10:0:DIDCAKE", Message.getAllMessageHashes().get(0));
        assertEquals("40:3:ITTIME", Message.getAllMessageHashes().get(3));
    }

    // ---------- Sender and recipient of stored messages ----------

    @Test
    void getStoredSendersAndRecipients_listsEveryStoredMessage() {
        String expected = "Sender: You | Recipient: +27838884567\n"
                + "Sender: You | Recipient: +27838884567\n";
        assertEquals(expected, Message.getStoredSendersAndRecipients());
    }

    // ---------- Longest message ----------

    @Test
    void getLongestStoredMessage_returnsTheLongestMessage() {
        assertEquals(LONGEST_TEXT, Message.getLongestStoredMessage());
    }

    // ---------- Search for a message ID ----------

    @Test
    void searchByMessageID_message4_returnsRecipientAndMessage() {
        String expected = "Recipient: 0838884567\nMessage: It is dinner time!";
        assertEquals(expected, Message.searchByMessageID(message4.getMessageID()));
    }

    @Test
    void searchByMessageID_unknownID_returnsNotFound() {
        assertEquals("Message ID not found.", Message.searchByMessageID("9999999999"));
    }

    // ---------- Search for a recipient ----------

    @Test
    void searchByRecipient_returnsAllMessagesForThatRecipient() {
        List<String> expected = List.of(LONGEST_TEXT, "Ok, I am leaving without you.");
        assertEquals(expected, Message.searchByRecipient("+27838884567"));
    }

    // The disregarded message was never sent or stored, so it must not be found
    @Test
    void searchByRecipient_disregardedMessage_isNotReturned() {
        assertTrue(Message.searchByRecipient("+27834484567").isEmpty());
    }

    // ---------- Delete using the message hash ----------

    @Test
    void deleteByHash_message2_returnsSuccessMessageAndRemovesIt() {
        String result = Message.deleteByHash(message2.getMessageHash());

        assertEquals("Message: \"" + LONGEST_TEXT + "\" successfully deleted.", result);
        assertEquals(List.of("Ok, I am leaving without you."), Message.getStoredMessageTexts());
        assertEquals(4, Message.getAllMessageHashes().size());
        assertEquals("Message ID not found.", Message.searchByMessageID(message2.getMessageID()));
    }

    // The JSON file must be updated too, or the message would come back after a restart
    @Test
    void deleteByHash_storedMessage_isAlsoRemovedFromTheJsonFile() {
        Message.deleteByHash(message2.getMessageHash());

        // Clear the program's memory, then read the file back in
        Message.resetMessages();
        int loaded = Message.loadStoredMessages();

        assertEquals(1, loaded);
        assertEquals("Ok, I am leaving without you.", Message.getStoredMessageTexts().get(0));
    }

    @Test
    void deleteByHash_unknownHash_returnsNotFound() {
        assertEquals("Message hash not found.", Message.deleteByHash("00:0:NOTHERE"));
    }

    // ---------- Reports ----------

    @Test
    void getReport_sent_showsHashRecipientAndMessageForEverySentMessage() {
        String expected = "Message ID: 1000000001\n"
                + "Message Hash: 10:0:DIDCAKE\n"
                + "Recipient: +27834557896\n"
                + "Message: Did you get the cake?\n\n"
                + "Message ID: 4000000004\n"
                + "Message Hash: 40:3:ITTIME\n"
                + "Recipient: 0838884567\n"
                + "Message: It is dinner time!\n\n";
        assertEquals(expected, Message.getReport("Sent"));
    }

    @Test
    void getReport_stored_showsTheStoredMessages() {
        String report = Message.getReport("Stored");

        assertTrue(report.contains(message2.getMessageHash()));
        assertTrue(report.contains(LONGEST_TEXT));
        assertTrue(report.contains(message5.getMessageHash()));
        assertTrue(report.contains("Ok, I am leaving without you."));
    }

    @Test
    void getReport_noMessages_returnsNoMessagesToShow() {
        Message.resetMessages();
        assertEquals("No messages to show.", Message.getReport("Stored"));
    }
}