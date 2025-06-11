package chatbot.strategy;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;

public class ApiBasedStrategy implements AnsweringStrategy {

    // Menggunakan API key dari environment variable $env:NLP_API_KEY="api_key"
    private static final String API_KEY = System.getenv("NLP_API_KEY");

    // Konteks chatbot default
    private static final String DEFAULT_CHATBOT_CONTEXT = "Kamu adalah asisten kampus virtual Universitas Dian Nuswantoro berbentuk chatbot. Jawab pertanyaan seputar perkuliahan dengan singkat, jelas, sopan, dan ramah dalam Bahasa Indonesia. Jika tidak tahu jawabannya, katakan saja tidak tahu dan jangan menebak. Jangan keluar dari topik kampus, fokus pada perkuliahan di Universitas Dian Nuswantoro, jangan yang lain.";

    // Client HTTP
    private final HttpClient client = HttpClient.newHttpClient();

    @Override
    public String getAnswer(String question) {
        return getAnswer(question, new JSONArray());
    }

    // Metode ini dipanggil jika TIDAK ADA konteks dari database lokal.
    public String getAnswer(String question, JSONArray history) {
        // Prompt untuk API
        String finalPrompt = "Riwayat Percakapan:\n" + history.toString(2) + "\n\n"
                           + "Pertanyaan Pengguna: " + question;
                           
        return callApi(finalPrompt, DEFAULT_CHATBOT_CONTEXT);
    }

    // Metode ini dipanggil KETIKA ADA konteks dari database lokal.
    public String getAnswerWithContext(String question, String localContext, JSONArray history) {
        // Prompting sebelum dikirim ke API
        String finalPrompt = "Gunakan informasi dari 'Konteks Database' dan 'Riwayat Percakapan' untuk menjawab 'Pertanyaan Pengguna'.\n\n"
                           + "--- Konteks Database ---\n"
                           + localContext + "\n\n"
                           + "--- Riwayat Percakapan ---\n"
                           + history.toString(2) + "\n\n"
                           + "--- Pertanyaan Pengguna ---\n"
                           + question;
                           
        return callApi(finalPrompt, DEFAULT_CHATBOT_CONTEXT);
    }

    // Helper method untuk melakukan pemanggilan API.
    private String callApi(String prompt, String systemContext) {
        // Validasi API Key
        if (API_KEY == null || API_KEY.isBlank()) {
            return "ERROR: NLP_API_KEY belum di-set. Silakan cek environment variable.";
        }

        try {
            // Membuat JSON body untuk dikirim ke API
            String jsonBody = String.format(
                "{\"input\": \"%s\", \"context\": \"%s\"}",
                escapeJson(prompt),
                escapeJson(systemContext)
            );

            // Membuat permintaan HTTP POST ke API
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.nlpcloud.io/v1/gpu/chatdolphin/chatbot"))
                    .header("Authorization", "Token " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // Mengirim permintaan HTTP dan menerima respons
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parseResponse(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return "Terjadi kesalahan saat menghubungi server AI.";
        }
    }

    // Mengurai respons dari API
    private String parseResponse(String responseBody) {
        if (responseBody != null && responseBody.contains("\"response\":\"")) {
            try {
                String[] parts = responseBody.split("\"response\":\"");
                String result = parts[1].split("\"")[0];
                return result.replace("\\n", "\n").strip();
            } catch (Exception e) {
                 return "Gagal memahami format respons dari AI (Parsing Error).";
            }
        }
        return "Respons dari AI kosong atau tidak valid.";
    }

    // Helper method untuk escape karakter spesial di JSON
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\b", "\\b")
                   .replace("\f", "\\f")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}