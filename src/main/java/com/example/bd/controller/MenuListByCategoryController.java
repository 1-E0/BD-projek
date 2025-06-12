package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.MenuDAO;
import com.example.bd.model.Kategori;
import com.example.bd.model.Menu;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class MenuListByCategoryController {

    @FXML private Label lblNamaKategori;
    @FXML private FlowPane menuFlowPane;

    private final MenuDAO menuDAO = new MenuDAO();

    public void initializeData(Kategori kategori) {
        lblNamaKategori.setText("Kategori: " + kategori.getNamaKategori());
        loadMenuData(kategori.getIdKategori());
    }

    private void loadMenuData(int idKategori) {
        menuFlowPane.getChildren().clear();
        for (Menu menu : menuDAO.getMenuByKategori(idKategori)) {
            VBox card = createMenuCard(menu);
            menuFlowPane.getChildren().add(card);
        }
    }

    private VBox createMenuCard(Menu menu) {
        VBox card = new VBox(8);
        card.getStyleClass().add("item-card");
        card.setPrefWidth(200);

        Label namaMenu = new Label(menu.getNamaMenu());
        namaMenu.getStyleClass().add("item-name");
        namaMenu.setWrapText(true);

        Label hargaMenu = new Label(String.format("Rp %.0f", menu.getHargaMenu()));
        hargaMenu.getStyleClass().add("item-price");

        Button btnTambah = new Button("Tambah ke Keranjang");
        btnTambah.setPrefWidth(Double.MAX_VALUE);

        // === LOGIKA BARU SAAT TOMBOL TAMBAH DIKLIK ===
        btnTambah.setOnAction(event -> handleTambahKeKeranjang(menu));

        card.getChildren().addAll(namaMenu, hargaMenu, btnTambah);
        return card;
    }

    private void handleTambahKeKeranjang(Menu menu) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Input Kuantitas");
        dialog.setHeaderText("Masukkan jumlah untuk: " + menu.getNamaMenu());
        dialog.setContentText("Jumlah:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(qtyStr -> {
            try {
                int kuantitas = Integer.parseInt(qtyStr);
                if (kuantitas <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Kuantitas harus lebih dari 0.");
                    return;
                }
                // Menambahkan item ke keranjang global via UserSession
                UserSession.getInstance().addItemToCart(menu, kuantitas);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", menu.getNamaMenu() + " telah ditambahkan ke keranjang.");

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Kuantitas harus berupa angka.");
            }
        });
    }

    @FXML
    private void handleKembali(ActionEvent event) throws IOException {
        Navigasi.goBack(event, "PelangganDashboardView.fxml");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}