package chatbot.strategy;

// Perhatikan, ini adalah "interface", bukan "class".
public interface AnsweringStrategy {

    // Ini adalah "pasal kontrak" kita.
    // Semua kelas yang "implements AnsweringStrategy" wajib membuat versi mereka sendiri dari metode ini.
    String getAnswer(String question);

}