package com.chatapp.chatapp.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Uses the exact test data specified in the assignment brief, since the
 * brief states this data will be used for marking.
 */
class LoginTest {

    // ---------- checkUserName() : assertTrue / assertFalse ----------

    @Test
    void checkUserName_correctlyFormatted_returnsTrue() {
        Login login = new Login("ktk_1", "Ch&&sec@ke99!", "+27838968976", "Trevor", "Kabe");
        assertTrue(login.checkUserName());
    }

    @Test
    void checkUserName_incorrectlyFormatted_returnsFalse() {
        // No underscore and well over 5 characters
        Login login = new Login("Trevor!!!!!!!", "Ch&&sec@ke99!", "+27838968976", "Trevor", "kabe");
        assertFalse(login.checkUserName());
    }

    // ---------- checkPasswordComplexity() : assertTrue / assertFalse ----------

    @Test
    void checkPasswordComplexity_meetsRequirements_returnsTrue() {
        Login login = new Login("ktk_1", "Ch&&sec@ke99!", "+27838968976", "Trevor", "kabe");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    void checkPasswordComplexity_doesNotMeetRequirements_returnsFalse() {
        Login login = new Login("ktk_1", "password", "+27838968976", "Trevor", "Kabe");
        assertFalse(login.checkPasswordComplexity());
    }

    // ---------- checkCellPhoneNumber() : assertTrue / assertFalse ----------

    @Test
    void checkCellPhoneNumber_correctlyFormatted_returnsTrue() {
        Login login = new Login("ktk_1", "Ch&&sec@ke99!", "+27838968976", "Trevor", "Kabe");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    void checkCellPhoneNumber_incorrectlyFormatted_returnsFalse() {
        // No international code
        Login login = new Login("ktk_1", "Ch&&sec@ke99!", "08966553", "Trevor", "kabe");
        assertFalse(login.checkCellPhoneNumber());
    }

    // ---------- registerUser() : assertEquals, exact brief wording ----------

    @Test
    void registerUser_usernameIncorrectlyFormatted_returnsUsernameMessage() {
        Login login = new Login("Trevor!!!!!!!", "Ch&&sec@ke99!", "+27838968976", "Trevor", "Kabe");
        assertEquals(
                "Username is not correctly formatted; please ensure that your username "
                        + "contains an underscore and is no more than five characters in length.",
                login.registerUser());
    }

    @Test
    void registerUser_passwordIncorrectlyFormatted_returnsPasswordMessage() {
        Login login = new Login("ktk_1", "password", "+27838968976", "Trevor", "Kabe");
        assertEquals(
                "Password is not correctly formatted; please ensure that the password "
                        + "contains at least eight characters, a capital letter, a number, "
                        + "and a special character.",
                login.registerUser());
    }

    @Test
    void registerUser_cellPhoneIncorrectlyFormatted_returnsCellMessage() {
        Login login = new Login("ktk_1", "Ch&&sec@ke99!", "08966553", "Trevor", "Kabe");
        assertEquals(
                "Cell number is incorrectly formatted or does not contain an international "
                        + "code; please correct the number and try again.",
                login.registerUser());
    }

    @Test
    void registerUser_allValid_returnsCombinedSuccessMessage() {
        Login login = new Login("ktk_1", "Ch&&sec@ke99!", "+27838968976", "Trevor", "Kabe");
        assertEquals(
                "Username successfully captured. Password successfully captured. "
                        + "Cell number successfully captured.",
                login.registerUser());
    }

    // ---------- loginUser() / returnLoginStatus() : assertTrue/False and assertEquals ----------

    @Test
    void loginUser_correctCredentials_returnsTrue() {
        Login login = new Login("ktk_1", "Ch&&sec@ke99!", "+27838968976", "Trevor", "Kabe");
        assertTrue(login.loginUser("ktk_1", "Ch&&sec@ke99!"));
    }

    @Test
    void loginUser_incorrectCredentials_returnsFalse() {
        Login login = new Login("ktk_1", "Ch&&sec@ke99!", "+27838968976", "Trevor", "Kabe");
        assertFalse(login.loginUser("ktk_1", "WrongPass1!"));
    }

    @Test
    void returnLoginStatus_correctCredentials_returnsWelcomeMessage() {
        // NOTE: "Kyle"/"Peterson" are placeholder values I chose - the brief
        // never gives an official first/last name test value, only "kyl_1"
        // for the username. Confirm the exact expected string with your
        // module guide or lecturer before relying on this specific test.
        Login login = new Login("ktk_1", "Ch&&sec@ke99!", "+27838968976", "Trevor", "Kabe");
        assertEquals("Welcome Trevor, Kabe it is great to see you again.",
                login.returnLoginStatus("ktk_1", "Ch&&sec@ke99!"));
    }

    @Test
    void returnLoginStatus_incorrectCredentials_returnsFailureMessage() {
        Login login = new Login("ktk_1", "Ch&&sec@ke99!", "+27838968976", "Trevor", "Kabe");
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus("ktk_1", "WrongPass1!"));
    }
}