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

    private ObservableList<FaqItem> faqList;

    // Inisialisasi controller, akan dipanggil saat FXML dimuat
    @FXML
    public void initialize() {
        keyColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        loadFaqData();
    }

    // Memuat data FAQ dari KnowledgeBase dan menampilkannya di TableView
    private void loadFaqData() {
        this.faqList = FXCollections.observableArrayList();
        KnowledgeBase.getFaqData().forEach((key, value) -> {
            faqList.add(new FaqItem(key, value));
        });
        faqTableView.setItems(faqList);
    }

    // Metode untuk menangani penambahan FAQ baru
    @FXML
    private void handleAdd() {
        // Membuat dialog custom baru yang akan mengembalikan sepasang String
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Tambah FAQ Baru");
        dialog.setHeaderText("Masukkan Kata Kunci dan Jawaban baru di bawah ini.");

        // Tombol OK dan Cancel untuk dialog
        ButtonType okButtonType = new ButtonType("OK", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Membuat kolom input untuk kata kunci dan jawaban
        TextField keyField = new TextField();
        keyField.setPromptText("Kata Kunci (cth: jadwal metopen)");
        TextArea valueArea = new TextArea();
        valueArea.setPromptText("Jawaban untuk kata kunci di atas");
        valueArea.setWrapText(true);

        grid.add(new Label("Kata Kunci:"), 0, 0);
        grid.add(keyField, 1, 0);
        grid.add(new Label("Jawaban:"), 0, 1);
        grid.add(valueArea, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Mengatur agar tombol OK tidak bisa ditekan jika kolom kata kunci kosong
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);
        keyField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        Platform.runLater(keyField::requestFocus);

        // Mengonversi hasil input menjadi sebuah pasangan key-value saat OK ditekan
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(keyField.getText(), valueArea.getText());
            }
            return null;
        });

        // Menampilkan dialog dan memproses hasilnya jika user menekan OK
        Optional<Pair<String, String>> result = dialog.showAndWait();

        // Jika hasilnya ada, tambahkan ke KnowledgeBase dan simpan ke file
        result.ifPresent(pair -> {
            KnowledgeBase.addOrUpdateFaq(pair.getKey(), pair.getValue());
            KnowledgeBase.saveFaqToFile();
            loadFaqData(); // Refresh tabel
        });
    }

    // Metode untuk menangani pengeditan FAQ yang sudah ada
    @FXML
    private void handleEdit() {
        // Memastikan ada item yang dipilih sebelum mengedit
        FaqItem selectedItem = faqTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Peringatan", "Tidak ada item yang dipilih untuk diedit.");
            return;
        }

        // Membuat dialog untuk mengedit FAQ yang sudah ada
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit FAQ");
        dialog.setHeaderText("Anda dapat mengedit Kata Kunci dan Jawaban di sini.");

        // Tombol OK dan Cancel untuk dialog
        ButtonType okButtonType = new ButtonType("OK", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Membuat kolom input untuk kata kunci dan jawaban, diisi dengan data yang sudah ada
        TextField keyField = new TextField(selectedItem.getKey());
        keyField.setPromptText("Kata Kunci");
        TextArea valueArea = new TextArea(selectedItem.getValue());
        valueArea.setPromptText("Jawaban");
        valueArea.setWrapText(true);

        grid.add(new Label("Kata Kunci:"), 0, 0);
        grid.add(keyField, 1, 0);
        grid.add(new Label("Jawaban:"), 0, 1);
        grid.add(valueArea, 1, 1);

        dialog.getDialogPane().setContent(grid);
        
        // Mengatur agar tombol OK tidak bisa ditekan jika kolom kata kunci kosong
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(false); // Awalnya bisa ditekan
        keyField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        
        // Meminta fokus pada kolom pertama saat dialog dibuka
        Platform.runLater(keyField::requestFocus);

        // Mengonversi hasil input menjadi sebuah pasangan key-value saat OK ditekan
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return new Pair<>(keyField.getText(), valueArea.getText());
            }
            return null;
        });

        // Menampilkan dialog dan memproses hasilnya jika user menekan OK
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            String newKey = pair.getKey();
            String newValue = pair.getValue();

            // Karena key bisa berubah, hapus data lama dan tambahkan data baru
            KnowledgeBase.removeFaq(selectedItem.getKey());
            KnowledgeBase.addOrUpdateFaq(newKey, newValue);
            
            // Simpan perubahan ke file dan muat ulang tabel untuk me-refresh tampilan
            KnowledgeBase.saveFaqToFile();
            loadFaqData();
        });
    }

    // Metode untuk menangani penghapusan FAQ yang sudah ada
    @FXML
    private void handleDelete() {
        // Memastikan ada item yang dipilih sebelum menghapus
        FaqItem selectedItem = faqTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Peringatan", "Tidak ada item yang dipilih untuk dihapus.");
            return;
        }

        // Menampilkan dialog konfirmasi sebelum menghapus
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Hapus");
        confirmAlert.setHeaderText("Anda yakin ingin menghapus FAQ ini?");
        confirmAlert.setContentText("Kata Kunci: " + selectedItem.getKey());

        // Tombol OK dan Cancel
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            KnowledgeBase.removeFaq(selectedItem.getKey());
            faqList.remove(selectedItem); // Hapus dari tampilan langsung
            KnowledgeBase.saveFaqToFile(); // Simpan langsung setelah penghapusan
        }
    }

    // Menampilkan alert
    private void showAlert(String title, String message) {
        // Membuat alert dengan tipe sesuai judul
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // Jika judulnya "Peringatan", tipe alert menjadi WARNING
        if (title.equals("Peringatan")) {
            alert.setAlertType(Alert.AlertType.WARNING);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Kelas FaqItem untuk menyimpan data FAQ
    public static class FaqItem {
        private final SimpleStringProperty key;
        private final SimpleStringProperty value;

        // Konstruktor untuk membuat item FAQ baru
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
