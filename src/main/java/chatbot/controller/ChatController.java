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

        // "Sumber kebenaran" untuk seluruh percakapan
        private final List<ChatMessage> conversation = new ArrayList<>();
        
        // Path untuk file history berformat JSON
        private final Path historyFile = Paths.get("chat-history.json");
        
        // "Manajer" yang memproses pertanyaan
        private final QuestionProcessor processor = new QuestionProcessor();

        /**
         * Metode ini dijalankan secara otomatis oleh JavaFX setelah tampilan (FXML) selesai dimuat.
         * Sangat cocok untuk melakukan setup awal.
         */
        @FXML
        public void initialize() {
            setupChatListView(); 
            loadChatHistory();   

            modeComboBox.getItems().addAll("Mode Rule", "Mode API");
            modeComboBox.setValue("Mode Rule");

            if (conversation.isEmpty()) {
                addMessage(ChatMessage.Sender.BOT, "Halo! Ada yang bisa dibantu?");
            }

            // ➔ Tambahkan ini untuk mendukung ENTER
            inputField.setOnAction(event -> handleSendButtonAction());
        }


        /**
         * Metode ini mengatur "pabrik sel" untuk ListView.
         * Ini mendefinisikan bagaimana setiap objek ChatMessage akan digambar menjadi gelembung chat.
         */
        private void setupChatListView() {
            chatListView.setCellFactory(param -> new ListCell<ChatMessage>() {
                private final Label messageBubble = new Label();
                private final Label timestampLabel = new Label();
                private final VBox container = new VBox(messageBubble, timestampLabel);

                {
                    messageBubble.setWrapText(true); // Mengaktifkan bungkus teks
                    // Mengikat lebar maksimal bubble dengan 70% lebar listview
                    messageBubble.maxWidthProperty().bind(chatListView.widthProperty().multiply(0.70));
                }

                @Override
                protected void updateItem(ChatMessage message, boolean empty) {
                    super.updateItem(message, empty);
                    if (empty || message == null) {
                        setGraphic(null);
                    } else {
                        messageBubble.setText(message.getContent());
                        timestampLabel.setText(message.getFormattedTimestamp());
                        timestampLabel.getStyleClass().add("timestamp-label");

                        // Mengatur perataan dan gaya CSS berdasarkan pengirim
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

        /**
         * Metode ini dieksekusi setiap kali tombol "Kirim" ditekan.
         */
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
        
        /**
         * Metode bantuan untuk menambahkan pesan baru ke list, lalu mengupdate tampilan dan history.
         */
        private void addMessage(ChatMessage.Sender sender, String content) {
            ChatMessage message = new ChatMessage(sender, content);
            conversation.add(message);
            chatListView.getItems().add(message);
            chatListView.scrollTo(conversation.size() - 1); // Otomatis scroll ke bawah
            saveChatHistory();
        }

        /**
         * Menyimpan seluruh list percakapan ke file chat-history.json.
         * Metode ini menimpa (overwrite) file setiap kali dipanggil.
         */
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
                Files.writeString(historyFile, jsonArray.toString(2)); // Angka 2 untuk format rapi
            } catch (IOException e) {
                System.err.println("Gagal menyimpan riwayat chat: " + e.getMessage());
            }
        }

        /**
         * Memuat riwayat percakapan dari file chat-history.json saat aplikasi pertama kali dibuka.
         */
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
                    
                    // PERBAIKAN: Membaca dan mengonversi timestamp dari file
                    String timestampStr = jsonMessage.getString("timestamp");
                    LocalDateTime timestamp = LocalDateTime.parse(timestampStr);
                    
                    // Menggunakan constructor baru untuk membuat ulang objek dengan timestamp aslinya
                    conversation.add(new ChatMessage(sender, content, timestamp));
                }
                chatListView.getItems().setAll(conversation);
            } catch (Exception e) {
                System.err.println("Gagal memuat riwayat chat: " + e.getMessage());
            }
        }

        /**
         * Metode ini akan dieksekusi saat menu "Manajemen FAQ" di-klik.
         */
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

                // 3. Tampilkan jendela dan tunggu sampai jendela ini ditutup
                // Ini akan mengunci jendela chat utama sampai jendela admin ditutup.
                adminStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
                // Di aplikasi nyata, kita akan menampilkan dialog error di sini.
            }
        }
    }