module chatbot {
    // Izin yang sudah ada
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;

    // ===== TAMBAHKAN IZIN BARU DI SINI =====
    requires org.json;

    // Bagian ini biarkan saja seperti semula
    opens chatbot.controller to javafx.fxml;
    exports chatbot;
}