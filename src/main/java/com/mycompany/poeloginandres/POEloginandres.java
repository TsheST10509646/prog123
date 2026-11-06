/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poeloginandres;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.charset.StandardCharsets;

public class POEloginandres {

    // the arrays need for part 3

    private static MessageRecord[] sentMessages =
            new MessageRecord[0];

    private static MessageRecord[] disregardedMessages =
            new MessageRecord[0];

    private static MessageRecord[] storedMessages =
            new MessageRecord[0];

    private static String[] messageHashes =
            new String[0];

    private static String[] messageIDs =
            new String[0];

    private static final Path JSON_FILE =
            Path.of("stored_messages.json");


    // main method

    public static void main(String[] args) {

        Scanner myInput = new Scanner(System.in);

        String userName;
        String password;
        String cellPhoneNum;

        System.out.println("Enter your username");
        userName = myInput.nextLine();
// used when the impormation inputed isnt according to requirments
        while (!loginClassclass.checkUserName(userName)) {

            System.out.println("Username is not correctly formatted");
            System.out.println("Please ensure your username contains an underscore");
            System.out.println("It must be no more than five characters in length");

            System.out.println("Please enter your username");
            userName = myInput.nextLine();
        }

        System.out.println("Username successfully captured");

        System.out.println(
                "Please enter your cell phone number using "
                + "the South African international formatting starting with +27");

        cellPhoneNum = myInput.nextLine();

        while (!loginClassclass.checkCellPhoneNumber(cellPhoneNum)) {

            System.out.println("Your cell phone number format is incorrect");

            System.out.println("Please enter your cell phone number "
                    + "using the South African international formatting starting with +27");

            cellPhoneNum = myInput.nextLine();
        }

        System.out.println("Cell phone number successfully entered");

        System.out.println("Please enter your password");
        password = myInput.nextLine();
// this is message will be displaced when password is incorrect
        while (!loginClassclass.checkPasswordComplexity(password)) {

            System.out.println("Your password is incorrect");
            System.out.println(
                    "Your password must have at least 8 characters, "
                    + "a capital letter, a number and a special character");

            System.out.println("Please enter your password");
            password = myInput.nextLine();
        }
// message will display when password requirments are met
        System.out.println("Password entered successfully");

        String registrationMessage =
                loginClassclass.registerUser(
                        userName, password, cellPhoneNum);

        System.out.println(registrationMessage);

        System.out.println("\n=====LOGIN=====");

        boolean loginSuccessful = false;
//this method is to enter the details saved in the previous sign in and 
//allow you to enter the programme/application
        while (!loginSuccessful) {

            System.out.print("Enter username to login: ");
            String enteredUserName = myInput.nextLine();

            System.out.print("Enter password to login: ");
            String enteredPassword = myInput.nextLine();

            loginSuccessful = loginClassclass.loginUser(
                    enteredUserName,
                    enteredPassword,
                    userName,
                    password);

            System.out.println(
                    loginClassclass.returnLoginStatus(
                            loginSuccessful, userName));
        }

        // PART 3: LOAD SAVED MESSAGES

        try {
            loadStoredMessages();
            System.out.println("Stored messages loaded successfully.");
        } catch (IOException ex) {
            System.out.println(
                    "Could not load stored messages: " + ex.getMessage());
        }

        mainMenu(myInput, userName);
    }


    // PART 3: MAIN MENU

    private static void mainMenu(Scanner input, String userName) {

        int choice;

        do {
            System.out.println("\n===== QUICKCHAT MAIN MENU =====");

            System.out.println("1. Send Messages");
            System.out.println("2. Show Recently Sent Messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");

            choice = readChoice(
                    input, "Choose an option: ", 1, 4, 3);

            switch (choice) {

                case 1:
                    sendMessages(input, userName);
                    break;

                case 2:
                    displaySentMessages();
                    break;

                case 3:
                    System.out.println("Thank you for using QuickChat.");
                    break;

                case 4:
                    storedMessagesMenu(input);
                    break;

                default:
                    break;
            }

        } while (choice != 3);
    }


    // PART 2: SEND, STORE OR DISREGARD

    private static void sendMessages(
            Scanner input, String userName) {

        System.out.println("Welcome to QuickChat");

        int numberOfMessages = readChoice(
                input,
                "How many messages would you like to send? ",
                0, 1000, 0);

        int messagesSent = 0;

        while (messagesSent < numberOfMessages) {

            System.out.println(
                    "\nMESSAGE " + (messagesSent + 1)
                    + " OF " + numberOfMessages);

            System.out.print("Enter recipient cell number (+27...): ");
            String recipient = input.nextLine();

            System.out.print("Enter your message: ");
            String messageText = input.nextLine();

            // ORIGINAL PART 2 CONSTRUCTOR
            message newMessage =
                    new message(recipient, messageText);

            System.out.println(
                    "Message ID: " + newMessage.getMessageID());

            if (!newMessage.checkMessageID()) {
                System.out.println("Invalid message ID.");
                continue;
            }

            if (!newMessage.checkRecipientCell()) {
                System.out.println("Invalid recipient number.");
                continue;
            }

            if (!newMessage.checkMessageLength()
                    || messageText.trim().isEmpty()) {
                System.out.println(
                        "Message cannot be empty or exceed 250 characters.");
                continue;
            }

            System.out.println(
                    "Message Hash: " + newMessage.createMessageHash());

            System.out.println("\n1. Send Message");
            System.out.println("2. Store Message");
            System.out.println("3. Disregard Message");
            System.out.println("0. Return to Main Menu");

            int action = readChoice(
                    input, "Enter your choice: ", 0, 3, 0);

            if (action == 0) {
                return;
            }

            // ORIGINAL PART 2 METHOD
            String result = newMessage.sentMessage(action);

            System.out.println(result);

            try {
                // PART 3: POPULATE THE ARRAYS
                registerMessage(newMessage, userName);

                if (action == 1) {
                    messagesSent++;
                }

                if (action == 2) {
                    System.out.println(
                            "Message saved to JSON. Enter another message.");
                }

                if (action == 3) {
                    System.out.println(
                            "Message disregarded. Enter another message.");
                }

            } catch (IOException | IllegalArgumentException ex) {
                System.out.println(
                        "Part 3 could not record the message: "
                        + ex.getMessage());
            }
        }

        System.out.println(
                "You have sent all requested messages.");
    }


    // PART 3: CAPTURE A PROCESSED MESSAGE

    private static void registerMessage(
            message original, String sender) throws IOException {

        MessageRecord record = new MessageRecord(
                original.getMessageID(),
                sender,
                original.getRecipient(),
                original.getMessageText(),
                original.getMessageHash(),
                original.getStatus());

        if (findMessageByID(record.id) != null) {
            throw new IllegalArgumentException(
                    "This message ID is already recorded.");
        }

        switch (record.status) {

            case "Sent":
                sentMessages = append(sentMessages, record);
                break;

            case "Stored":
                MessageRecord[] updated =
                        append(storedMessages, record);

                saveStoredMessages(updated);
                storedMessages = updated;
                break;

            case "Disregarded":
                disregardedMessages =
                        append(disregardedMessages, record);
                break;

            default:
                throw new IllegalArgumentException(
                        "Invalid message status.");
        }

        updateIndexes();
    }


    // PART 3: STORED MESSAGES MENU

    private static void storedMessagesMenu(Scanner input) {

        int choice;

        do {
            System.out.println("\n===== STORED MESSAGES =====");

            System.out.println("1. Display Stored Senders and Recipients");
            System.out.println("2. Display Longest Stored Message");
            System.out.println("3. Search by Message ID");
            System.out.println("4. Search by Recipient");
            System.out.println("5. Delete Message Using Hash");
            System.out.println("6. Display Full Message Report");
            System.out.println("7. Reload JSON and Display");
            System.out.println("8. Back to Main Menu");

            choice = readChoice(
                    input, "Choose an option: ", 1, 8, 8);

            try {
                switch (choice) {

                    case 1:
                        displayStoredRecipients();
                        break;

                    case 2:
                        displayLongestStoredMessage();
                        break;

                    case 3:
                        System.out.print("Enter message ID: ");
                        searchByMessageID(input.nextLine());
                        break;

                    case 4:
                        System.out.print("Enter recipient: ");
                        searchByRecipient(input.nextLine());
                        break;

                    case 5:
                        System.out.print("Enter message hash: ");
                        deleteByHash(input.nextLine());
                        break;

                    case 6:
                        displayReport();
                        break;

                    case 7:
                        loadStoredMessages();
                        displayStoredMessages();
                        break;

                    case 8:
                        break;

                    default:
                        break;
                }

            } catch (IOException ex) {
                System.out.println(
                        "File error: " + ex.getMessage());
            }

        } while (choice != 8);
    }


    // PART 3: DISPLAY SENT MESSAGES

    private static void displaySentMessages() {

        if (sentMessages.length == 0) {
            System.out.println("No sent messages available.");
            return;
        }

        for (MessageRecord record : sentMessages) {
            System.out.println(formatMessage(record));
        }
    }


    // PART 3: DISPLAY STORED SENDERS AND RECIPIENTS

    private static void displayStoredRecipients() {

        if (storedMessages.length == 0) {
            System.out.println("No stored messages available.");
            return;
        }

        for (MessageRecord record : storedMessages) {
            System.out.println("Sender: " + record.sender);
            System.out.println("Recipient: " + record.recipient);
            System.out.println();
        }
    }


    // PART 3: DISPLAY LONGEST STORED MESSAGE

    private static void displayLongestStoredMessage() {

        if (storedMessages.length == 0) {
            System.out.println("No stored messages available.");
            return;
        }

        MessageRecord longest = storedMessages[0];

        for (MessageRecord record : storedMessages) {

            if (record.text.length() > longest.text.length()) {
                longest = record;
            }
        }

        System.out.println("Longest stored message:");
        System.out.println(formatMessage(longest));
    }


    // PART 3: SEARCH BY MESSAGE ID

    private static MessageRecord findMessageByID(String id) {

        if (id == null) {
            return null;
        }

        for (MessageRecord record : allMessages()) {

            if (record.id.equals(id)) {
                return record;
            }
        }

        return null;
    }

    private static void searchByMessageID(String id) {

        MessageRecord record = findMessageByID(id.trim());

        if (record == null) {
            System.out.println("Message ID not found.");
            return;
        }

        System.out.println("Recipient: " + record.recipient);
        System.out.println("Message: " + record.text);
    }


    // PART 3: SEARCH ALL SENT AND STORED MESSAGES BY RECIPIENT

    private static void searchByRecipient(String recipient) {

        recipient = recipient.trim();
        boolean found = false;

        for (MessageRecord record : sentMessages) {

            if (record.recipient.equals(recipient)) {

                System.out.println("Message: " + record.text);
                found = true;
            }
        }

        for (MessageRecord record : storedMessages) {

            if (record.recipient.equals(recipient)) {

                System.out.println("Message: " + record.text);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No messages found for this recipient.");
        }
    }


    // PART 3: DELETE BY MESSAGE HASH

    private static void deleteByHash(String hash)
            throws IOException {

        hash = hash.trim();

        MessageRecord found = null;

        for (MessageRecord record : allMessages()) {

            if (record.hash.equals(hash)) {
                found = record;
                break;
            }
        }

        if (found == null) {
            System.out.println("Message hash not found.");
            return;
        }

        String id = found.id;

        if (found.status.equals("Stored")) {

            MessageRecord[] updated =
                    removeByID(storedMessages, id);

            saveStoredMessages(updated);
            storedMessages = updated;

        } else if (found.status.equals("Sent")) {

            sentMessages = removeByID(sentMessages, id);

        } else {

            disregardedMessages =
                    removeByID(disregardedMessages, id);
        }

        updateIndexes();

        System.out.println(
                "Message: \"" + found.text
                + "\" successfully deleted.");
    }


    // PART 3: FULL MESSAGE REPORT

    private static void displayReport() {

        System.out.println("\n===== MESSAGE REPORT =====");

        displayGroup("SENT MESSAGES", sentMessages);
        displayGroup("STORED MESSAGES", storedMessages);
        displayGroup("DISREGARDED MESSAGES",
                disregardedMessages);

        System.out.println(
                "Total sent messages: " + sentMessages.length);
    }

    private static void displayGroup(
            String heading, MessageRecord[] records) {

        System.out.println("\n===== " + heading + " =====");

        if (records.length == 0) {
            System.out.println("None");
        }

        for (MessageRecord record : records) {
            System.out.println(formatMessage(record));
        }
    }

    private static void displayStoredMessages() {

        if (storedMessages.length == 0) {
            System.out.println("No stored messages available.");
            return;
        }

        for (MessageRecord record : storedMessages) {
            System.out.println(formatMessage(record));
        }
    }


    // PART 3: FORMAT THE REPORT

    private static String formatMessage(MessageRecord record) {

        return "\nMessage ID: " + record.id
                + "\nMessage Hash: " + record.hash
                + "\nSender: " + record.sender
                + "\nRecipient: " + record.recipient
                + "\nMessage: " + record.text
                + "\nStatus: " + record.status + "\n";
    }


    // PART 3: SAVE STORED MESSAGES AS JSON

    private static void saveStoredMessages(
            MessageRecord[] records) throws IOException {

        Path absolute = JSON_FILE.toAbsolutePath();
        Path directory = absolute.getParent();

        Files.createDirectories(directory);

        Path temp = Files.createTempFile(
                directory, "quickchat-", ".tmp");

        try {
            StringBuilder json = new StringBuilder("[\n");

            for (int i = 0; i < records.length; i++) {

                MessageRecord r = records[i];

                json.append("  {\n")
                    .append("    \"id\": ").append(quote(r.id)).append(",\n")
                    .append("    \"sender\": ").append(quote(r.sender)).append(",\n")
                    .append("    \"recipient\": ").append(quote(r.recipient)).append(",\n")
                    .append("    \"text\": ").append(quote(r.text)).append(",\n")
                    .append("    \"hash\": ").append(quote(r.hash)).append(",\n")
                    .append("    \"status\": \"Stored\"\n")
                    .append("  }");

                if (i < records.length - 1) {
                    json.append(",");
                }

                json.append("\n");
            }

            json.append("]");

            Files.writeString(
                    temp, json.toString(), StandardCharsets.UTF_8);

            try {
                Files.move(temp, absolute,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);

            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temp, absolute,
                        StandardCopyOption.REPLACE_EXISTING);
            }

        } finally {
            Files.deleteIfExists(temp);
        }
    }


    // PART 3: LOAD STORED MESSAGES FROM JSON

    private static void loadStoredMessages() throws IOException {

        if (!Files.exists(JSON_FILE)
                || Files.size(JSON_FILE) == 0) {

            storedMessages = new MessageRecord[0];
            updateIndexes();
            return;
        }

        String json = Files.readString(
                JSON_FILE, StandardCharsets.UTF_8);

        List<Map<String, String>> records =
                new JsonReader(json).readArray();

        List<MessageRecord> loaded = new ArrayList<>();

        for (Map<String, String> data : records) {

            if (!"Stored".equals(data.get("status"))) {
                throw new IOException("Invalid stored message status.");
            }

            try {
                loaded.add(new MessageRecord(
                        data.get("id"),
                        data.get("sender"),
                        data.get("recipient"),
                        data.get("text"),
                        data.get("hash"),
                        data.get("status")));

            } catch (IllegalArgumentException ex) {
                throw new IOException(
                        "Invalid message in JSON: " + ex.getMessage(), ex);
            }
        }

        storedMessages = loaded.toArray(new MessageRecord[0]);
        updateIndexes();
    }


    // PART 3: REBUILD THE MESSAGE HASH AND ID ARRAYS

    private static void updateIndexes() {

        List<MessageRecord> all = allMessages();

        messageIDs = new String[all.size()];
        messageHashes = new String[all.size()];

        for (int i = 0; i < all.size(); i++) {

            messageIDs[i] = all.get(i).id;
            messageHashes[i] = all.get(i).hash;
        }
    }


    private static List<MessageRecord> allMessages() {

        List<MessageRecord> all = new ArrayList<>();

        all.addAll(Arrays.asList(sentMessages));
        all.addAll(Arrays.asList(disregardedMessages));
        all.addAll(Arrays.asList(storedMessages));

        return all;
    }


    // PART 3: ARRAY HELPER METHODS

    private static <T> T[] append(T[] array, T value) {

        T[] result = Arrays.copyOf(
                array, array.length + 1);

        result[array.length] = value;

        return result;
    }

    private static MessageRecord[] removeByID(
            MessageRecord[] records, String id) {

        List<MessageRecord> remaining = new ArrayList<>();

        for (MessageRecord record : records) {

            if (!record.id.equals(id)) {
                remaining.add(record);
            }
        }

        return remaining.toArray(new MessageRecord[0]);
    }


    // PART 3: MESSAGE RECORD HELD INSIDE THE MAIN CLASS

    private static class MessageRecord {

        String id;
        String sender;
        String recipient;
        String text;
        String hash;
        String status;

        MessageRecord(String id, String sender,
                String recipient, String text,
                String hash, String status) {

            if (id == null || !id.matches("[0-9]{10}")
                    || recipient == null || text == null
                    || hash == null || hash.isEmpty()
                    || status == null) {

                throw new IllegalArgumentException(
                        "Incomplete message details.");
            }

            if (!status.equals("Sent")
                    && !status.equals("Stored")
                    && !status.equals("Disregarded")) {

                throw new IllegalArgumentException(
                        "Invalid message status.");
            }

            this.id = id;
            this.sender = sender == null || sender.isBlank()
                    ? "Developer" : sender;
            this.recipient = recipient;
            this.text = text;
            this.hash = hash;
            this.status = status;
        }
    }


    // PART 3: JSON STRING ESCAPING

    private static String quote(String value) {

        StringBuilder result = new StringBuilder("\"");

        for (int i = 0; i < value.length(); i++) {

            char c = value.charAt(i);

            switch (c) {
                case '"': result.append("\\\""); break;
                case '\\': result.append("\\\\"); break;
                case '\n': result.append("\\n"); break;
                case '\r': result.append("\\r"); break;
                case '\t': result.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        result.append(String.format("\\u%04x", (int)c));
                    } else {
                        result.append(c);
                    }
            }
        }

        return result.append('"').toString();
    }


    // PART 3: JSON READER INSIDE THE MAIN CLASS

    private static class JsonReader {

        private final String source;
        private int pos = 0;

        JsonReader(String source) {
            this.source = source;
        }

        List<Map<String, String>> readArray() throws IOException {

            List<Map<String, String>> records = new ArrayList<>();

            require('[');

            if (!take(']')) {
                do {
                    records.add(readObject());
                } while (take(','));

                require(']');
            }

            whitespace();

            if (pos != source.length()) {
                throw error("Unexpected data after JSON array");
            }

            return records;
        }

        private Map<String, String> readObject() throws IOException {

            Map<String, String> record = new HashMap<>();

            require('{');

            if (!take('}')) {
                do {
                    String key = readString();

                    require(':');

                    String value = readValue();

                    if (record.containsKey(key)) {
                        throw error("Duplicate JSON field");
                    }

                    record.put(key, value);

                } while (take(','));

                require('}');
            }

            return record;
        }

        private String readValue() throws IOException {

            whitespace();

            if (pos < source.length()
                    && source.charAt(pos) == '"') {

                return readString();
            }

            if (source.startsWith("null", pos)) {
                pos += 4;
                return null;
            }

            throw error("Expected a JSON string or null");
        }

        private String readString() throws IOException {

            require('"');

            StringBuilder result = new StringBuilder();

            while (pos < source.length()) {

                char c = source.charAt(pos++);

                if (c == '"') {
                    return result.toString();
                }

                if (c == '\\') {

                    if (pos >= source.length()) {
                        throw error("Incomplete escape");
                    }

                    c = source.charAt(pos++);

                    switch (c) {
                        case '"': result.append('"'); break;
                        case '\\': result.append('\\'); break;
                        case '/': result.append('/'); break;
                        case 'n': result.append('\n'); break;
                        case 'r': result.append('\r'); break;
                        case 't': result.append('\t'); break;
                        case 'b': result.append('\b'); break;
                        case 'f': result.append('\f'); break;
                        case 'u':
                            if (pos + 4 > source.length()) {
                                throw error("Invalid Unicode escape");
                            }

                            try {
                                result.append((char) Integer.parseInt(
                                        source.substring(pos, pos + 4), 16));

                            } catch (NumberFormatException ex) {
                                throw error("Invalid Unicode escape");
                            }

                            pos += 4;
                            break;

                        default:
                            throw error("Invalid JSON escape");
                    }

                } else {

                    if (c < 0x20) {
                        throw error("Invalid control character");
                    }

                    result.append(c);
                }
            }

            throw error("Unterminated JSON string");
        }

        private void whitespace() {

            while (pos < source.length()
                    && " \r\n\t".indexOf(source.charAt(pos)) >= 0) {

                pos++;
            }
        }

        private boolean take(char expected) {

            whitespace();

            if (pos < source.length()
                    && source.charAt(pos) == expected) {

                pos++;
                return true;
            }

            return false;
        }

        private void require(char expected) throws IOException {

            if (!take(expected)) {
                throw error("Expected " + expected);
            }
        }

        private IOException error(String message) {

            return new IOException(
                    "Invalid JSON at character "
                    + pos + ": " + message);
        }
    }


    // GENERAL MENU INPUT VALIDATION

    private static int readChoice(Scanner input,
            String prompt, int minimum, int maximum,
            int exitOnEndOfInput) {

        while (true) {

            System.out.print(prompt);

            if (!input.hasNextLine()) {
                return exitOnEndOfInput;
            }

            try {
                int choice = Integer.parseInt(
                        input.nextLine().trim());

                if (choice >= minimum && choice <= maximum) {
                    return choice;
                }

            } catch (NumberFormatException ex) {
                // Ask the user again.
            }

            System.out.println(
                    "Please enter a number between "
                    + minimum + " and " + maximum + ".");
        }
    }
}
