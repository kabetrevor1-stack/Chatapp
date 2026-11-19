# QuickChat (Chatapp)

A console chat application written in Java (Maven, JUnit 5) for PROG5121 Programming 1A.

## Features
- **Part 1:** register (username, password, South African cell number) and log in.
- **Part 2:** after login, send, store or disregard messages. Each message gets a 10-digit ID and a hash. Stored messages are saved to `storedMessages.json`.
- **Part 3:** arrays for sent, disregarded and stored messages, hashes and IDs. The JSON file is read back in at start-up. A "Stored Messages" menu can:
  1. show the sender and recipient of all stored messages
  2. show the longest stored message
  3. search for a message ID
  4. search for all messages sent or stored for a recipient
  5. delete a message using its hash
  6. show a report of all stored messages

## How to run
1. Open the project in NetBeans and press **F6**, or run `mvn exec:java`.
2. Register, log in, then use the QuickChat menu.

## How to test
Run `mvn test` or use **Test Project** in NetBeans. There are 52 unit tests (LoginTest 14, MessageTest 22, StoredMessagesTest 16). GitHub Actions (`TestJava.yml`) runs them on every push.

## References
Bray, T. (ed.) (2017) *The JavaScript Object Notation (JSON) Data Interchange Format*. RFC 8259. Available at: https://www.rfc-editor.org/rfc/rfc8259 (Accessed: 15 October 2026).

Google (2024) *Gson* (Version 2.11.0) [Java library]. Available at: https://github.com/google/gson (Accessed:20 October 2026).

Google (n.d.) *Gson User Guide*. Available at: https://github.com/google/gson/blob/main/UserGuide.md (Accessed:20 October 2026).

Oracle (n.d.) *FileWriter (Java SE 9 & JDK 9)*. Available at: https://docs.oracle.com/javase/9/docs/api/java/io/FileWriter.html (Accessed: 15 October 2026).

Checks that the cell phone number is in South African international format,
e.g. +27838968976
Regex reference: validate.js (n.d.) South African mobile number regex.
GitHub issue #235. Available at: https://github.com/ansman/validate.js/issues/235
(Accessed: 10 October 2026).