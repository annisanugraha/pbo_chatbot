package chatbot.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    private String sender;
    private String content;
    private LocalDateTime timestamp;

    public ChatMessage(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return sender + " [" + timestamp.format(formatter) + "]: " + content;
    }
}


//Struktur data untuk menyimpan isi pesan (user/bot, isi, timestamp).