    package chatbot.controller;

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    import org.json.JSONArray;
    import org.json.JSONObject;

    import chatbot.model.ChatMessage;
    import chatbot.processor.QuestionProcessor;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.geometry.Pos;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.Button;
    import javafx.scene.control.ComboBox;
    import javafx.scene.control.Label;
    import javafx.scene.control.ListCell;
    import javafx.scene.control.ListView;
    import javafx.scene.control.MenuItem;
    import javafx.scene.control.TextField;
    import javafx.scene.layout.VBox;
    import javafx.stage.Stage;

    public class ChatController {

        @FXML
        private ListView<ChatMessage> chatListView;
        @FXML
        private TextField inputField;
        @FXML
        private Button sendButton;
        @FXML
        private ComboBox<String> modeComboBox;
        @FXML
        private MenuItem faqManagerMenuItem;

        // Daftar untuk menyimpan seluruh percakapan
        private final List<ChatMessage> conversation = new ArrayList<>();
        
        // File untuk menyimpan riwayat chat
        private final Path historyFile = Paths.get("chat-history.json");
        
        // Objek untuk memproses pertanyaan
        private final QuestionProcessor processor = new QuestionProcessor();

        // Inisialisasi controller, ini akan dipanggil saat FXML dimuat
        @FXML
        public void initialize() {
            setupChatListView(); 
            loadChatHistory();   

            modeComboBox.getItems().addAll("Mode Rule", "Mode API");
            modeComboBox.setValue("Mode Rule");

            if (conversation.isEmpty()) {
                addMessage(ChatMessage.Sender.BOT, "Halo! Ada yang bisa dibantu?");
            }
            inputField.setOnAction(event -> handleSendButtonAction());
        }

        // Metode untuk mengatur tampilan ListView chat
        private void setupChatListView() {
            chatListView.setCellFactory(param -> new ListCell<ChatMessage>() {
                private final Label messageBubble = new Label();
                private final Label timestampLabel = new Label();
                private final VBox container = new VBox(messageBubble, timestampLabel);
                {
                    messageBubble.setWrapText(true);
                    messageBubble.maxWidthProperty().bind(chatListView.widthProperty().multiply(0.70));
                }

                // Override metode updateItem untuk mengatur tampilan setiap item
                @Override
                protected void updateItem(ChatMessage message, boolean empty) {
                    super.updateItem(message, empty);
                    if (empty || message == null) {
                        setGraphic(null);
                    } else {
                        messageBubble.setText(message.getContent());
                        timestampLabel.setText(message.getFormattedTimestamp());
                        timestampLabel.getStyleClass().add("timestamp-label");

                        // Atur gaya dan posisi bubble pesan berdasarkan pengirim
                        if (message.getSender() == ChatMessage.Sender.USER) {
                            messageBubble.getStyleClass().setAll("chat-bubble", "chat-bubble-user");
                            container.setAlignment(Pos.CENTER_RIGHT);
                        } else {
                            messageBubble.getStyleClass().setAll("chat-bubble", "chat-bubble-bot");
                            container.setAlignment(Pos.CENTER_LEFT);
                        }
                        setGraphic(container);
                    }
                }
            });
        }

        // Metode untuk menangani aksi tombol kirim
        @FXML
        private void handleSendButtonAction() {
            String userQuestion = inputField.getText();
            if (userQuestion.isBlank()) {
                return;
            }

            // 1. Tambahkan pesan pengguna ke percakapan
            addMessage(ChatMessage.Sender.USER, userQuestion);
            
            // 2. Baca mode yang dipilih
            String selectedMode = modeComboBox.getValue();
            
            // 3. Proses pertanyaan untuk mendapatkan jawaban bot
            String botAnswer = processor.process(userQuestion, selectedMode, conversation);

            // 4. Tambahkan jawaban bot ke percakapan
            String responseContent = (botAnswer != null && !botAnswer.isBlank()) ? 
                botAnswer : "Maaf, aku sudah coba cari kemana-mana tapi tetap tidak tahu jawabannya.";
            addMessage(ChatMessage.Sender.BOT, responseContent);

            inputField.clear();
        }
        
        // Metode untuk menambahkan pesan ke dalam percakapan dan memperbarui tampilan ListView
        private void addMessage(ChatMessage.Sender sender, String content) {
            ChatMessage message = new ChatMessage(sender, content);
            conversation.add(message);
            chatListView.getItems().add(message);
            chatListView.scrollTo(conversation.size() - 1);
            saveChatHistory();
        }

        // Metode untuk menyimpan riwayat chat ke file chat-history.json
        private void saveChatHistory() {
            JSONArray jsonArray = new JSONArray();
            for (ChatMessage message : conversation) {
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put("sender", message.getSender().toString());
                jsonMessage.put("content", message.getContent());
                jsonMessage.put("timestamp", message.getTimestamp().toString());
                jsonArray.put(jsonMessage);
            }
            try {
                Files.writeString(historyFile, jsonArray.toString(2));
            } catch (IOException e) {
                System.err.println("Gagal menyimpan riwayat chat: " + e.getMessage());
            }
        }

        // Metode untuk memuat riwayat chat dari file chat-history.json
        private void loadChatHistory() {
            if (!Files.exists(historyFile)) {
                return;
            }
            try {
                String jsonText = Files.readString(historyFile);
                JSONArray jsonArray = new JSONArray(jsonText);
                
                conversation.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonMessage = jsonArray.getJSONObject(i);
                    
                    ChatMessage.Sender sender = ChatMessage.Sender.valueOf(jsonMessage.getString("sender"));
                    String content = jsonMessage.getString("content");
                    
                    String timestampStr = jsonMessage.getString("timestamp");
                    LocalDateTime timestamp = LocalDateTime.parse(timestampStr);
                    
                    conversation.add(new ChatMessage(sender, content, timestamp));
                }
                chatListView.getItems().setAll(conversation);
            } catch (Exception e) {
                System.err.println("Gagal memuat riwayat chat: " + e.getMessage());
            }
        }

        // Metode untuk menangani klik pada menu FAQ Manager
        @FXML
        private void handleFaqManagerMenu() {
            try {
                // 1. Muat file FXML untuk jendela admin yang sudah kita buat
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatbot/FaqManager.fxml"));
                Parent root = loader.load();

                // 2. Buat Stage (jendela) baru yang terpisah
                Stage adminStage = new Stage();
                adminStage.setTitle("Admin - Manajemen FAQ");
                adminStage.setScene(new Scene(root));

                // 3. Ambil controller dari FXML yang sudah dimuat
                adminStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }