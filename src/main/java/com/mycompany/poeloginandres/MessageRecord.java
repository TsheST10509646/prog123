/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poeloginandres;

/** The saved details of one processed Part 2 message. */
public final class MessageRecord {
    static final String SENT = "Sent";
    static final String STORED = "Stored";
    static final String DISREGARDED = "Disregarded";

    private static final String DEFAULT_SENDER = "Developer";
    private static final String ID_PATTERN = "[0-9]{10}";

    private final String messageID;
    private final String sender;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private final String status;

    public MessageRecord(String messageID, String sender, String recipient,
                         String messageText, String messageHash, String status) {
        if (messageID == null || !messageID.matches(ID_PATTERN)) {
            throw new IllegalArgumentException("A ten-digit message ID is required.");
        }
        if (messageText == null || messageHash == null || messageHash.isEmpty()
                || recipient == null || status == null) {
            throw new IllegalArgumentException("Message details are incomplete.");
        }
        if (!SENT.equals(status) && !STORED.equals(status)
                && !DISREGARDED.equals(status)) {
            throw new IllegalArgumentException("Unknown message status: " + status);
        }

        this.messageID = messageID;
        this.sender = sender == null || sender.trim().isEmpty()
                ? DEFAULT_SENDER : sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
        this.status = status;
    }

    /** Copy details after the Part 2 message has been processed. */
    public static MessageRecord fromOriginal(message original, String sender) {
        if (original == null) {
            throw new IllegalArgumentException("A message is required.");
        }

        String hash = original.getMessageHash();
        if (hash == null || hash.isEmpty()) {
            hash = original.createMessageHash();
        }

        return new MessageRecord(
                original.getMessageID(),
                sender,
                original.getRecipient(),
                original.getMessageText(),
                hash,
                original.getStatus());
    }

    public String getMessageID() { return messageID; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }
}