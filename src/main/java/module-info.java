module chatbot {
    requires javafx.controls;
    requires javafx.fxml;

    opens chatbot to javafx.fxml;
    opens chatbot.controller to javafx.fxml;
    exports chatbot;
    exports chatbot.controller to javafx.fxml;
}
