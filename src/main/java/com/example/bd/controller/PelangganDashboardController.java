package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.FavoritDAO;
import com.example.bd.dao.KategoriDAO;
import com.example.bd.dao.MenuDAO;
import com.example.bd.model.ItemKeranjang;
import com.example.bd.model.Kategori;
import com.example.bd.model.Menu;
import com.example.bd.model.MenuHarian;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PelangganDashboardController implements Initializable {

    @FXML private Label lblWelcome;
    @FXML private FlowPane kategoriFlowPane;
    @FXML private FlowPane favoritFlowPane;
    @FXML private VBox favoritContainer;
    @FXML private TextField txtSearch;

    private final KategoriDAO kategoriDAO = new KategoriDAO();
    private final FavoritDAO favoritDAO = new FavoritDAO();
    private final MenuDAO menuDAO = new MenuDAO();
    private static final int ID_CABANG_PADRAO = 1; // Asumsi cabang default

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pelanggan pelanggan = UserSession.getInstance().getLoggedInPelanggan();
        if (pelanggan != null) {
            String namaDepan = pelanggan.getNamaPelanggan().split(" ")[0];
            lblWelcome.setText("Selamat Pagi, " + namaDepan + "!");
        }
        loadKategori();
        loadFavorit();
    }

    private void loadKategori() {
        kategoriFlowPane.getChildren().clear();
        for (Kategori kategori : kategoriDAO.getAllKategori()) {
            VBox card = createKategoriCard(kategori);
            kategoriFlowPane.getChildren().add(card);
        }
    }

    private void loadFavorit() {
        favoritFlowPane.getChildren().clear();
        int idPelanggan = UserSession.getInstance().getLoggedInPelanggan().getIdPelanggan();
        List<MenuHarian> favoritList = favoritDAO.getFavoritMenuHarianByPelanggan(idPelanggan, ID_CABANG_PADRAO);

        favoritContainer.setVisible(!favoritList.isEmpty());
        favoritContainer.setManaged(!favoritList.isEmpty());

        for (MenuHarian menuHarian : favoritList) {
            favoritFlowPane.getChildren().add(createFavoritCard(menuHarian));
        }
    }

    private VBox createKategoriCard(Kategori kategori) {
        VBox card = new VBox(10);
        card.getStyleClass().add("category-card");
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(140, 120);

        FontAwesomeIconView icon = new FontAwesomeIconView();
        String namaLower = kategori.getNamaKategori().toLowerCase();
        String warnaIkon;

        if (namaLower.contains("minum")) {
            icon.setGlyphName("COFFEE");
            warnaIkon = "#8D6E63";
        } else if (namaLower.contains("dessert") || namaLower.contains("snack")) {
            icon.setGlyphName("BITBUCKET");
            warnaIkon = "#FF8A65";
        } else if (namaLower.contains("paket")) {
            icon.setGlyphName("ARCHIVE");
            warnaIkon = "#42A5F5";
        } else {
            icon.setGlyphName("CUTLERY");
            warnaIkon = "#EF5350";
        }

        icon.setStyle("-fx-font-size: 40px; -fx-fill: " + warnaIkon + ";");
        Label namaKategori = new Label(kategori.getNamaKategori());
        namaKategori.setWrapText(true);
        namaKategori.setAlignment(Pos.CENTER);

        card.getChildren().addAll(icon, namaKategori);
        card.setOnMouseClicked(event -> handleKategoriClick(event, kategori));
        return card;
    }

    private VBox createFavoritCard(MenuHarian menuHarian) {
        VBox card = new VBox(8);
        card.getStyleClass().add("favorit-item-card");
        card.setPrefWidth(220);

        ToggleButton favoriteButton = new ToggleButton();
        FontAwesomeIconView heartIcon = new FontAwesomeIconView();
        heartIcon.setGlyphName("HEART");
        favoriteButton.setGraphic(heartIcon);
        favoriteButton.getStyleClass().add("favorite-button");
        favoriteButton.setSelected(true); // Selalu terpilih karena ini daftar favorit

        favoriteButton.setOnAction(e -> handleUnfavorite(menuHarian));

        HBox cardHeader = new HBox();
        cardHeader.setAlignment(Pos.CENTER_LEFT);
        Label namaMenu = new Label(menuHarian.getNamaMenu());
        namaMenu.getStyleClass().add("item-name");
        HBox.setHgrow(namaMenu, javafx.scene.layout.Priority.ALWAYS);
        cardHeader.getChildren().addAll(namaMenu, favoriteButton);

        Label hargaMenu = new Label(String.format("Rp %.0f", menuHarian.getHargaMenu()));
        hargaMenu.getStyleClass().add("item-price");

        Label stokLabel = new Label("Sisa Stok: " + menuHarian.getStokMenuHarian());
        stokLabel.getStyleClass().add("item-stock");

        Button btnTambah = new Button("Tambah ke Keranjang");
        btnTambah.setPrefWidth(Double.MAX_VALUE);
        btnTambah.setOnAction(event -> handleTambahFavoritKeKeranjang(menuHarian));

        card.getChildren().addAll(cardHeader, hargaMenu, stokLabel, btnTambah);
        return card;
    }

    private void handleUnfavorite(MenuHarian menuHarian) {
        int idPelanggan = UserSession.getInstance().getLoggedInPelanggan().getIdPelanggan();
        favoritDAO.removeFavorit(idPelanggan, menuHarian.getIdMenu());
        loadFavorit(); // Langsung refresh daftar favorit
    }

    private void handleTambahFavoritKeKeranjang(MenuHarian menuHarian) {
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
                for (ItemKeranjang item : UserSession.getInstance().getKeranjang()) {
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

    private void handleKategoriClick(MouseEvent event, Kategori kategori) {
        try {
            Node source = (Node) event.getSource();
            Scene scene = source.getScene();
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("view/MenuListByCategoryView.fxml"));
            Parent newRoot = loader.load();
            MenuListByCategoryController controller = loader.getController();
            controller.initializeData(kategori);
            double sceneWidth = scene.getWidth();
            newRoot.setTranslateX(sceneWidth);
            scene.setRoot(newRoot);
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
            return;
        }
        try {
            Node source = (Node) event.getSource();
            Scene scene = source.getScene();
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("view/SearchResultView.fxml"));
            Parent newRoot = loader.load();
            SearchResultController controller = loader.getController();
            controller.initializeData(keyword);
            double sceneWidth = scene.getWidth();
            newRoot.setTranslateX(sceneWidth);
            scene.setRoot(newRoot);
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
    void goToMemberArea(ActionEvent event) {
        Navigasi.switchScene(event, "MemberAreaView.fxml");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        UserSession.getInstance().cleanUserSession();
        Navigasi.goBack(event, "LoginView.fxml");
    }

    @FXML
    void goToEditProfil(ActionEvent event) {
        Navigasi.switchScene(event, "EditProfilView.fxml");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}