package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.KategoriDAO;

import com.example.bd.model.Kategori;

import com.example.bd.model.Pelanggan;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PelangganDashboardController implements Initializable {

    @FXML private Label lblWelcome;
    @FXML private FlowPane kategoriFlowPane;
    @FXML private TextField txtSearch;

    private final KategoriDAO kategoriDAO = new KategoriDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pelanggan pelanggan = UserSession.getInstance().getLoggedInPelanggan();
        if(pelanggan != null){
            String namaDepan = pelanggan.getNamaPelanggan().split(" ")[0];
            lblWelcome.setText("Selamat Pagi, " + namaDepan + "!");
        }
        loadKategori();
    }


    private void loadKategori() {
        kategoriFlowPane.getChildren().clear();
        for (Kategori kategori : kategoriDAO.getAllKategori()) {
            VBox card = createKategoriCard(kategori);
            kategoriFlowPane.getChildren().add(card);
        }
    }

    // === PERBAIKAN UTAMA ADA DI METHOD INI ===
    private VBox createKategoriCard(Kategori kategori) {
        VBox card = new VBox(10);
        card.getStyleClass().add("category-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(140, 120);

        FontAwesomeIconView icon = new FontAwesomeIconView();
        icon.setSize("3em");

        String namaLower = kategori.getNamaKategori().toLowerCase();
        String warnaIkon;

        // Atur ikon dan warna berdasarkan nama kategori
        if (namaLower.contains("minum")) {
            icon.setGlyphName("COFFEE");
            warnaIkon = "#8D6E63"; // Coklat
        } else if (namaLower.contains("dessert") || namaLower.contains("snack")) {
            icon.setGlyphName("BITBUCKET");
            warnaIkon = "#FF8A65"; // Oranye muda
        } else if (namaLower.contains("paket")) {
            icon.setGlyphName("ARCHIVE");
            warnaIkon = "#42A5F5"; // Biru
        } else { // Makanan Utama
            icon.setGlyphName("CUTLERY");
            warnaIkon = "#EF5350"; // Merah
        }

        // Terapkan warna ke ikon secara langsung
        icon.setStyle("-fx-fill: " + warnaIkon + ";");

        Label namaKategori = new Label(kategori.getNamaKategori());
        namaKategori.setWrapText(true);
        namaKategori.setAlignment(Pos.CENTER);

        card.getChildren().addAll(icon, namaKategori);
        card.setOnMouseClicked(event -> handleKategoriClick(event, kategori));
        return card;
    }

    private void handleKategoriClick(MouseEvent event, Kategori kategori) {
        try {
            // 1. Dapatkan Scene saat ini
            Node source = (Node) event.getSource();
            Scene scene = source.getScene();

            // 2. Muat FXML baru DAN DAPATKAN CONTROLLER-nya
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("view/MenuListByCategoryView.fxml"));
            Parent newRoot = loader.load();

            // 3. SEBELUM PINDAH, kirim data ke controller yang baru dimuat
            MenuListByCategoryController controller = loader.getController();
            controller.initializeData(kategori);

            // 4. Lakukan animasi transisi secara manual
            double sceneWidth = scene.getWidth();
            newRoot.setTranslateX(sceneWidth); // Posisikan di luar layar kanan
            scene.setRoot(newRoot); // Ganti konten scene

            // Mainkan animasi slide-in
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(350), newRoot);
            slideIn.setToX(0);
            slideIn.setInterpolator(Interpolator.EASE_OUT);
            slideIn.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleSearch(ActionEvent event) {
        String keyword = txtSearch.getText();
        if (keyword == null || keyword.trim().isEmpty()) {
            return; // Jangan lakukan apa-apa jika search bar kosong
        }

        try {
            // 1. Dapatkan Scene saat ini
            Node source = (Node) event.getSource();
            Scene scene = source.getScene();

            // 2. Muat FXML baru DAN DAPATKAN CONTROLLER-nya
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("view/SearchResultView.fxml"));
            Parent newRoot = loader.load();

            // 3. SEBELUM PINDAH, kirim data (kata kunci) ke controller baru
            SearchResultController controller = loader.getController();
            controller.initializeData(keyword);

            // 4. Lakukan animasi transisi secara manual
            double sceneWidth = scene.getWidth();
            newRoot.setTranslateX(sceneWidth); // Posisikan di luar layar kanan
            scene.setRoot(newRoot); // Ganti konten scene

            // Mainkan animasi slide-in
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(350), newRoot);
            slideIn.setToX(0);
            slideIn.setInterpolator(Interpolator.EASE_OUT);
            slideIn.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToBuatPesanan(ActionEvent event) {
        Navigasi.switchScene(event, "BuatPesananView.fxml");
    }

    @FXML
    void goToRiwayatPesanan(ActionEvent event) {
        Navigasi.switchScene(event, "RiwayatPesananView.fxml");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        UserSession.getInstance().cleanUserSession();
        Navigasi.goBack(event, "LoginView.fxml");
    }
    // Di dalam kelas PelangganDashboardController
    @FXML
    void goToEditProfil(ActionEvent event) {
        Navigasi.switchScene(event, "EditProfilView.fxml");
    }
}