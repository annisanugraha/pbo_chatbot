module chatbot {
    // Memberi izin untuk menggunakan komponen JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Memberi izin untuk menggunakan klien HTTP untuk API
    requires java.net.http;

    // Memberi izin untuk menggunakan library JSON
    requires org.json;

    // Membuka paket agar FXML bisa mengakses controller
    opens chatbot.controller to javafx.fxml;

    // Mengekspor paket utama aplikasi
    exports chatbot;
}