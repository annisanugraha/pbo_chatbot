package chatbot.strategy;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Interface untuk strategi menjawab pertanyaan
public class ApiBasedStrategy implements AnsweringStrategy {

    // Menggunakan API key dari environment variable $env:NLP_API_KEY="api_key"
    private static final String API_KEY = System.getenv("NLP_API_KEY");

    // Konteks chatbot
    private static final String CHATBOT_CONTEXT = "Kamu adalah asisten kampus virtual Universitas Dian Nuswantoro. Jawab pertanyaan seputar perkuliahan dengan singkat, jelas, sopan, dan ramah dalam Bahasa Indonesia. Jika tidak tahu jawabannya, katakan saja tidak tahu dan jangan menebak. Jangan keluar dari topik kampus, fokus pada perkuliahan di Universitas Dian Nuswantoro, jangan yang lain.";

    // Client HTTP
    private final HttpClient client = HttpClient.newHttpClient(); // 

    @Override
    // Menggunakan API untuk menjawab pertanyaan
    public String getAnswer(String question) {
        try {
            String escapedContext = CHATBOT_CONTEXT.replace("\"", "\\\"");
            String jsonBody = String.format(
                "{\"input\": \"%s\", \"context\": \"%s\"}",
                question,
                escapedContext
            );

            // Membuat permintaan HTTP POST ke API
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.nlpcloud.io/v1/gpu/chatdolphin/chatbot"))
                .header("Authorization", "Token " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

            // Mengirim permintaan HTTP
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parseResponse(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Mengurai respons dari API
    private String parseResponse(String responseBody) {
        if (responseBody != null && responseBody.contains("\"response\":\"")) {
            String[] parts = responseBody.split("\"response\":\"");
            if (parts.length > 1) {
                String result = parts[1];
                result = result.substring(0, result.indexOf("\""));
                return result.replace("\\n", "\n").strip();
            }
        }
        // Jika respons tidak dapat diurai, kembalikan null
        return "Gagal memahami format respons dari AI.";
    }
}
