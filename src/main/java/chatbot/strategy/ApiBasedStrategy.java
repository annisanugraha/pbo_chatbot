package chatbot.strategy;

public class ApiBasedStrategy implements AnsweringStrategy {
    private String apiKey;

    public ApiBasedStrategy(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getAnswer(String question) {
        // Panggilan HTTP ke OpenAI/HuggingFace dapat ditambahkan di sini
        return "Fitur API belum diimplementasikan.";
    }
}
