package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.MenuDAO;
import com.example.bd.model.Menu;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.List;
import java.util.Optional;

public class SearchResultController {

    @FXML private Label lblSearchResult;
    @FXML private Label lblJumlahHasil;
    @FXML private FlowPane menuFlowPane;

    private final MenuDAO menuDAO = new MenuDAO();

    // Method ini akan dipanggil dari Dashboard untuk mengirim kata kunci pencarian
    public void initializeData(String keyword) {
        lblSearchResult.setText("Hasil Pencarian untuk: \"" + keyword + "\"");
        loadMenuData(keyword);
    }

    private void loadMenuData(String keyword) {
        menuFlowPane.getChildren().clear();
        List<Menu> resultList = menuDAO.searchMenu(keyword);

        lblJumlahHasil.setText("Ditemukan " + resultList.size() + " hasil");

        for (Menu menu : resultList) {
            VBox card = createMenuCard(menu);
            menuFlowPane.getChildren().add(card);
        }
    }

    // Method ini sama persis dengan yang ada di MenuListByCategoryController
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