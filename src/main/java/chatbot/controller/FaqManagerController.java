package chatbot.controller;

import java.util.Optional;

import chatbot.processor.KnowledgeBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;

public class FaqManagerController {

    @FXML
    private TableView<FaqItem> faqTableView;
    @FXML
    private TableColumn<FaqItem, String> keyColumn;
    @FXML
    private TableColumn<FaqItem, String> valueColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private ObservableList<FaqItem> faqList;

    @FXML
    public void initialize() {
        // Mengatur bagaimana kolom menampilkan data
        keyColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

        // Memuat data awal ke tabel
        loadFaqData();
    }

    private void loadFaqData() {
        this.faqList = FXCollections.observableArrayList();
        KnowledgeBase.getFaqData().forEach((key, value) -> {
            faqList.add(new FaqItem(key, value));
        });
        faqTableView.setItems(faqList);
    }

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
                KnowledgeBase.saveFaqToFile(); // Simpan langsung setelah penambahan
                loadFaqData();
            });
        });
    }

    @FXML
    private void handleEdit() {
        FaqItem selectedItem = faqTableView.getSelectionModel().getSelectedItem();
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
            if (newAnswer.isBlank()) {
                showAlert("Peringatan", "Jawaban tidak boleh kosong. Perubahan dibatalkan.");
            } else {
                KnowledgeBase.addOrUpdateFaq(selectedItem.getKey(), newAnswer);
                selectedItem.setValue(newAnswer); // Update tampilan langsung
                KnowledgeBase.saveFaqToFile(); // Simpan langsung setelah pengeditan
                faqTableView.refresh();
            }
        });
    }

    @FXML
    private void handleDelete() {
        FaqItem selectedItem = faqTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Peringatan", "Tidak ada item yang dipilih untuk dihapus.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus");
        confirmAlert.setHeaderText("Anda yakin ingin menghapus FAQ ini?");
        confirmAlert.setContentText("Kata Kunci: " + selectedItem.getKey());

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            KnowledgeBase.removeFaq(selectedItem.getKey());
            faqList.remove(selectedItem); // Hapus dari tampilan langsung
            KnowledgeBase.saveFaqToFile(); // Simpan langsung setelah penghapusan
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (title.equals("Peringatan")) {
            alert.setAlertType(Alert.AlertType.WARNING);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Inner Class untuk FaqItem
    public static class FaqItem {
        private final SimpleStringProperty key;
        private final SimpleStringProperty value;

        public FaqItem(String key, String value) {
            this.key = new SimpleStringProperty(key);
            this.value = new SimpleStringProperty(value);
        }

        public String getKey() {
            return key.get();
        }

        public void setKey(String key) {
            this.key.set(key);
        }

        public String getValue() {
            return value.get();
        }

        public void setValue(String value) {
            this.value.set(value);
        }

        public SimpleStringProperty keyProperty() {
            return key;
        }

        public SimpleStringProperty valueProperty() {
            return value;
        }
    }
}
