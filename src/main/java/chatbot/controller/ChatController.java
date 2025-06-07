package chatbot.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import chatbot.processor.QuestionProcessor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {

    @FXML
    private TextArea chatArea;
    @FXML
    private TextField inputField;
    @FXML
    private Button sendButton;
    @FXML
    private ComboBox<String> modeComboBox;

    private final Path historyFile = Paths.get("chat-history.log");
    
    // Buat satu instance "manajer" kita
    private final QuestionProcessor processor = new QuestionProcessor();

    @FXML
    public void initialize() {
        loadChatHistory();

        // Mengisi pilihan mode ke dalam ComboBox
        modeComboBox.getItems().addAll("Mode Sederhana", "Mode Cerdas");
        // Mengatur "Mode Sederhana" sebagai pilihan default saat aplikasi dibuka
        modeComboBox.setValue("Mode Sederhana");

        chatArea.appendText("Bot: Halo! Selamat datang kembali.\n\n");
    }

    @FXML
    private void handleSendButtonAction() {
        String userQuestion = inputField.getText();
        if (userQuestion.isBlank()) {
            return;
        }

        // Tampilkan & simpan pesan user
        String userMessage = "You: " + userQuestion + "\n";
        chatArea.appendText(userMessage);
        saveMessageToFile(userMessage);

        // Baca mode yang sedang dipilih dari ComboBox
        String selectedMode = modeComboBox.getValue();

        // Berikan pertanyaan DAN mode yang dipilih ke "manajer" kita (QuestionProcessor)
        String botAnswer = processor.process(userQuestion, selectedMode);

        // Tampilkan & simpan jawaban bot
        String botMessage;
        if (botAnswer != null && !botAnswer.isBlank()) {
            botMessage = "Bot: " + botAnswer + "\n\n";
        } else {
            botMessage = "Bot: Maaf, aku sudah coba cari kemana-mana tapi tetap tidak tahu jawabannya.\n\n";
        }
        chatArea.appendText(botMessage);
        saveMessageToFile(botMessage);

        inputField.clear();
    }

    private void saveMessageToFile(String message) {
        try {
            Files.writeString(historyFile, message, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Gagal menyimpan riwayat chat: " + e.getMessage());
        }
    }

    private void loadChatHistory() {
        try {
            if (Files.exists(historyFile)) {
                String history = Files.readString(historyFile);
                chatArea.setText(history);
            }
        } catch (IOException e) {
            System.err.println("Gagal memuat riwayat chat: " + e.getMessage());
        }
    }
}