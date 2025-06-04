package chatbot.processor;
import java.util.HashMap;
import java.util.Map;

public class KnowledgeBase {
    private Map<String, String> faqList = new HashMap<>();

    public void loadFromJSON(String filePath) {
        // Gunakan library seperti Gson/Jackson untuk membaca file JSON
    }

    public String getAnswerFor(String question) {
        // Mirip RuleBasedStrategy tapi dari file
        return faqList.getOrDefault(question.toLowerCase(), "Tidak ditemukan di FAQ.");
    }
}


//Menyimpan daftar pertanyaan-jawaban (FAQ).
//Format bisa berupa JSON atau file lokal sederhana.