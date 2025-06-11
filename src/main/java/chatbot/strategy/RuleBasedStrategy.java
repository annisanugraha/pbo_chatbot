package chatbot.strategy;

import java.util.Map;

import chatbot.processor.KnowledgeBase;

public class RuleBasedStrategy implements AnsweringStrategy {

    @Override
    public String getAnswer(String question) {
        String lowerCaseQuestion = question.toLowerCase();
        Map<String, String> faqData = KnowledgeBase.getFaqData();

        // Mencari kata kunci
        for (Map.Entry<String, String> entry : faqData.entrySet()) {
            String key = entry.getKey();
            String answer = entry.getValue();

            // Pecah kunci menjadi kata-kata individual
            String[] keywords = key.split(" "); 
            
            // Looping setiap kata kunci
            boolean allKeywordsFound = true; 
            for (String keyword : keywords) {
                if (!lowerCaseQuestion.contains(keyword)) {
                    allKeywordsFound = false;
                    break;
                }
            }

            // Jika kata kunci ditemukan, maka jawaban akan ditampilkan
            if (allKeywordsFound) {
                return answer;
            }
        }
        
        // Jika tidak ada jawaban yang sesuai, maka jawaban default akan ditampilkan
        return null; 
    }
}