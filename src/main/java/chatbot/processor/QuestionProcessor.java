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

    // Konstruktor untuk menginisialisasi semua strategi yang dibutuhkan.
    public QuestionProcessor() {
        this.ruleBased = new RuleBasedStrategy();
        this.apiBased = new ApiBasedStrategy();
    }

    //Memproses pertanyaan masuk dan mengembalikan jawaban dari bot.
    public String process(String question, String mode, List<ChatMessage> conversation) {
        String answer;

        if ("Mode API".equals(mode)) {
            String contextFromRuleBased = this.ruleBased.getAnswer(question);
            JSONArray conversationHistory = convertConversationToJson(conversation);

            if (contextFromRuleBased != null) {
                // Jika ada konteks dari database, panggil API dengan semua informasi
                answer = this.apiBased.getAnswerWithContext(question, contextFromRuleBased, conversationHistory);
            } else {
                // Jika tidak ada konteks, panggil API hanya dengan pertanyaan dan riwayat chat
                answer = this.apiBased.getAnswer(question, conversationHistory);
            }

        } else {
            // Langsung panggil strategi berbasis aturan.
            answer = this.ruleBased.getAnswer(question);
        }
        
        return answer;
    }

    //Mengonversi riwayat percakapan menjadi format JSONArray.
    private JSONArray convertConversationToJson(List<ChatMessage> conversation) {
        JSONArray history = new JSONArray();
        
        // Ambil maksimal 5 percakapan terakhir untuk menjaga relevansi
        int startIndex = Math.max(0, conversation.size() - 5);
        List<ChatMessage> recentConversation = conversation.subList(startIndex, conversation.size());

        for (ChatMessage msg : recentConversation) {
            JSONObject turn = new JSONObject();
            if (msg.getSender() == ChatMessage.Sender.USER) {
                turn.put("role", "user");
            } else {
                turn.put("role", "bot");
            }
            turn.put("content", msg.getContent());
            history.put(turn);
        }
        
        return history;
    }
}