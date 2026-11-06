/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package testpackge;

import com.mycompany.poeloginandres.MessageRecord;
import com.mycompany.poeloginandres.MessageStorage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

public class MessageStorageTest {

    private static final String RECIPIENT = "+27838884567";
    private static final String LATE_TEXT =
            "Where are you? You are late! I have asked you to be on time.";
    private static final String LEAVING_TEXT = "Ok, I am leaving without you.";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Path file;
    private MessageStorage storage;

    @Before
    public void setUp() {
        file = folder.getRoot().toPath().resolve("messages.json");
        storage = new MessageStorage(file);
    }

    @Test
    public void savesAndLoadsBothStoredMessages() throws IOException {
        MessageRecord messageTwo = stored("1000000002", LATE_TEXT);
        MessageRecord messageFive = stored("1000000005", LEAVING_TEXT);

        storage.saveStoredMessages(List.of(messageTwo, messageFive));

        List<MessageRecord> loaded = new MessageStorage(file).readStoredMessages();

        assertEquals(2, loaded.size());
        assertSameDetails(messageTwo, loaded.get(0));
        assertSameDetails(messageFive, loaded.get(1));
    }

    @Test
    public void missingFileReturnsAnEmptyList() throws IOException {
        assertEquals(0, storage.readStoredMessages().size());
    }

    @Test
    public void malformedJsonThrowsIOException() throws IOException {
        Files.writeString(file, "[{");

        assertThrows(IOException.class, storage::readStoredMessages);
    }

    @Test
    public void rejectingASentMessageKeepsTheSavedRecords() throws IOException {
        MessageRecord messageTwo = stored("1000000002", LATE_TEXT);
        storage.saveStoredMessages(List.of(messageTwo));

        MessageRecord sent = new MessageRecord(
                "1000000001", "Developer", "+27834557896",
                "Did you get the cake?", "HASH-1000000001", "Sent");

        assertThrows(IllegalArgumentException.class, () ->
                storage.saveStoredMessages(List.of(sent)));

        List<MessageRecord> loaded = storage.readStoredMessages();
        assertEquals(1, loaded.size());
        assertSameDetails(messageTwo, loaded.get(0));
    }

    @Test
    public void quotesAndNewlinesSurviveSavingAndLoading() throws IOException {
        String text = "She said \"Hello\".\nSee you soon.";
        storage.saveStoredMessages(List.of(stored("1000000006", text)));

        assertEquals(text, storage.readStoredMessages().get(0).getMessageText());
    }

    private MessageRecord stored(String id, String text) {
        return new MessageRecord(
                id, "Developer", RECIPIENT, text, "HASH-" + id, "Stored");
    }

    private void assertSameDetails(MessageRecord expected, MessageRecord actual) {
        assertEquals(expected.getMessageID(), actual.getMessageID());
        assertEquals(expected.getSender(), actual.getSender());
        assertEquals(expected.getRecipient(), actual.getRecipient());
        assertEquals(expected.getMessageText(), actual.getMessageText());
        assertEquals(expected.getMessageHash(), actual.getMessageHash());
        assertEquals(expected.getStatus(), actual.getStatus());
    }
}