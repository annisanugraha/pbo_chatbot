package chatbot.controller;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class ChatController {
    private QuestionProcessor processor = new QuestionProcessor();
    private List<ChatMessage> messages = new ArrayList<>();
    private ComboBox<String> modeComboBox = new ComboBox<>();
    private TextArea chatArea = new TextArea();
    private TextField inputField = new TextField();

public Scene initUI() {
    chatArea.setEditable(false);
    Button sendButton = new Button("Kirim");
    sendButton.setOnAction(e -> handleSendMessage());

    VBox layout = new VBox(10, chatArea, inputField, sendButton);
        return new Scene(layout, 400, 400);

    modeComboBox.getItems().addAll("Rule-Based", "API-Based");
    modeComboBox.setValue("Rule-Based");

    modeComboBox.setOnAction(e -> {
    String selected = modeComboBox.getValue();
    if (selected.equals("API-Based")) {
        processor.setStrategy(new ApiBasedStrategy("YOUR_API_KEY")); // Ganti dengan API key nyata
    } else {
        processor.setStrategy(new RuleBasedStrategy());
    }
});

    }   

    public void handleSendMessage() {
        String input = inputField.getText();
        ChatMessage userMsg = new ChatMessage("User", input);
        messages.add(userMsg);
        displayMessage(userMsg);

        String response = processor.process(input);
        ChatMessage botMsg = new ChatMessage("Bot", response);
        messages.add(botMsg);
        displayMessage(botMsg);

        inputField.clear();
    }

    private void displayMessage(ChatMessage msg) {
        chatArea.appendText(msg.getFormatted() + "\n");
    }
}

//Menangani logika interaksi pengguna dan bot.
//Memanggil QuestionProcessor dan menampilkan hasil.