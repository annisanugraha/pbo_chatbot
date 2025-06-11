package chatbot.processor;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import chatbot.model.ChatMessage;
import chatbot.strategy.ApiBasedStrategy;
import chatbot.strategy.RuleBasedStrategy;

public class QuestionProcessor {
    private final RuleBasedStrategy ruleBased;
    private final ApiBasedStrategy apiBased;

    // Konstruktor untuk inisialisasi strategi
    public QuestionProcessor() {
        this.ruleBased = new RuleBasedStrategy();
        this.apiBased = new ApiBasedStrategy();
    }

    // Metode process sekarang menerima List<ChatMessage>
    public String process(String question, String mode, List<ChatMessage> conversation) {
        String answer;

        if ("Mode API".equals(mode)) {
            answer = this.ruleBased.getAnswer(question);
            if (answer == null) {
                // Mengubah list percakapan menjadi JSON Array
                JSONArray historyJson = convertConversationToJson(conversation);
                answer = this.apiBased.getAnswer(question);
            }
        } else {
            answer = this.ruleBased.getAnswer(question);
        }
        
        return answer;
    }

    // Metode untuk mengonversi percakapan menjadi JSON Array menjadi format yang sesuai untuk API
    private JSONArray convertConversationToJson(List<ChatMessage> conversation) {
        JSONArray history = new JSONArray();
        // Ambil maks 5 percakapan terakhir
        int historySize = Math.min(conversation.size(), 5); 
        
        // Hanya ambil pesan terakhir yang relevan
        if (conversation.size() < historySize) {
            historySize = conversation.size();
        }
        //
        for (int i = conversation.size() - historySize; i < conversation.size(); i++) {
            ChatMessage msg = conversation.get(i);
            JSONObject exchange = new JSONObject();
            if (msg.getSender() == ChatMessage.Sender.USER) {
                exchange.put("input", msg.getContent());
            } else {
                exchange.put("response", msg.getContent());
            }
            // Mengirim input user sebelumnya
            if (msg.getSender() == ChatMessage.Sender.USER) {
                 history.put(new JSONObject().put("input", msg.getContent()));
            }
        }
        return history;
    }
}