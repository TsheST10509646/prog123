/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package testpackge;

import com.mycompany.poeloginandres.MessageManager;
import com.mycompany.poeloginandres.MessageRecord;
import com.mycompany.poeloginandres.MessageStorage;
import com.mycompany.poeloginandres.message;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

public class MessageManagerTest {

    private static final String LATE_ID = "1000000002";
    private static final String DINNER_ID = "0838884567";
    private static final String STORED_RECIPIENT = "+27838884567";
    private static final String CAKE_TEXT = "Did you get the cake?";
    private static final String LATE_TEXT =
            "Where are you? You are late! I have asked you to be on time.";
    private static final String GATE_TEXT = "Yohoooo, I am at your gate.";
    private static final String DINNER_TEXT = "It is dinner time!";
    private static final String LEAVING_TEXT = "Ok, I am leaving without you.";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private MessageStorage storage;
    private MessageManager manager;

    @Before
    public void setUp() throws IOException {
        storage = new MessageStorage(
                folder.getRoot().toPath().resolve("messages.json"));
        manager = new MessageManager(storage);

        register("1000000001", "+27834557896", CAKE_TEXT, "Sent");
        register(LATE_ID, STORED_RECIPIENT, LATE_TEXT, "Stored");
        register("1000000003", "+27834484567", GATE_TEXT, "Disregarded");
        register(DINNER_ID, "0838884567", DINNER_TEXT, "Sent");
    }

    @Test
    public void sentArrayContainsMessagesOneAndFour() {
        MessageRecord[] sent = manager.getSentMessagesArray();

        assertEquals(2, sent.length);
        assertEquals(CAKE_TEXT, sent[0].getMessageText());
        assertEquals(DINNER_TEXT, sent[1].getMessageText());
    }

    @Test
    public void messageTwoIsTheLongest() {
        assertEquals(LATE_TEXT, manager.findLongestMessage());
        assertEquals(LATE_TEXT, manager.findLongestStoredMessage().getMessageText());
    }

    @Test
    public void searchingByIdReturnsMessageFour() {
        assertEquals("Recipient: 0838884567\nMessage: It is dinner time!",
                manager.searchByMessageID(DINNER_ID));
    }

    @Test
    public void recipientSearchReturnsMessagesTwoAndFive() throws IOException {
        registerMessageFive();

        List<MessageRecord> matches = manager.findMessagesByRecipient(STORED_RECIPIENT);

        assertEquals(2, matches.size());
        assertEquals(LATE_TEXT, matches.get(0).getMessageText());
        assertEquals(LEAVING_TEXT, matches.get(1).getMessageText());
    }

    @Test
    public void deletingMessageTwoByHashUpdatesTheJsonFile() throws IOException {
        registerMessageFive();
        String hash = manager.findMessageByID(LATE_ID).getMessageHash();

        assertEquals("Message: \"" + LATE_TEXT + "\" successfully deleted.",
                manager.deleteByHashWithConfirmation(hash));
        assertNull(manager.findMessageByID(LATE_ID));

        List<MessageRecord> saved = storage.readStoredMessages();
        assertEquals(1, saved.size());
        assertEquals(LEAVING_TEXT, saved.get(0).getMessageText());
    }

    @Test
    public void reportShowsTheHashRecipientAndTextOfEachSentMessage() {
        String report = manager.generateMessageReport();
        int start = report.indexOf("=== SENT MESSAGES ===");
        int end = report.indexOf("=== STORED MESSAGES ===");
        assertTrue(start >= 0 && end > start);

        String expectedSentSection = """
                === SENT MESSAGES ===
                Message ID: 1000000001
                Message Hash: HASH-1000000001
                Sender: Developer
                Recipient: +27834557896
                Message: Did you get the cake?
                Status: Sent

                Message ID: 0838884567
                Message Hash: HASH-0838884567
                Sender: Developer
                Recipient: 0838884567
                Message: It is dinner time!
                Status: Sent
                """;

        assertEquals(expectedSentSection.strip(), report.substring(start, end).strip());
        assertTrue(report.endsWith("Total sent messages: 2"));
    }

    @Test
    public void storedArrayAndRecipientDisplayAreRestoredFromJson() throws IOException {
        registerMessageFive();

        MessageManager reloaded = new MessageManager(storage);
        reloaded.readJsonFileIntoArrayAndDisplay();

        MessageRecord[] stored = reloaded.getStoredMessagesArray();
        assertEquals(2, stored.length);
        assertEquals(LATE_TEXT, stored[0].getMessageText());
        assertEquals(LEAVING_TEXT, stored[1].getMessageText());

        String pair = "Sender: Developer\nRecipient: " + STORED_RECIPIENT + "\n\n";
        assertEquals("Stored-message senders and recipients:\n" + pair + pair,
                reloaded.displayStoredRecipients());
    }

    @Test
    public void disregardedArrayAndIdAndHashIndexesArePopulated() {
        MessageRecord[] disregarded = manager.getDisregardedMessagesArray();
        assertEquals(1, disregarded.length);
        assertEquals(GATE_TEXT, disregarded[0].getMessageText());

        assertArrayEquals(
                new String[]{"1000000001", DINNER_ID, "1000000003", LATE_ID},
                manager.getMessageIDs());
        assertArrayEquals(
                new String[]{"HASH-1000000001", "HASH-" + DINNER_ID,
                    "HASH-1000000003", "HASH-" + LATE_ID},
                manager.getMessageHashes());
    }

    private void registerMessageFive() throws IOException {
        register("1000000005", STORED_RECIPIENT, LEAVING_TEXT, "Stored");
    }

    // Fixed details represent a processed message and make each test repeatable.
    private void register(String id, String recipient, String text, String status)
            throws IOException {

        message original = new message(recipient, text) {
            @Override
            public String getMessageID() {
                return id;
            }

            @Override
            public String getMessageHash() {
                return "HASH-" + id;
            }

            @Override
            public String getStatus() {
                return status;
            }
        };

        manager.registerProcessedMessage(original, "Developer");
    }
}
