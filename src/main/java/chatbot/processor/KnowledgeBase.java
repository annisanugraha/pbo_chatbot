package chatbot.processor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONObject;

public class KnowledgeBase {

    private static final Path FAQ_FILE_PATH = Paths.get("faq.json");
    private static final Map<String, String> faq = new TreeMap<>();

    // Inisialisasi statis untuk memuat data dari file saat kelas dimuat
    static {
        loadFaqFromFile();
    }

    // Metode statis untuk mendapatkan instance Map FAQ
    private static void loadFaqFromFile() {
        // Jika file ada, baca isinya
        if (Files.exists(FAQ_FILE_PATH)) {
            try {
                String jsonText = Files.readString(FAQ_FILE_PATH);
                JSONObject jsonObject = new JSONObject(jsonText);
                
                // Iterasi semua kunci di dalam JSON dan masukkan ke dalam Map
                Iterator<String> keys = jsonObject.keys();
                while(keys.hasNext()) {
                    String key = keys.next();
                    String value = jsonObject.getString(key);
                    faq.put(key, value);
                }
                System.out.println("KnowledgeBase berhasil dimuat dari faq.json");
            } catch (IOException e) {
                System.err.println("Gagal membaca file faq.json: " + e.getMessage());
            }
        } else {
            // Jika file tidak ada, print map kosong
            System.out.println("File faq.json tidak ditemukan. KnowledgeBase kosong.");
        }
    }

    // Metode statis untuk menyimpan data ke file
    public static void saveFaqToFile() {
        JSONObject jsonObject = new JSONObject(faq);
        try {
            // Tulis JSON ke file faq.json
            Files.writeString(FAQ_FILE_PATH, jsonObject.toString(2));
            System.out.println("KnowledgeBase berhasil disimpan ke faq.json");
        } catch (IOException e) {
            System.err.println("Gagal menyimpan ke file faq.json: " + e.getMessage());
        }
    }

    // Metode untuk mendapatkan data FAQ sebagai Map
    public static Map<String, String> getFaqData() {
        return faq;
    }
    
    // Metode untuk mendapatkan jawaban berdasarkan kunci
    public static void addOrUpdateFaq(String key, String value) {
        faq.put(key.toLowerCase(), value);
    }

    // Metode untuk menghapus FAQ berdasarkan kunci
    public static void removeFaq(String key) {
        faq.remove(key.toLowerCase());
    }
}