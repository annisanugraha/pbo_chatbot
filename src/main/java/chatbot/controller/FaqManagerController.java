package chatbot.controller;

import java.util.Optional;

import chatbot.processor.KnowledgeBase;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

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

    // Di dalam FaqManagerController.java

    @FXML
    private void handleAdd() {
        // 1. Membuat dialog custom, sama persis seperti di handleEdit
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Tambah FAQ Baru");
        dialog.setHeaderText("Masukkan Kata Kunci dan Jawaban baru di bawah ini.");

        // 2. Menyiapkan tombol OK dan Cancel
        ButtonType okButtonType = new ButtonType("OK", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // 3. Membuat layout grid untuk menata label dan kolom input
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Perbedaan utama: Kolom inputnya kita buat KOSONG
        TextField keyField = new TextField();
        keyField.setPromptText("Kata Kunci (cth: jadwal metopen)");
        TextArea valueArea = new TextArea();
        valueArea.setPromptText("Jawaban untuk kata kunci di atas");
        valueArea.setWrapText(true);

        grid.add(new Label("Kata Kunci:"), 0, 0);
        grid.add(keyField, 1, 0);
        grid.add(new Label("Jawaban:"), 0, 1);
        grid.add(valueArea, 1, 1);

        // 4. Menaruh layout grid ke dalam dialog
        dialog.getDialogPane().setContent(grid);

        // 5. Mengatur agar tombol OK tidak bisa ditekan jika kolom kata kunci kosong
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);
        keyField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        // Meminta fokus pada kolom pertama saat dialog dibuka
        Platform.runLater(keyField::requestFocus);

        // 6. Mengonversi hasil input menjadi sepasang key-value (logikanya sama seperti handleEdit)
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(keyField.getText(), valueArea.getText());
            }
            return null;
        });

        // 7. Menampilkan dialog dan memproses hasilnya
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            // Logikanya lebih sederhana, hanya menambah, tidak perlu hapus data lama
            KnowledgeBase.addOrUpdateFaq(pair.getKey(), pair.getValue());
            KnowledgeBase.saveFaqToFile();
            loadFaqData(); // Refresh tabel
        });
    }

    @FXML
    private void handleEdit() {
        FaqItem selectedItem = faqTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Peringatan", "Tidak ada item yang dipilih untuk diedit.");
            return;
        }

        // 1. Membuat dialog custom baru yang akan mengembalikan sepasang String
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit FAQ");
        dialog.setHeaderText("Anda dapat mengedit Kata Kunci dan Jawaban di sini.");

        // 2. Menyiapkan tombol OK dan Cancel untuk dialog
        ButtonType okButtonType = new ButtonType("OK", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // 3. Membuat layout grid untuk menata label dan kolom input
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField keyField = new TextField(selectedItem.getKey());
        keyField.setPromptText("Kata Kunci");
        TextArea valueArea = new TextArea(selectedItem.getValue());
        valueArea.setPromptText("Jawaban");
        valueArea.setWrapText(true);

        grid.add(new Label("Kata Kunci:"), 0, 0);
        grid.add(keyField, 1, 0);
        grid.add(new Label("Jawaban:"), 0, 1);
        grid.add(valueArea, 1, 1);

        // 4. Menaruh layout grid ke dalam panel konten dialog
        dialog.getDialogPane().setContent(grid);
        
        // 5. Mengatur agar tombol OK tidak bisa ditekan jika kolom kata kunci kosong
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(false); // Awalnya bisa ditekan
        keyField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        
        // Meminta fokus pada kolom pertama saat dialog dibuka
        Platform.runLater(keyField::requestFocus);

        // 6. Mengonversi hasil input menjadi sebuah "Pair" (pasangan) key-value saat OK ditekan
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(keyField.getText(), valueArea.getText());
            }
            return null;
        });

        // 7. Menampilkan dialog dan memproses hasilnya jika user menekan OK
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            String newKey = pair.getKey();
            String newValue = pair.getValue();

            // Karena key bisa berubah, cara teraman adalah hapus data lama dan tambahkan data baru
            KnowledgeBase.removeFaq(selectedItem.getKey());
            KnowledgeBase.addOrUpdateFaq(newKey, newValue);
            
            // Simpan perubahan ke file dan muat ulang tabel untuk me-refresh tampilan
            KnowledgeBase.saveFaqToFile();
            loadFaqData();
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
