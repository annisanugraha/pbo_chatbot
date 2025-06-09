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

    public QuestionProcessor() {
        this.ruleBased = new RuleBasedStrategy();
        this.apiBased = new ApiBasedStrategy();
    }

    // Metode process sekarang menerima List<ChatMessage>
    public String process(String question, String mode, List<ChatMessage> conversation) {
        String answer;

        if ("Mode Cerdas".equals(mode)) {
            answer = this.ruleBased.getAnswer(question);
            if (answer == null) {
                // Ubah list percakapan menjadi JSON Array
                JSONArray historyJson = convertConversationToJson(conversation);
                answer = this.apiBased.getAnswer(question);
            }
        } else {
            answer = this.ruleBased.getAnswer(question);
        }
        
        return answer;
    }

    // Metode bantuan untuk mengubah List menjadi JSON Array untuk API
    private JSONArray convertConversationToJson(List<ChatMessage> conversation) {
        JSONArray history = new JSONArray();
        // Ambil beberapa pesan terakhir saja agar tidak terlalu panjang
        int historySize = Math.min(conversation.size(), 5); // Ambil maks 5 percakapan terakhir
        
        for (int i = conversation.size() - historySize; i < conversation.size(); i++) {
            ChatMessage msg = conversation.get(i);
            JSONObject exchange = new JSONObject();
            if (msg.getSender() == ChatMessage.Sender.USER) {
                exchange.put("input", msg.getContent());
            } else {
                // API mungkin tidak butuh response bot, tapi ini format umumnya
                exchange.put("response", msg.getContent());
            }
            // Logika sederhana untuk memasangkan input-response, bisa disempurnakan
            // Untuk sekarang kita hanya akan mengirim input user sebelumnya
            if (msg.getSender() == ChatMessage.Sender.USER) {
                 history.put(new JSONObject().put("input", msg.getContent()));
            }
        }
        return history;
    }
}