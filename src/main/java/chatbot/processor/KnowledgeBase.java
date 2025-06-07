package chatbot.processor;

import java.util.HashMap;
import java.util.Map;

public class KnowledgeBase {

    private static final Map<String, String> faq = new HashMap<>();

    static {

        faq.put("sejarah udinus", "Udinus berawal dari kursus komputer IMKA pada tahun 1986 dan resmi menjadi universitas pada 30 Agustus 2001 melalui penggabungan STMIK, STIE, STBA, dan STKES.");
        faq.put("visi udinus", "Visi Udinus adalah menjadi Universitas Pilihan Utama di Bidang Pendidikan dan Kewirausahaan.");
        faq.put("arti logo", "Logo Udinus melambangkan cinta kasih Tuhan (warna biru) yang memancarkan ilmu pengetahuan (lingkaran kuning emas) ke nusantara.");
        faq.put("struktur organisasi", "Udinus dipimpin oleh Rektor yang dibantu oleh 4 Wakil Rektor, Senat, Fakultas, Biro, dan UPT (Unit Pelaksana Teknis).");

        // --- Seputar Pendaftaran ---
        faq.put("pendaftaran", "Pendaftaran mahasiswa baru Udinus biasanya dibuka dalam 3 gelombang, dimulai sekitar bulan November hingga Agustus. Untuk tanggal pastinya, silakan cek situs web resmi pmb.dinus.ac.id.");
        faq.put("biaya kuliah", "Informasi rinci mengenai biaya kuliah untuk setiap program studi dapat dilihat di brosur penerimaan atau di situs web pmb.dinus.ac.id.");
        faq.put("syarat", "Syarat pendaftaran umum adalah lulusan SMA/SMK/sederajat, fotokopi ijazah, pas foto, dan mengisi formulir pendaftaran. Beberapa program studi mungkin memiliki syarat tambahan.");
        faq.put("brosur", "Brosur penerimaan mahasiswa baru dapat diunduh langsung dari halaman utama situs web pmb.dinus.ac.id.");

        // --- Seputar Akademik ---
        faq.put("fakultas", "Beberapa fakultas di Udinus: Fakultas Ilmu Komputer (FIK), Fakultas Ekonomi dan Bisnis (FEB), Fakultas Ilmu Budaya (FIB), Fakultas Kesehatan (FKES), dan Fakultas Teknik (FT), serta program Pascasarjana.");
        faq.put("rektor", "Rektor Universitas Dian Nuswantoro saat ini adalah Prof. Dr. Pulung Nurtantio Andono, S.T., M.Kom.");
        faq.put("kalender akademik", "Kalender akademik yang berisi jadwal perkuliahan, ujian, dan hari libur dapat diakses melalui portal mahasiswa di siadin.dinus.ac.id.");
        faq.put("apa itu sks", "SKS adalah singkatan dari Satuan Kredit Semester. Ini adalah bobot studi untuk setiap mata kuliah yang kamu ambil.");
        faq.put("krs", "Pengisian Kartu Rencana Studi (KRS) dilakukan secara online melalui portal Siadin di awal setiap semester sesuai jadwal yang ditentukan.");
        
        // --- Seputar Lokasi & Fasilitas ---
        faq.put("alamat", "Kampus utama Udinus berlokasi di Jl. Imam Bonjol No. 207, Pendrikan Kidul, Semarang, dan ada juga kampus di Jl. Nakula I No. 5-11, Semarang.");
        faq.put("perpus", "Perpustakaan pusat Udinus berada di gedung A, lantai 1. Jam operasionalnya adalah Senin-Jumat pukul 08:00 - 20:00 WIB.");
        
        // --- Seputar Kemahasiswaan ---
        faq.put("ukm", "Ada banyak Unit Kegiatan Mahasiswa (UKM) di Udinus, mulai dari bidang olahraga, seni, hingga penalaran. Daftar lengkapnya bisa dilihat di web kemahasiswaan.dinus.ac.id.");
        faq.put("beasiswa", "Udinus menyediakan berbagai macam beasiswa, seperti beasiswa prestasi akademik, beasiswa kurang mampu, dan beasiswa dari pihak eksternal. Info lengkap ada di bagian kemahasiswaan.");

        // --- Seputar Dosen (Data yang sudah diperbaiki sesuai data terbaru) ---
        faq.put("dosen otomata", "Dosen mata kuliah Otomata dan Teori Bahasa adalah ERWIN YUDI HIDAYAT S.Kom, M.CS.");
        faq.put("dosen jaringan komputer", "Dosen mata kuliah Jaringan Komputer adalah ADHITYA NUGRAHA S.Kom, M.CS.");
        faq.put("dosen pbo", "Dosen mata kuliah Pemrograman Berorientasi Objek (PBO) adalah DANNY OKA RATMANA M.Kom.");
        faq.put("dosen web lanjut", "Dosen mata kuliah Pemrograman Web Lanjut adalah APRILYANI NUR SAFITRI M.Kom.");
        faq.put("dosen pembelajaran mesin", "Dosen mata kuliah Pembelajaran Mesin adalah Dr. GURUH FAJAR SHIDIK S.Kom., M.Cs.");
        faq.put("dosen machine learning", "Dosen mata kuliah Pembelajaran Mesin (Machine Learning) adalah Dr. GURUH FAJAR SHIDIK S.Kom., M.Cs.");
        faq.put("dosen basis data", "Dosen mata kuliah Sistem Basis Data adalah DEFRI KURNIAWAN M.Kom.");
        faq.put("dosen logika digital", "Dosen mata kuliah Rangkaian Logika Digital adalah FILMADA OCKY SAPUTRA M.Eng.");
        faq.put("dosen rld", "Dosen mata kuliah Rangkaian Logika Digital adalah FILMADA OCKY SAPUTRA M.Eng.");
        faq.put("dosen metodologi penelitian", "Dosen mata kuliah Metodologi Penelitian adalah EGIA ROSI SUBHIYAKTO M.Kom.");
        faq.put("dosen metopen", "Dosen mata kuliah Metodologi Penelitian adalah EGIA ROSI SUBHIYAKTO M.Kom.");
        faq.put("dosen literasi informasi", "Dosen mata kuliah Literasi Informasi adalah ARDYTHA LUTHFIARTA M.Kom.");

        // --- Seputar Jadwal Kuliah ---
        faq.put("jadwal otomata", "Jadwal mata kuliah Otomata dan Teori Bahasa adalah setiap hari Selasa, pukul 12.30-15.00 di ruang H.4.12.");
        faq.put("jadwal jaringan komputer", "Jadwal mata kuliah Jaringan Komputer adalah setiap hari Selasa, pukul 15.30-18.00 di ruang H.4.8.");
        faq.put("jadwal pbo", "Mata kuliah Pemrograman Berorientasi Objek (PBO) memiliki dua jadwal: hari Rabu pukul 10.20-12.00 di ruang H.5.11, dan hari Jumat pukul 12.30-14.10 di ruang D.2.K.");
        faq.put("jadwal web lanjut", "Jadwal mata kuliah Pemrograman Web Lanjut adalah setiap hari Senin, pukul 15.30-17.10 di ruang H.6.1.");
        faq.put("jadwal pembelajaran mesin", "Jadwal mata kuliah Pembelajaran Mesin adalah setiap hari Rabu, pukul 12.30-15.00 di ruang H.5.3.");
        faq.put("jadwal machine learning", "Jadwal mata kuliah Pembelajaran Mesin (Machine Learning) adalah setiap hari Rabu, pukul 12.30-15.00 di ruang H.5.3.");
        faq.put("jadwal basis data", "Jadwal mata kuliah Sistem Basis Data adalah setiap hari Kamis, pukul 08.40-10.20 di ruang D.3.M.");
        faq.put("jadwal logika digital", "Jadwal mata kuliah Rangkaian Logika Digital adalah setiap hari Senin, pukul 12.30-15.00 di ruang H.5.4.");
        faq.put("jadwal rld", "Jadwal mata kuliah Rangkaian Logika Digital adalah setiap hari Senin, pukul 12.30-15.00 di ruang H.5.4.");
        faq.put("jadwal metodologi penelitian", "Jadwal mata kuliah Metodologi Penelitian adalah setiap hari Kamis, pukul 12.30-14.10 di ruang H.5.3.");
        faq.put("jadwal metopen", "Jadwal mata kuliah Metodologi Penelitian adalah setiap hari Kamis, pukul 12.30-14.10 di ruang H.5.3.");
        faq.put("jadwal literasi informasi", "Jadwal mata kuliah Literasi Informasi adalah setiap hari Jumat, pukul 08.40-10.20 di ruang Kulino.");
    }

    // Metode ini digunakan oleh kelas lain untuk bisa "melihat" isi databasenya.
    public static Map<String, String> getFaqData() {
        return faq;
    }
}