/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package testpackge;

import com.mycompany.poeloginandres.message;
import org.junit.jupiter.api.BeforeEach; 
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tshep
 */
public class messageTest {

    private message msg;

    public messageTest() {
    }

    @BeforeEach // Fixed: JUnit 5 setup annotation
    public void setUp() {
        // Reset internal static states before each test if necessary
        message.getSentMessages(); 
        msg = new message("+27821234567", "Hello World");
    }

    @Test
    public void testMessageID() {
        assertNotNull(msg.getMessageID());
        assertEquals(10, msg.getMessageID().length());
        assertTrue(msg.checkMessageID());
    }

    @Test
    public void testValidRecipientCell() {
        assertTrue(msg.checkRecipientCell());
    }

    @Test
    public void testInvalidRecipientCell() {
        message invalidMsg = new message("0821234567", "Hello World");
        assertFalse(invalidMsg.checkRecipientCell());
    }

    @Test
    public void testValidMessageLength() {
        assertTrue(msg.checkMessageLength());
    }

    @Test
    public void testMessageTooLong() {
        String longText = "A".repeat(251);
        message longMsg = new message("+27821234567", longText);
        assertFalse(longMsg.checkMessageLength());
    }

    @Test
    public void testCreateMessageHash() {
        String hash = msg.createMessageHash();
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
        assertEquals(hash, msg.getMessageHash());
    }

    @Test
    public void testInitialStatus() {
        assertEquals("Pending", msg.getStatus());
    }

    @Test
    public void testSendMessage() {
        int oldTotal = message.returnTotalMessages();
        String result = msg.sentMessage(1);

        assertEquals("Message successfully sent.", result);
        assertEquals("Sent", msg.getStatus());
        assertEquals(oldTotal + 1, message.returnTotalMessages());
        assertTrue(message.getSentMessages().contains(msg));
    }

    @Test
    public void testStoreMessage() {
        int oldTotal = message.returnTotalMessages();
        String result = msg.sentMessage(2);

        assertEquals("Message stored.", result);
        assertEquals("Stored", msg.getStatus());
        assertEquals(oldTotal, message.returnTotalMessages());
    }

    @Test
    public void testDisregardMessage() {
        int oldTotal = message.returnTotalMessages();
        String result = msg.sentMessage(3);

        assertEquals("Message disregarded.", result);
        assertEquals("Disregarded", msg.getStatus());
        assertEquals(oldTotal, message.returnTotalMessages());
    }

    @Test
    public void testInvalidMessageOption() {
        String result = msg.sentMessage(4);

        assertEquals("Invalid message option.", result);
        assertEquals("Pending", msg.getStatus());
    }

    @Test
    public void testGetters() {
        assertEquals("+27821234567", msg.getRecipient());
        assertEquals("Hello World", msg.getMessageText());
        assertNotNull(msg.getMessageID());
        assertEquals("Pending", msg.getStatus());
    }

    @Test
    public void testPrintMessages() {
        msg.sentMessage(1);
        String output = msg.printMessages();

        assertTrue(output.contains("Message ID:"));
        assertTrue(output.contains("Recipient:"));
        assertTrue(output.contains("Message:"));
        assertTrue(output.contains("Hello World"));
        assertTrue(output.contains("Status: Sent"));
    }

    @Test
    public void testGetSentMessagesReturnsCopy() {
        msg.sentMessage(1);
        int originalSize = message.getSentMessages().size();

        // Testing defensive copying behavior
        message.getSentMessages().clear();
        assertEquals(originalSize, message.getSentMessages().size());
    }
}
