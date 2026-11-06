/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package testpackge;

import com.mycompany.poeloginandres.MessageRecord;
import com.mycompany.poeloginandres.message;
import org.junit.Test;

import static org.junit.Assert.*;

public class MessageRecordTest {

    private static final String ID = "1000000002";
    private static final String RECIPIENT = "+27838884567";
    private static final String TEXT =
            "Where are you? You are late! I have asked you to be on time.";
    private static final String HASH = "HASH-1000000002";

    @Test
    public void recordKeepsMessageTwoDetails() {
        MessageRecord record = new MessageRecord(
                ID, "Developer", RECIPIENT, TEXT, HASH, "Stored");

        assertEquals(ID, record.getMessageID());
        assertEquals("Developer", record.getSender());
        assertEquals(RECIPIENT, record.getRecipient());
        assertEquals(TEXT, record.getMessageText());
        assertEquals(HASH, record.getMessageHash());
        assertEquals("Stored", record.getStatus());
    }

    @Test
    public void blankSenderBecomesDeveloper() {
        MessageRecord record = new MessageRecord(
                ID, " ", RECIPIENT, TEXT, HASH, "Stored");

        assertEquals("Developer", record.getSender());
    }

    @Test
    public void recordRejectsInvalidIdAndPendingStatus() {
        assertThrows(IllegalArgumentException.class, () ->
                new MessageRecord(
                        "123", "Developer", RECIPIENT, TEXT, HASH, "Stored"));

        assertThrows(IllegalArgumentException.class, () ->
                new MessageRecord(
                        ID, "Developer", RECIPIENT, TEXT, HASH, "Pending"));
    }

    @Test
    public void recordCopiesAnOriginalStoredMessage() {
        message original = new message(RECIPIENT, TEXT);
        original.sentMessage(2); // Option 2 marks it as Stored.

        MessageRecord record = MessageRecord.fromOriginal(original, "Developer");

        assertEquals(original.getMessageID(), record.getMessageID());
        assertEquals(RECIPIENT, record.getRecipient());
        assertEquals(TEXT, record.getMessageText());
        assertEquals(original.getMessageHash(), record.getMessageHash());
        assertFalse(record.getMessageHash().isEmpty());
        assertEquals("Stored", record.getStatus());
    }
}