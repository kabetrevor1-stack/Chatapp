# Chatapp - Registration, Login and QuickChat Messaging

## Student Information
* **Name:** Trevor Kabe
* **Student Number:** ST10503290
* **Module Code:** PROG5121
* **Module Name:** Programming 1A

## Project Description
This is a console-based Chat Application PoE written in Java.
* **Part 1:** registration, login and validation rules.
* **Part 2:** QuickChat, where a logged-in user can send, disregard or store messages.

## Presentation Videos
* **Part 1:** https://youtu.be/pt99f30Tm-Q
* **Part 2:** https://youtu.be/75yLAeLdu_o

## Features Implemented

### Part 1 - Registration and Login
* **Registration Validation:** Checks that the username contains an underscore and is no more than 5 characters. Verifies password complexity (8+ characters, uppercase, number, special character).
* **Cell Phone Validation:** Verifies South African phone numbers for length and international format.
* **Login:** Shows a welcome message when the details match, or an error message when they do not.
* **JUnit 5 Testing:** Automated test suite verifying validation outcomes against expected rubric test cases.

### Part 2 - QuickChat
* **Login required:** Messages can only be sent after a successful login.
* **Menu:** 1) Send Messages, 2) Show recently sent messages (Coming Soon), 3) Quit. A while loop keeps the menu running until the user quits.
* **For loop:** The user chooses how many messages to enter, and a for loop lets them enter exactly that many. The loop counter is the message number.
* **Message ID:** A random ten-digit number, built with a loop and cut to ten characters with substring.
* **Message Hash:** First two digits of the ID, the message number, and the first and last word of the message, in capitals (for example 00:0:HITONIGHT).
* **Validation:** Recipient numbers must contain the international code, and messages must not be longer than 250 characters.
* **Send, disregard or store:** Each message can be sent, disregarded or stored. Stored messages are saved in storedMessages.json.
* **Total:** The total number of messages sent is shown at the end.

## How to Run
Compile and run the main class `Chatapp.java` using NetBeans IDE (Run File), or through Maven in the terminal:
```bash
mvn clean install
java -cp target/classes com.chatapp.chatapp.Chatapp
```
Requirements: Java 26 and Maven.

## Example Session
1. Choose 1 (Register) and enter ktk_1, Ch&&sec@ke99!, +27838968976, then a first and last name.
2. Choose 2 (Login) with ktk_1 and Ch&&sec@ke99!.
3. Enter how many messages you want, choose 1 (Send Messages), then enter a recipient such as +27718693002 and a message.
4. Choose send, disregard or store for each message.

## Project Structure
```
src/main/java/com/chatapp/chatapp/
    Chatapp.java              console menu: register, login
    QuickChat.java            QuickChat menu and message entry
    auth/Login.java           registration and login checks
    message/Message.java      message ID, hash, validation, send/store, JSON
src/test/java/com/chatapp/chatapp/
    auth/LoginTest.java       14 unit tests
    message/MessageTest.java  20 unit tests
.github/workflows/TestJava.yml    runs the tests in GitHub Actions
```

## Testing
Run the 34 JUnit 5 tests with:
```bash
mvn test
```
The tests also run automatically in GitHub Actions on every push and pull request (TestJava.yml).

## Version Control
Part 2 was built on the branch feature/part2-messages with 6 commits and merged into main through pull request #1.

## References
Bray, T. (ed.) (2017) The JavaScript Object Notation (JSON) Data Interchange Format. RFC 8259. Available at: https://www.rfc-editor.org/rfc/rfc8259 (Accessed: 15 October 2026).

Oracle (n.d.) FileWriter (Java SE 9 and JDK 9). Available at: https://docs.oracle.com/javase/9/docs/api/java/io/FileWriter.html (Accessed: 15 October 2026).

validate.js (n.d.) South African mobile number regex. GitHub issue #235. Available at: https://github.com/ansman/validate.js/issues/235 (Accessed: 19 October 2026).