package chatbot.strategy;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiBasedStrategy implements AnsweringStrategy {

    private static final String API_KEY = "5c2be345b3b0b8eb638656448ee87c7a9d004249";

    private static final String CHATBOT_CONTEXT =
        "Kamu adalah asisten kampus virtual yang menjawab pertanyaan seputar perkuliahan di Universitas Dian Nuswantoro. Jawab semua pertanyaan dengan sopan dan informatif dalam Bahasa Indonesia. Jika kamu tidak tahu, katakan saja tidak tahu.";

    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public String getAnswer(String question) {
        try {
            String escapedContext = CHATBOT_CONTEXT.replace("\"", "\\\"");
            String jsonBody = String.format(
                "{\"input\": \"%s\", \"context\": \"%s\"}",
                question,
                escapedContext
            );

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.nlpcloud.io/v1/gpu/chatdolphin/chatbot"))
                .header("Authorization", "Token " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parseGptResponse(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseGptResponse(String responseBody) {
        if (responseBody != null && responseBody.contains("\"response\":\"")) {
            String[] parts = responseBody.split("\"response\":\"");
            if (parts.length > 1) {
                String result = parts[1];
                result = result.substring(0, result.indexOf("\""));
                return result.replace("\\n", "\n").strip();
            }
        }
        return "Gagal memahami format respons dari AI.";
    }
}
