/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poeloginandres;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Manages records created from Part 2 messages and loaded from JSON. */
public class MessageManager {
    // The arrays fill from processed messages or saved JSON at runtime.
    private MessageRecord[] sentMessages = new MessageRecord[0];
    private MessageRecord[] disregardedMessages = new MessageRecord[0];
    private MessageRecord[] storedMessages = new MessageRecord[0];
    private String[] messageHashes = new String[0];
    private String[] messageIDs = new String[0];

    private final MessageStorage storage;

    public MessageManager() {
        this(MessageStorage.defaultStorage());
    }

    public MessageManager(MessageStorage storage) {
        if (storage == null) {
            throw new IllegalArgumentException("Message storage is required.");
        }
        this.storage = storage;
    }

    /**
     * Register a message after Part 2 has processed the user's action.
     * Pass the logged-in username as sender.
     */
    public void registerProcessedMessage(message original, String sender)
            throws IOException {
        MessageRecord record = MessageRecord.fromOriginal(original, sender);

        if (findMessageByID(record.getMessageID()) != null) {
            throw new IllegalArgumentException("This message was already recorded.");
        }

        switch (record.getStatus()) {
            case MessageRecord.SENT:
                sentMessages = append(sentMessages, record);
                break;
            case MessageRecord.DISREGARDED:
                disregardedMessages = append(disregardedMessages, record);
                break;
            case MessageRecord.STORED:
                MessageRecord[] next = append(storedMessages, record);
                storage.saveStoredMessages(Arrays.asList(next));
                storedMessages = next;
                break;
            default:
                throw new IllegalArgumentException(
                        "Message must be sent, stored or disregarded first.");
        }

        rebuildIndexes();
    }

    public String readJsonFileIntoArrayAndDisplay() throws IOException {
        List<MessageRecord> records = storage.readStoredMessages();
        storedMessages = records.toArray(new MessageRecord[0]);
        rebuildIndexes();

        if (storedMessages.length == 0) {
            return "No stored messages available in JSON.";
        }

        StringBuilder output = new StringBuilder("Stored messages loaded from JSON:\n");
        for (MessageRecord record : storedMessages) {
            output.append(format(record)).append("\n\n");
        }
        return output.toString();
    }

    public String displaySentMessages() {
        if (sentMessages.length == 0) {
            return "No sent messages available.";
        }

        StringBuilder output = new StringBuilder("Sent messages:\n");
        for (MessageRecord record : sentMessages) {
            output.append(format(record)).append("\n\n");
        }
        return output.toString();
    }

    public String displayStoredRecipients() {
        if (storedMessages.length == 0) {
            return "No stored messages available.";
        }

        StringBuilder output = new StringBuilder("Stored-message senders and recipients:\n");
        for (MessageRecord record : storedMessages) {
            output.append("Sender: ").append(record.getSender())
                  .append("\nRecipient: ").append(record.getRecipient()).append("\n\n");
        }
        return output.toString();
    }

    public MessageRecord findLongestStoredMessage() {
        return longest(Arrays.asList(storedMessages));
    }

    public String findLongestMessage() {
        List<MessageRecord> eligible = new ArrayList<>(Arrays.asList(sentMessages));
        eligible.addAll(Arrays.asList(storedMessages));

        MessageRecord found = longest(eligible);
        return found == null ? "" : found.getMessageText();
    }

    private static MessageRecord longest(List<MessageRecord> eligible) {
        MessageRecord found = null;

        for (MessageRecord record : eligible) {
            if (found == null
                    || record.getMessageText().length() > found.getMessageText().length()) {
                found = record;
            }
        }
        return found;
    }

    public String displayLongestStoredMessage() {
        MessageRecord found = findLongestStoredMessage();
        return found == null ? "No stored messages available." : format(found);
    }

    public MessageRecord findMessageByID(String id) {
        if (id == null) {
            return null;
        }

        for (MessageRecord record : all()) {
            if (id.equals(record.getMessageID())) {
                return record;
            }
        }
        return null;
    }

    public String searchByMessageID(String id) {
        MessageRecord found = findMessageByID(id);

        return found == null ? "No message found for ID: " + id
                : "Recipient: " + found.getRecipient()
                  + "\nMessage: " + found.getMessageText();
    }

    public List<MessageRecord> findMessagesByRecipient(String recipient) {
        List<MessageRecord> matches = new ArrayList<>();
        if (recipient == null) {
            return matches;
        }

        for (MessageRecord record : sentMessages) {
            if (recipient.equals(record.getRecipient())) {
                matches.add(record);
            }
        }

        for (MessageRecord record : storedMessages) {
            if (recipient.equals(record.getRecipient())) {
                matches.add(record);
            }
        }

        return matches;
    }

    public String searchByRecipient(String recipient) {
        List<MessageRecord> matches = findMessagesByRecipient(recipient);
        if (matches.isEmpty()) {
            return "No messages found for recipient: " + recipient;
        }

        StringBuilder output = new StringBuilder();
        for (MessageRecord record : matches) {
            output.append("Message: ").append(record.getMessageText())
                  .append("\nRecipient: ").append(record.getRecipient()).append("\n\n");
        }
        return output.toString();
    }

    public boolean deleteByHash(String hash) throws IOException {
        MessageRecord target = null;
        for (MessageRecord record : all()) {
            if (hash != null && hash.equals(record.getMessageHash())) {
                target = record;
                break;
            }
        }

        if (target == null) {
            return false;
        }

        String deleteID = target.getMessageID();

        if (MessageRecord.STORED.equals(target.getStatus())) {
            MessageRecord[] next = withoutID(storedMessages, deleteID);
            storage.saveStoredMessages(Arrays.asList(next));
            storedMessages = next;
        } else if (MessageRecord.SENT.equals(target.getStatus())) {
            sentMessages = withoutID(sentMessages, deleteID);
        } else {
            disregardedMessages = withoutID(disregardedMessages, deleteID);
        }

        rebuildIndexes();
        return true;
    }

    public String deleteByHashWithConfirmation(String hash) throws IOException {
        MessageRecord target = null;

        for (MessageRecord record : all()) {
            if (hash != null && hash.equals(record.getMessageHash())) {
                target = record;
                break;
            }
        }

        if (target == null) {
            return "No message found with that hash.";
        }

        return deleteByHash(hash)
                ? "Message: \"" + target.getMessageText() + "\" successfully deleted."
                : "No message found with that hash.";
    }

    public String generateMessageReport() {
        StringBuilder output = new StringBuilder("QUICKCHAT MESSAGE REPORT\n");

        reportGroup(output, "SENT MESSAGES", sentMessages);
        reportGroup(output, "STORED MESSAGES", storedMessages);
        reportGroup(output, "DISREGARDED MESSAGES", disregardedMessages);
        output.append("Total sent messages: ").append(sentMessages.length);

        return output.toString();
    }

    private static void reportGroup(StringBuilder output, String title,
                                    MessageRecord[] group) {
        output.append("\n=== ").append(title).append(" ===\n");

        if (group.length == 0) {
            output.append("None\n");
        }
        for (MessageRecord record : group) {
            output.append(format(record)).append("\n\n");
        }
    }

    private static String format(MessageRecord record) {
        return "Message ID: " + record.getMessageID()
                + "\nMessage Hash: " + record.getMessageHash()
                + "\nSender: " + record.getSender()
                + "\nRecipient: " + record.getRecipient()
                + "\nMessage: " + record.getMessageText()
                + "\nStatus: " + record.getStatus();
    }

    private List<MessageRecord> all() {
        List<MessageRecord> records = new ArrayList<>();
        records.addAll(Arrays.asList(sentMessages));
        records.addAll(Arrays.asList(disregardedMessages));
        records.addAll(Arrays.asList(storedMessages));
        return records;
    }

    private void rebuildIndexes() {
        List<MessageRecord> records = all();
        messageIDs = new String[records.size()];
        messageHashes = new String[records.size()];

        for (int i = 0; i < records.size(); i++) {
            MessageRecord record = records.get(i);
            messageIDs[i] = record.getMessageID();
            messageHashes[i] = record.getMessageHash();
        }
    }

    private static MessageRecord[] withoutID(MessageRecord[] records, String id) {
        List<MessageRecord> kept = new ArrayList<>();

        for (MessageRecord record : records) {
            if (!record.getMessageID().equals(id)) {
                kept.add(record);
            }
        }

        return kept.toArray(new MessageRecord[0]);
    }

    private static <T> T[] append(T[] array, T next) {
        T[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = next;
        return result;
    }

    public MessageRecord[] getSentMessagesArray() {
        return sentMessages.clone();
    }

    public MessageRecord[] getDisregardedMessagesArray() {
        return disregardedMessages.clone();
    }

    public MessageRecord[] getStoredMessagesArray() {
        return storedMessages.clone();
    }

    public String[] getMessageHashes() {
        return messageHashes.clone();
    }

    public String[] getMessageIDs() {
        return messageIDs.clone();
    }
}