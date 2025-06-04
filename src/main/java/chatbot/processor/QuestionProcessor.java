package chatbot.processor;
public class QuestionProcessor {
    private AnsweringStrategy strategy;

    public QuestionProcessor() {
        // Default to rule-based
        this.strategy = new RuleBasedStrategy();
    }

    public void setStrategy(AnsweringStrategy strategy) {
        this.strategy = strategy;
    }

    public String process(String question) {
        return strategy.getAnswer(question);
    }
}


//Menentukan jenis pertanyaan dan strategi yang digunakan.