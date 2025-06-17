package chatbot;

import java.io.IOException;
import java.net.URL; // Import URL

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatbotApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Akses fxml
        URL fxmlLocation = getClass().getResource("/chatbot/gui.fxml");

        // Pengecekan penting: jika alamatnya tidak ditemukan, hentikan program dengan pesan jelas
        if (fxmlLocation == null) {
            System.err.println("CRITICAL ERROR: File 'gui.fxml' tidak ditemukan di path '/chatbot/gui.fxml'.");
            System.err.println("Pastikan file tersebut ada di dalam 'src/main/resources/chatbot/'.");
            return; // Hentikan aplikasi
        }

        // Muat FXML dari lokasi yang sudah pasti benar
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setTitle("Chatbot Kampus Pintar");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}