package chatbot.processor;

import chatbot.strategy.AnsweringStrategy;
import chatbot.strategy.ApiBasedStrategy;
import chatbot.strategy.RuleBasedStrategy;

public class QuestionProcessor {

    private final AnsweringStrategy ruleBased;
    private final AnsweringStrategy apiBased;

    public QuestionProcessor() {
        this.ruleBased = new RuleBasedStrategy();
        this.apiBased = new ApiBasedStrategy();
    }

    public String process(String question, String mode) {
        String answer;

        if ("Mode Cerdas".equals(mode)) {
            // Mode Cerdas: Coba kamus internal dulu, jika gagal baru pakai API.
            answer = this.ruleBased.getAnswer(question);
            if (answer == null) {
                answer = this.apiBased.getAnswer(question);
            }
        } else {
            // Mode Sederhana: Hanya pakai kamus internal.
            answer = this.ruleBased.getAnswer(question);
        }

        return answer;
    }
}
