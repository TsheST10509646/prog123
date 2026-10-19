/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package testpackge;

import com.mycompany.poeloginandres.message;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author tshep
 */

public class MessageTest {

    private message msg;

    public MessageTest() {
    }

    @Before
    public void setUp() {
        msg = new message("+27821234567", "Hello World");
    }

    // Test 1: Check Message ID
    @Test
    public void testMessageID() {
        assertNotNull(msg.getMessageID());
        assertEquals(10, msg.getMessageID().length());
        assertTrue(msg.checkMessageID());
    }

    // Test 2: Valid recipient number
    @Test
    public void testValidRecipientCell() {
        assertTrue(msg.checkRecipientCell());
    }

    // Test 3: Invalid recipient number
    @Test
    public void testInvalidRecipientCell() {
        message invalidMsg =
                new message("0821234567", "Hello World");

        assertFalse(invalidMsg.checkRecipientCell());
    }

    // Test 4: Valid message length
    @Test
    public void testValidMessageLength() {
        assertTrue(msg.checkMessageLength());
    }

    // Test 5: Message exceeds 250 characters
    @Test
    public void testMessageTooLong() {
        String longText = "A".repeat(251);

        message longMsg =
                new message("+27821234567", longText);

        assertFalse(longMsg.checkMessageLength());
    }

    // Test 6: Create message hash
    @Test
    public void testCreateMessageHash() {
        String hash = msg.createMessageHash();

        assertNotNull(hash);
        assertFalse(hash.isEmpty());
        assertEquals(hash, msg.getMessageHash());
    }

    // Test 7: Initial message status
    @Test
    public void testInitialStatus() {
        assertEquals("Pending", msg.getStatus());
    }

    // Test 8: Send message
    @Test
    public void testSendMessage() {
        int oldTotal = message.returnTotalMessages();

        String result = msg.sentMessage(1);

        assertEquals("Message successfully sent.", result);
        assertEquals("Sent", msg.getStatus());
        assertEquals(oldTotal + 1,
                message.returnTotalMessages());
        assertTrue(message.getSentMessages().contains(msg));
    }

    // Test 9: Store message
    @Test
    public void testStoreMessage() {
        int oldTotal = message.returnTotalMessages();

        String result = msg.sentMessage(2);

        assertEquals("Message stored.", result);
        assertEquals("Stored", msg.getStatus());
        assertEquals(oldTotal,
                message.returnTotalMessages());
    }

    // Test 10: Disregard message
    @Test
    public void testDisregardMessage() {
        int oldTotal = message.returnTotalMessages();

        String result = msg.sentMessage(3);

        assertEquals("Message disregarded.", result);
        assertEquals("Disregarded", msg.getStatus());
        assertEquals(oldTotal,
                message.returnTotalMessages());
    }

    // Test 11: Invalid message option
    @Test
    public void testInvalidMessageOption() {
        String result = msg.sentMessage(4);

        assertEquals("Invalid message option.", result);
        assertEquals("Pending", msg.getStatus());
    }

    // Test 12: Test message information
    @Test
    public void testGetters() {
        assertEquals("+27821234567", msg.getRecipient());
        assertEquals("Hello World", msg.getMessageText());
        assertNotNull(msg.getMessageID());
        assertEquals("Pending", msg.getStatus());
    }

    // Test 13: Print sent messages
    @Test
    public void testPrintMessages() {
        msg.sentMessage(1);

        String output = msg.printMessages();

        assertNotNull(output);
        assertTrue(output.contains("Message ID:"));
        assertTrue(output.contains("Recipient:"));
        assertTrue(output.contains("Message:"));
        assertTrue(output.contains("Hello World"));
        assertTrue(output.contains("Status: Sent"));
    }

    // Test 14: Check sent messages list copy
    @Test
    public void testGetSentMessagesReturnsCopy() {
        msg.sentMessage(1);

        int originalSize =
                message.getSentMessages().size();

        message.getSentMessages().clear();

        assertEquals(originalSize,
                message.getSentMessages().size());
    }
}
