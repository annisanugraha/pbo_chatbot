package chatbot.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage implements Serializable {

    public enum Sender {
        USER, BOT
    }

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Sender sender;
    private final String content;
    private final LocalDateTime timestamp;

    // Constructor untuk pesan baru
    public ChatMessage(Sender sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now(); 
    }

    // Constructor untuk meload history chat
    public ChatMessage(Sender sender, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }


    public Sender getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(TIME_FORMATTER);
    }

    @Override
    public String toString() {
        String prefix = (sender == Sender.USER) ? "You: " : "Bot: ";
        return prefix + content;
    }
}