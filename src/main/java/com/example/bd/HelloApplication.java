package com.example.bd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // === PENGATURAN WINDOW UTAMA DI SINI ===

        // 1. Menambahkan Logo/Ikon pada Jendela Aplikasi
        try {
            Image appIcon = new Image(HelloApplication.class.getResourceAsStream("images/chef.png"));
            stage.getIcons().add(appIcon);
        } catch (Exception e) {
            System.err.println("Gagal memuat ikon aplikasi: " + e.getMessage());
        }

        // 2. Mengatur Judul Default Window
        stage.setTitle("Aplikasi Catering");

        // 3. Membuat Ukuran Window Tetap (Tidak Bisa Di-resize)
        //changes\




        


        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}