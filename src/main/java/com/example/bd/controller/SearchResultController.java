package com.example.bd.controller;

import com.example.bd.dao.MenuDAO;
import com.example.bd.model.Menu; // <-- Import ini penting untuk mengatasi ambiguitas
import com.example.bd.model.MenuHarian;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SearchResultController {

    @FXML private Label lblSearchResult;
    @FXML private Label lblJumlahHasil;
    @FXML private FlowPane menuFlowPane;

    private final MenuDAO menuDAO = new MenuDAO();
    // Asumsi cabang default
    private static final int ID_CABANG_PADRAO = 1;

    public void initializeData(String keyword) {
        lblSearchResult.setText("Hasil Pencarian untuk: \"" + keyword + "\"");
        loadMenuData(keyword);
    }

    private void loadMenuData(String keyword) {
        menuFlowPane.getChildren().clear();
        // Memanggil metode pencarian baru
        List<MenuHarian> resultList = menuDAO.searchMenuHarian(keyword, ID_CABANG_PADRAO);

        lblJumlahHasil.setText("Ditemukan " + resultList.size() + " hasil");

        for (MenuHarian menuHarian : resultList) {
            VBox card = createMenuCard(menuHarian);
            menuFlowPane.getChildren().add(card);
        }
    }

    // Metode ini sekarang menerima MenuHarian
    private VBox createMenuCard(MenuHarian menuHarian) {
        VBox card = new VBox(8);
        card.getStyleClass().add("item-card");
        card.setPrefWidth(200);

        Label namaMenu = new Label(menuHarian.getNamaMenu());
        namaMenu.getStyleClass().add("item-name");
        namaMenu.setWrapText(true);

        Label hargaMenu = new Label(String.format("Rp %.0f", menuHarian.getHargaMenu()));
        hargaMenu.getStyleClass().add("item-price");

        Button btnTambah = new Button("Tambah ke Keranjang");
        btnTambah.setPrefWidth(Double.MAX_VALUE);
        btnTambah.setOnAction(event -> handleTambahKeKeranjang(menuHarian));

        card.getChildren().addAll(namaMenu, hargaMenu, btnTambah);
        return card;
    }

    // Metode ini juga sekarang menerima MenuHarian
    private void handleTambahKeKeranjang(MenuHarian menuHarian) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Input Kuantitas");
        dialog.setHeaderText("Masukkan jumlah untuk: " + menuHarian.getNamaMenu());
        dialog.setContentText("Jumlah:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(qtyStr -> {
            try {
                int kuantitas = Integer.parseInt(qtyStr);
                if (kuantitas <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Kuantitas harus lebih dari 0.");
                    return;
                }
                if (kuantitas > menuHarian.getStokMenuHarian()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Jumlah melebihi stok yang tersedia (" + menuHarian.getStokMenuHarian() + ").");
                    return;
                }

                // Mengambil menu dasar dan memanggil addItemToCart dengan 3 argumen
                Menu menuBase = menuDAO.getMenuById(menuHarian.getIdMenu());
                UserSession.getInstance().addItemToCart(menuBase, kuantitas, menuHarian.getIdMenuHarian());
                showAlert(Alert.AlertType.INFORMATION, "Sukses", menuHarian.getNamaMenu() + " telah ditambahkan ke keranjang.");
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