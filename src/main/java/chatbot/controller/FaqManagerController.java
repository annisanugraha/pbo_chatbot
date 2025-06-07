package chatbot.controller;

import java.util.Map;
import java.util.Optional;

import chatbot.processor.KnowledgeBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class FaqManagerController {

    @FXML
    private TableView<Map.Entry<String, String>> faqTableView;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> keyColumn;
    @FXML
    private TableColumn<Map.Entry<String, String>, String> valueColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button saveButton;

    private ObservableList<Map.Entry<String, String>> faqList;

    @FXML
    public void initialize() {
        // Mengatur bagaimana kolom menampilkan data
        keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        valueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue()));

        // Memuat data awal ke tabel
        loadFaqData();
        
        // Bagian setOnAction di sini kita HAPUS, karena event sudah di-handle
        // langsung di file FXML melalui atribut onAction="#namaMetode".
    }

    private void loadFaqData() {
        this.faqList = FXCollections.observableArrayList(KnowledgeBase.getFaqData().entrySet());
        faqTableView.setItems(this.faqList);
    }

    // TAMBAHKAN @FXML agar metode ini bisa "dilihat" oleh FXML
    @FXML
    private void handleAdd() {
        TextInputDialog keyDialog = new TextInputDialog();
        keyDialog.setTitle("Tambah FAQ Baru");
        keyDialog.setHeaderText("Langkah 1: Masukkan Kata Kunci");
        keyDialog.setContentText("Kata Kunci (cth: jadwal pbo):");
        
        Optional<String> keyResult = keyDialog.showAndWait();
        keyResult.ifPresent(key -> {
            if (key.isBlank()) return;
            TextInputDialog valueDialog = new TextInputDialog();
            valueDialog.setTitle("Tambah FAQ Baru");
            valueDialog.setHeaderText("Langkah 2: Masukkan Jawaban");
            valueDialog.setContentText("Jawaban untuk '" + key + "':");

            Optional<String> valueResult = valueDialog.showAndWait();
            valueResult.ifPresent(value -> {
                KnowledgeBase.addOrUpdateFaq(key, value);
                loadFaqData();
            });
        });
    }

    // TAMBAHKAN @FXML
    @FXML
    private void handleEdit() {
        Map.Entry<String, String> selectedItem = faqTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Peringatan", "Tidak ada item yang dipilih untuk diedit.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selectedItem.getValue());
        dialog.setTitle("Edit FAQ");
        dialog.setHeaderText("Mengedit jawaban untuk kata kunci: '" + selectedItem.getKey() + "'");
        dialog.setContentText("Jawaban baru:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newAnswer -> {
            if (!newAnswer.isBlank()) {
                KnowledgeBase.addOrUpdateFaq(selectedItem.getKey(), newAnswer);
                loadFaqData();
            }
        });
    }

    // TAMBAHKAN @FXML
    @FXML
    private void handleDelete() {
        Map.Entry<String, String> selectedItem = faqTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Peringatan", "Tidak ada item yang dipilih untuk dihapus.");
            return;
        }

        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus");
        confirmAlert.setHeaderText("Anda yakin ingin menghapus FAQ ini?");
        confirmAlert.setContentText("Kata Kunci: " + selectedItem.getKey());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            KnowledgeBase.removeFaq(selectedItem.getKey());
            loadFaqData();
        }
    }

    // TAMBAHKAN @FXML
    @FXML
    private void handleSaveAndClose() {
        KnowledgeBase.saveFaqToFile();
        showAlert("Sukses", "Semua perubahan telah disimpan ke file faq.json.");
        
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        if (title.equals("Peringatan")) {
            alert.setAlertType(AlertType.WARNING);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}