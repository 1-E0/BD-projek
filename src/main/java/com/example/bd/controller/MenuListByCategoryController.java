package com.example.bd.controller;

import com.example.bd.dao.FavoritDAO; // <-- IMPORT BARU
import com.example.bd.dao.MenuDAO;
import com.example.bd.model.Kategori;
import com.example.bd.model.Menu;
import com.example.bd.model.MenuHarian;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView; // <-- IMPORT BARU
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox; // <-- IMPORT BARU
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Optional;
import java.util.Set; // <-- IMPORT BARU

public class MenuListByCategoryController {

    @FXML private Label lblNamaKategori;
    @FXML private FlowPane menuFlowPane;

    private final MenuDAO menuDAO = new MenuDAO();
    private final FavoritDAO favoritDAO = new FavoritDAO(); // <-- DAO BARU
    private Set<Integer> favoritIds; // <-- SET UNTUK MENYIMPAN ID FAVORIT
    private static final int ID_CABANG_PADRAO = 1;

    public void initializeData(Kategori kategori) {
        // Muat daftar favorit pelanggan saat halaman dibuka
        int idPelanggan = UserSession.getInstance().getLoggedInPelanggan().getIdPelanggan();
        this.favoritIds = favoritDAO.getFavoritIdsByPelanggan(idPelanggan);

        lblNamaKategori.setText("Kategori: " + kategori.getNamaKategori());
        loadMenuData(kategori.getIdKategori());
    }

    private void loadMenuData(int idKategori) {
        menuFlowPane.getChildren().clear();
        for (MenuHarian menuHarian : menuDAO.getMenuHarianByKategori(idKategori, ID_CABANG_PADRAO)) {
            VBox card = createMenuCard(menuHarian);
            menuFlowPane.getChildren().add(card);
        }
    }

    private VBox createMenuCard(MenuHarian menuHarian) {
        VBox card = new VBox(8);
        card.getStyleClass().add("item-card");
        card.setPrefWidth(200);

        // --- TOMBOL FAVORIT ---
        ToggleButton favoriteButton = new ToggleButton();
        FontAwesomeIconView heartIcon = new FontAwesomeIconView();
        heartIcon.setGlyphName("HEART");
        favoriteButton.setGraphic(heartIcon);
        favoriteButton.getStyleClass().add("favorite-button");

        // Set status tombol berdasarkan daftar favorit
        if (favoritIds.contains(menuHarian.getIdMenu())) {
            favoriteButton.setSelected(true);
        }

        favoriteButton.setOnAction(e -> {
            int idPelanggan = UserSession.getInstance().getLoggedInPelanggan().getIdPelanggan();
            if (favoriteButton.isSelected()) {
                favoritDAO.addFavorit(idPelanggan, menuHarian.getIdMenu());
                favoritIds.add(menuHarian.getIdMenu());
            } else {
                favoritDAO.removeFavorit(idPelanggan, menuHarian.getIdMenu());
                favoritIds.remove(menuHarian.getIdMenu());
            }
        });

        HBox cardHeader = new HBox();
        cardHeader.setAlignment(Pos.CENTER_LEFT);
        Label namaMenu = new Label(menuHarian.getNamaMenu());
        namaMenu.getStyleClass().add("item-name");
        namaMenu.setWrapText(true);
        HBox.setHgrow(namaMenu, javafx.scene.layout.Priority.ALWAYS);
        cardHeader.getChildren().addAll(namaMenu, favoriteButton);
        // --- AKHIR TOMBOL FAVORIT ---


        Label hargaMenu = new Label(String.format("Rp %.0f", menuHarian.getHargaMenu()));
        hargaMenu.getStyleClass().add("item-price");

        Label stokLabel = new Label("Stok: " + menuHarian.getStokMenuHarian());
        stokLabel.getStyleClass().add("item-stock");

        Button btnTambah = new Button("Tambah ke Keranjang");
        btnTambah.setPrefWidth(Double.MAX_VALUE);
        btnTambah.setOnAction(event -> handleTambahKeKeranjang(menuHarian));

        card.getChildren().addAll(cardHeader, hargaMenu, stokLabel, btnTambah);
        return card;
    }

    // ... (sisa kode controller tidak berubah, salin dari kode Anda yang sudah ada)
    private void handleTambahKeKeranjang(MenuHarian menuHarian) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Input Kuantitas");
        dialog.setHeaderText("Masukkan jumlah untuk: " + menuHarian.getNamaMenu());
        dialog.setContentText("Jumlah:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(qtyStr -> {
            try {
                int kuantitasDiminta = Integer.parseInt(qtyStr);
                if (kuantitasDiminta <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Kuantitas harus lebih dari 0.");
                    return;
                }
                int stokTersedia = menuHarian.getStokMenuHarian();
                int kuantitasDiKeranjang = 0;
                for (var item : UserSession.getInstance().getKeranjang()) {
                    if (item.getIdMenuHarian() == menuHarian.getIdMenuHarian()) {
                        kuantitasDiKeranjang = item.getKuantitas();
                        break;
                    }
                }
                if (kuantitasDiminta + kuantitasDiKeranjang > stokTersedia) {
                    showAlert(Alert.AlertType.ERROR, "Stok Tidak Cukup",
                            "Jumlah melebihi stok yang tersedia.\nStok tersedia: " + stokTersedia +
                                    "\nSudah ada di keranjang: " + kuantitasDiKeranjang);
                    return;
                }
                Menu menuBase = menuDAO.getMenuById(menuHarian.getIdMenu());
                UserSession.getInstance().addItemToCart(menuBase, kuantitasDiminta, menuHarian.getIdMenuHarian());
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