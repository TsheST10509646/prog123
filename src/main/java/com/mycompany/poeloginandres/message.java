/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poeloginandres;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 *
 * @author tshep
 */
public class message {
      private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status;

    private static int totalMessages = 0;

    private static final List<message> sentMessages =
            new ArrayList<>();

    public message(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.status = "Pending";
    }

    private String generateMessageID() {
        Random random = new Random();

        long id = 1000000000L
                + random.nextInt(900000000);

        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID != null
                && messageID.length() <= 10;
    }

    public boolean checkRecipientCell() {
        /*
         * This example accepts South African numbers
         * in international format, such as +27821234567.
         * Adjust this pattern if your rubric specifies
         * a different format.
         */
        return recipient != null
                && recipient.matches("\\+27[0-9]{9}");
    }

    public boolean checkMessageLength() {
        return messageText != null
                && messageText.length() <= 250;
    }

    public String createMessageHash() {
        if (messageText == null
                || messageText.trim().isEmpty()) {
            messageHash = "";
            return messageHash;
        }

        String[] words =
                messageText.trim().split("\\s+");

        String firstTwoDigits =
                messageID.substring(0, 2);

        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        messageHash = firstTwoDigits
                + ":" + totalMessages
                + ":" + firstWord.toUpperCase()
                + lastWord.toUpperCase();

        return messageHash;
    }

    public String sentMessage(int option) {
        if (option == 1) {
            status = "Sent";
            sentMessages.add(this);
            totalMessages++;

            // Re-create the hash using the updated total.
            createMessageHash();

            return "Message successfully sent.";
        } else if (option == 2) {
            status = "Stored";
            return "Message stored.";
        } else if (option == 3) {
            status = "Disregarded";
            return "Message disregarded.";
        }

        return "Invalid message option.";
    }

    public String printMessages() {
        StringBuilder output = new StringBuilder();

        for (message current : sentMessages) {
            output.append("Message ID: ")
                    .append(current.getMessageID())
                    .append("\nRecipient: ")
                    .append(current.getRecipient())
                    .append("\nMessage: ")
                    .append(current.getMessageText())
                    .append("\nMessage Hash: ")
                    .append(current.getMessageHash())
                    .append("\nStatus: ")
                    .append(current.getStatus())
                    .append("\n\n");
        }

        if (output.length() == 0) {
            return "No sent messages available.";
        }

        return output.toString();
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getStatus() {
        return status;
    }

    public static List<message> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }
}


