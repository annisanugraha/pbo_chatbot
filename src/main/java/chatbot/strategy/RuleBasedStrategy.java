package chatbot.strategy;
import java.util.HashMap;
import java.util.Map;

/**
 * RuleBasedStrategy: Strategi chatbot berbasis aturan sederhana (keyword matching).
 * Cocok untuk pertanyaan seputar kampus: jadwal, dosen, ruangan, KRS, dsb.
 */
public class RuleBasedStrategy implements AnsweringStrategy {
    // Map keyword ke jawaban
    private Map<String, String> rules = new HashMap<>();

    public RuleBasedStrategy() {
        // Aturan dasar FAQ kampus
        rules.put("jadwal", "Lihat jadwal kuliah di SIAKAD atau papan pengumuman kampus.");
        rules.put("dosen", "Nama dosen bisa dilihat di portal kampus atau hubungi bagian akademik.");
        rules.put("ruangan", "Lokasi ruangan dapat dilihat pada denah kampus atau aplikasi SIAKAD.");
        rules.put("krs", "Tata cara KRS: login SIAKAD, pilih menu KRS, lalu ikuti petunjuk pengisian.");
        rules.put("nilai", "Nilai dapat diakses melalui SIAKAD pada menu Nilai Akademik.");
        rules.put("wifi", "Password WiFi kampus bisa didapatkan di bagian IT atau papan pengumuman.");
        rules.put("beasiswa", "Informasi beasiswa tersedia di website resmi kampus atau bagian kemahasiswaan.");
        rules.put("perpustakaan", "Jam buka perpustakaan: Senin-Jumat 08.00-16.00.");
        rules.put("cuti", "Pengajuan cuti kuliah dilakukan melalui SIAKAD atau bagian akademik.");
        // Tambahkan aturan lain sesuai kebutuhan
    }

    @Override
    public String getAnswer(String question) {
        String lowerQ = question.toLowerCase();
        for (String keyword : rules.keySet()) {
            if (lowerQ.contains(keyword)) {
                return rules.get(keyword);
            }
        }
        return "Maaf, saya tidak mengerti pertanyaannya. Silakan ajukan pertanyaan lain atau hubungi admin.";
    }
}