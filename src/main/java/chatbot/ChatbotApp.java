package chatbot;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ChatbotApp extends Application { // Perhatikan, sekarang "extends Application"

    @Override
    public void start(Stage primaryStage) throws IOException {
        // 1. Muat file FXML yang sudah kita desain tadi
        // Kode baru yang benar
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/chatbot/gui.fxml"));
        Parent root = loader.load();

        // 2. Siapkan "Scene" (adegan) dengan konten dari FXML
        // Atur ukuran jendela 400x500 piksel
        Scene scene = new Scene(root, 400, 500);

        // 3. Atur "Stage" (panggung atau jendela utama)
        primaryStage.setTitle("Chatbot Kampus Pintar");
        primaryStage.setScene(scene);

        // 4. Tampilkan panggungnya!
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Metode main sekarang tugasnya hanya memanggil launch() untuk memulai aplikasi JavaFX
        launch(args);
    }
}