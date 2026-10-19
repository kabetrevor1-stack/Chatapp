package com.chatapp.chatapp.message;

import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// These tests use the test data given in the Part 2 brief.
class MessageTest {

    // Fixed IDs so the hash tests give the same answer every time
    private static final String ID_1 = "0012345678";
    private static final String ID_2 = "0098765432";

    // Test data for message 1 and message 2
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String TEXT_1 = "Hi Trevor, can you join us for dinner tonight?";
    private static final String RECIPIENT_2 = "08575975889";
    private static final String TEXT_2 = "Hi Kabe, did you receive the payment?";

    // Start every test with an empty list and a total of 0
    @BeforeEach
    void setUp() {
        Message.resetMessages();
    }

    // Remove the JSON file the store test creates
    @AfterEach
    void tearDown() {
        new File("storedMessages.json").delete();
    }

    // ---------- Message length ----------

    @Test
    void checkMessageLength_under250_returnsReadyToSend() {
        assertEquals("Message ready to send.", Message.checkMessageLength(TEXT_1));
    }

    @Test
    void checkMessageLength_exactly250_returnsReadyToSend() {
        String text = "a".repeat(250);
        assertEquals("Message ready to send.", Message.checkMessageLength(text));
    }

    @Test
    void checkMessageLength_over250_returnsHowManyTooMany() {
        String longText = "a".repeat(300); // 50 characters too long
        assertEquals("Message exceeds 250 characters by 50; please reduce the size.",
                Message.checkMessageLength(longText));
    }

    // ---------- Recipient number ----------

    @Test
    void checkRecipientCell_correctlyFormatted_returnsSuccess() {
        assertEquals("Cell phone number successfully captured.",
                Message.checkRecipientCell(RECIPIENT_1));
    }

    @Test
    void checkRecipientCell_noInternationalCode_returnsFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.",
                Message.checkRecipientCell(RECIPIENT_2));
    }

    @Test
    void isRecipientValid_numberTooLong_returnsFalse() {
        assertFalse(Message.isRecipientValid("+27718693002123456"));
    }

    // ---------- Message hash ----------

    @Test
    void createMessageHash_testCase1_returnsCorrectHash() {
        Message message = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    void createMessageHash_briefExample_returnsHiThanks() {
        Message message = new Message(ID_1, 0, RECIPIENT_1, "Hi Trevor, thanks");
        assertEquals("00:0:HITHANKS", message.createMessageHash());
    }

    // Checks the hash for both test messages in a loop (the loop counter is the message number)
    @Test
    void createMessageHash_allTestMessages_returnCorrectHashes() {
        String[] ids = {ID_1, ID_2};
        String[] recipients = {RECIPIENT_1, RECIPIENT_2};
        String[] texts = {TEXT_1, TEXT_2};
        String[] expectedHashes = {"00:0:HITONIGHT", "00:1:HIPAYMENT"};

        for (int i = 0; i < texts.length; i++) {
            Message message = new Message(ids[i], i, recipients[i], texts[i]);
            assertEquals(expectedHashes[i], message.createMessageHash());
        }
    }

    // ---------- Message ID ----------

    // Checks the generated ID for both test messages in a loop
    @Test
    void generatedMessageID_allTestMessages_areTenDigits() {
        String[] recipients = {RECIPIENT_1, RECIPIENT_2};
        String[] texts = {TEXT_1, TEXT_2};

        for (int i = 0; i < texts.length; i++) {
            Message message = new Message(recipients[i], texts[i], i);
            String id = message.getMessageID();

            assertEquals(10, id.length());
            assertTrue(id.matches("[0-9]+"));
            assertTrue(message.checkMessageID());
            // The hash starts with the first two digits of the ID and the message number
            assertTrue(message.getMessageHash().startsWith(id.substring(0, 2) + ":" + i + ":"));
        }
    }

    @Test
    void checkMessageID_moreThanTenCharacters_returnsFalse() {
        Message message = new Message("00123456789", 0, RECIPIENT_1, TEXT_1);
        assertFalse(message.checkMessageID());
    }

    // ---------- Send, disregard and store ----------

    @Test
    void sentMessage_send_returnsSentMessage() {
        Message message = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        assertEquals("Message successfully sent.", message.sentMessage(1));
    }

    @Test
    void sentMessage_disregard_returnsPressZeroMessage() {
        Message message = new Message(ID_2, 1, RECIPIENT_2, TEXT_2);
        assertEquals("Press 0 to delete the message.", message.sentMessage(2));
    }

    @Test
    void sentMessage_store_returnsStoredMessage() {
        Message message = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        assertEquals("Message successfully stored.", message.sentMessage(3));
    }

    @Test
    void sentMessage_invalidOption_returnsInvalidOption() {
        Message message = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        assertEquals("Invalid option.", message.sentMessage(9));
    }

    // ---------- Totals and printing ----------

    // Message 1 is sent and message 2 is disregarded, so the total is 1
    @Test
    void returnTotalMessages_oneSentOneDisregarded_returnsOne() {
        Message message1 = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        Message message2 = new Message(ID_2, 1, RECIPIENT_2, TEXT_2);

        message1.sentMessage(1);
        message2.sentMessage(2);

        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    void returnTotalMessages_bothSent_returnsTwo() {
        Message message1 = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        Message message2 = new Message(ID_2, 1, RECIPIENT_2, TEXT_2);

        message1.sentMessage(1);
        message2.sentMessage(1);

        assertEquals(2, Message.returnTotalMessages());
    }

    // The details must be in this order: Message ID, Message Hash, Recipient, Message
    @Test
    void getMessageDetails_returnsDetailsInCorrectOrder() {
        Message message = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);

        String expected = "Message ID: 0012345678\n"
                + "Message Hash: 00:0:HITONIGHT\n"
                + "Recipient: +27718693002\n"
                + "Message: Hi Trevor, can you join us for dinner tonight?";
        assertEquals(expected, message.getMessageDetails());
    }

    @Test
    void printMessages_afterSending_containsTheMessageDetails() {
        Message message = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        message.sentMessage(1);

        String expected = "Message ID: 0012345678\n"
                + "Message Hash: 00:0:HITONIGHT\n"
                + "Recipient: +27718693002\n"
                + "Message: Hi Trevor, can you join us for dinner tonight?\n\n";
        assertEquals(expected, Message.printMessages());
    }

    // ---------- JSON file ----------

    @Test
    void storeMessage_savesTheMessageToAJsonFile() throws Exception {
        Message message = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        message.storeMessage();

        File jsonFile = new File("storedMessages.json");
        assertTrue(jsonFile.exists());

        String content = new String(Files.readAllBytes(jsonFile.toPath()));
        assertTrue(content.contains("\"messageID\": \"0012345678\""));
        assertTrue(content.contains("\"messageHash\": \"00:0:HITONIGHT\""));
        assertTrue(content.contains("\"recipient\": \"+27718693002\""));
        assertTrue(content.contains("\"message\": \"Hi Trevor, can you join us for dinner tonight?\""));
    }
}