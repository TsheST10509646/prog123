/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poeloginandres;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Reads and writes the Stored message collection. */
public final class MessageStorage {
    private static final String FILE_PROPERTY = "quickchat.storage.file";
    private static final String DEFAULT_FILE = "stored_messages.json";

    private final Path file;

    public MessageStorage(Path file) {
        if (file == null) {
            throw new IllegalArgumentException("JSON file path is required.");
        }
        this.file = file;
    }

    public static MessageStorage defaultStorage() {
        return new MessageStorage(Path.of(System.getProperty(
                FILE_PROPERTY, DEFAULT_FILE)));
    }

    public Path getFile() {
        return file;
    }

    public void saveStoredMessages(List<MessageRecord> records) throws IOException {
        Path absolute = file.toAbsolutePath();
        Path directory = absolute.getParent();
        Files.createDirectories(directory);
        Path temporary = Files.createTempFile(directory, "quickchat-", ".tmp");

        try {
            StringBuilder json = new StringBuilder("[\n");

            for (int i = 0; i < records.size(); i++) {
                MessageRecord item = records.get(i);
                if (!MessageRecord.STORED.equals(item.getStatus())) {
                    throw new IllegalArgumentException(
                            "Only Stored messages may be saved in this file.");
                }

                json.append("  {\n")
                    .append("    \"id\": ").append(quote(item.getMessageID())).append(",\n")
                    .append("    \"sender\": ").append(quote(item.getSender())).append(",\n")
                    .append("    \"recipient\": ").append(quote(item.getRecipient())).append(",\n")
                    .append("    \"text\": ").append(quote(item.getMessageText())).append(",\n")
                    .append("    \"hash\": ").append(quote(item.getMessageHash())).append(",\n")
                    .append("    \"status\": ").append(quote(item.getStatus())).append('\n')
                    .append("  }");

                if (i + 1 < records.size()) {
                    json.append(',');
                }
                json.append('\n');
            }

            json.append(']');
            Files.writeString(temporary, json.toString(), StandardCharsets.UTF_8);

            try {
                Files.move(temporary, absolute,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temporary, absolute, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    public List<MessageRecord> readStoredMessages() throws IOException {
        if (!Files.exists(file) || Files.size(file) == 0) {
            return new ArrayList<>();
        }

        String json = Files.readString(file, StandardCharsets.UTF_8);
        if (json.isBlank()) {
            return new ArrayList<>();
        }

        List<MessageRecord> results = new ArrayList<>();
        for (Map<String, String> record : new JsonReader(json).readArray()) {
            if (!MessageRecord.STORED.equals(record.get("status"))) {
                throw new IOException("JSON record must have Stored status.");
            }

            try {
                results.add(new MessageRecord(
                        readField(record, "id", "messageID"),
                        record.get("sender"),
                        record.get("recipient"),
                        readField(record, "text", "messageText"),
                        readField(record, "hash", "messageHash"),
                        record.get("status")));
            } catch (IllegalArgumentException ex) {
                throw new IOException(
                        "Stored JSON contains an invalid record: " + ex.getMessage(), ex);
            }
        }
        return results;
    }

    // Accept the console keys and the older standalone storage keys.
    private static String readField(Map<String, String> record,
                                    String consoleKey, String olderKey) {
        return record.containsKey(consoleKey)
                ? record.get(consoleKey) : record.get(olderKey);
    }

    private static String quote(String value) {
        if (value == null) {
            return "null";
        }

        StringBuilder output = new StringBuilder("\"");
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '"': output.append("\\\""); break;
                case '\\': output.append("\\\\"); break;
                case '\n': output.append("\\n"); break;
                case '\r': output.append("\\r"); break;
                case '\t': output.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        output.append(String.format("\\u%04x", (int) c));
                    } else {
                        output.append(c);
                    }
            }
        }
        return output.append('"').toString();
    }

    /** Reads an array of JSON objects with string or null values. */
    private static final class JsonReader {
        private final String source;
        private int pos;

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
                throw fail("Extra content after the JSON array");
            }
            return records;
        }

        private Map<String, String> readObject() throws IOException {
            Map<String, String> record = new HashMap<>();
            require('{');

            if (!take('}')) {
                do {
                    String key = string();
                    require(':');
                    String val = value();

                    if (record.put(key, val) != null) {
                        throw fail("Duplicate field: " + key);
                    }
                } while (take(','));
                require('}');
            }
            return record;
        }

        private String value() throws IOException {
            whitespace();
            if (pos < source.length() && source.charAt(pos) == '"') {
                return string();
            }
            if (source.startsWith("null", pos)) {
                pos += 4;
                return null;
            }
            throw fail("Expected a JSON string or null");
        }

        private String string() throws IOException {
            require('"');
            StringBuilder output = new StringBuilder();

            while (pos < source.length()) {
                char c = source.charAt(pos++);
                if (c == '"') {
                    return output.toString();
                }

                if (c == '\\') {
                    if (pos == source.length()) {
                        throw fail("Incomplete escape");
                    }
                    c = source.charAt(pos++);

                    switch (c) {
                        case '"': output.append('"'); break;
                        case '\\': output.append('\\'); break;
                        case '/': output.append('/'); break;
                        case 'n': output.append('\n'); break;
                        case 'r': output.append('\r'); break;
                        case 't': output.append('\t'); break;
                        case 'b': output.append('\b'); break;
                        case 'f': output.append('\f'); break;
                        case 'u':
                            if (pos + 4 > source.length()) {
                                throw fail("Incomplete Unicode escape");
                            }
                            try {
                                output.append((char) Integer.parseInt(
                                        source.substring(pos, pos + 4), 16));
                            } catch (NumberFormatException ex) {
                                throw fail("Invalid Unicode escape");
                            }
                            pos += 4;
                            break;
                        default:
                            throw fail("Invalid escape sequence");
                    }
                } else {
                    if (c < 0x20) {
                        throw fail("Unescaped control character");
                    }
                    output.append(c);
                }
            }
            throw fail("Unterminated JSON string");
        }

        private void whitespace() {
            while (pos < source.length()
                    && " \r\n\t".indexOf(source.charAt(pos)) >= 0) {
                pos++;
            }
        }

        private boolean take(char c) {
            whitespace();
            if (pos < source.length() && source.charAt(pos) == c) {
                pos++;
                return true;
            }
            return false;
        }

        private void require(char c) throws IOException {
            if (!take(c)) {
                throw fail("Expected '" + c + "'");
            }
        }

        private IOException fail(String message) {
            return new IOException("Invalid JSON at character " + pos + ": " + message);
        }
    }
}