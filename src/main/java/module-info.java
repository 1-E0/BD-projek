module com.example.bd {
    // Modul-modul JavaFX yang dibutuhkan
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Modul untuk koneksi database
    requires java.sql;

    // !! PERUBAHAN PENTING ADA DI SINI !!
    // Modul untuk library ikon FontAwesomeFX
    requires de.jensd.fx.glyphs.fontawesome;

    // Memberi izin FXML untuk mengakses controller
    opens com.example.bd.controller to javafx.fxml;

    // Memberi izin TableView untuk mengakses model
    opens com.example.bd.model to javafx.base;

    // Mengekspor package utama agar aplikasi bisa berjalan
    exports com.example.bd;
}