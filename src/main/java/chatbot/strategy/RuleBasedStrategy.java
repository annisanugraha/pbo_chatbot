package chatbot.strategy;

import java.util.Map;

import chatbot.processor.KnowledgeBase;

public class RuleBasedStrategy implements AnsweringStrategy {

    @Override
    public String getAnswer(String question) {
        String lowerCaseQuestion = question.toLowerCase();
        Map<String, String> faqData = KnowledgeBase.getFaqData();

        // --- LOGIKA PENCARIAN KATA KUNCI ---
        for (Map.Entry<String, String> entry : faqData.entrySet()) {
            String key = entry.getKey();
            String answer = entry.getValue();

            // Pecah kunci menjadi kata-kata individual
            String[] keywords = key.split(" "); 
            
            boolean allKeywordsFound = true; 
            for (String keyword : keywords) {
                if (!lowerCaseQuestion.contains(keyword)) {
                    allKeywordsFound = false;
                    break;
                }
            }

            if (allKeywordsFound) {
                return answer;
            }
        }
        
        return null; 
    }
}