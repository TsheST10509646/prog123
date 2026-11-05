
package com.mycompany.poeloginandres;

/**
 * A snapshot of a Part 2 message. It lets Part 3 save/search messages without
 * changing the original message.java class or its constructors/getters.
 */
public final class MessageRecord {
    private final String messageID;
    private final String sender;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private final String status;

    public MessageRecord(String messageID, String sender, String recipient,
                         String messageText, String messageHash, String status) {
        if (messageID == null || !messageID.matches("[0-9]{10}")) {
            throw new IllegalArgumentException("A ten-digit message ID is required.");
        }
        if (messageText == null || messageHash == null || messageHash.isEmpty()
                || recipient == null || status == null) {
            throw new IllegalArgumentException("Message details are incomplete.");
        }
        if (!status.equals("Sent") && !status.equals("Stored")
                && !status.equals("Disregarded")) {
            throw new IllegalArgumentException("Unknown message status: " + status);
        }
        this.messageID = messageID;
        this.sender = sender == null || sender.trim().isEmpty() ? "Developer" : sender;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
        this.status = status;
    }

    /** Read values from the ORIGINAL Part 2 message object. */
    public static MessageRecord fromOriginal(message original, String sender) {
        if (original == null) {
            throw new IllegalArgumentException("A message is required.");
        }
        String hash = original.getMessageHash();
        if (hash == null || hash.isEmpty()) {
            hash = original.createMessageHash();
        }
        return new MessageRecord(original.getMessageID(), sender, original.getRecipient(),
                original.getMessageText(), hash, original.getStatus());
    }

    public String getMessageID() { return messageID; }
    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }
}
