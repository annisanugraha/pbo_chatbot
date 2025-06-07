package chatbot.model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage implements Serializable {

    public enum Sender {
        USER, BOT
    }

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final Sender sender;
    private final String content;
    private final LocalDateTime timestamp;

    // Constructor #1: Ini untuk membuat pesan BARU. Timestamp dibuat otomatis.
    public ChatMessage(Sender sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now(); 
    }

    // ===== CONSTRUCTOR BARU #2: Ini untuk me-load pesan LAMA dari history =====
    // Constructor ini menerima timestamp secara spesifik dari data yang kita baca.
    public ChatMessage(Sender sender, String content, LocalDateTime timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    // --- Getter methods ---
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