package com.example.bd.controller;

import com.example.bd.dao.HadiahDAO;
import com.example.bd.dao.PelangganDAO;
import com.example.bd.model.Hadiah;
import com.example.bd.model.Pelanggan;
import com.example.bd.model.RiwayatPenukaran;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MemberAreaController implements Initializable {

    @FXML private Label lblNamaMember;
    @FXML private Label lblJumlahPoin;
    @FXML private TilePane hadiahTilePane;

    @FXML private TableView<RiwayatPenukaran> riwayatTable;
    @FXML private TableColumn<RiwayatPenukaran, String> colNamaHadiah;
    @FXML private TableColumn<RiwayatPenukaran, Integer> colPoinDigunakan;
    @FXML private TableColumn<RiwayatPenukaran, Date> colTanggalTukar;

    private final PelangganDAO pelangganDAO = new PelangganDAO();
    private final HadiahDAO hadiahDAO = new HadiahDAO();
    private Pelanggan pelangganAktif;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupRiwayatTable();
        pelangganAktif = UserSession.getInstance().getLoggedInPelanggan();
        if (pelangganAktif != null) {
            lblNamaMember.setText("Member: " + pelangganAktif.getNamaPelanggan());
            updatePoinDanRiwayat();
        }
        loadHadiah();
    }

    private void setupRiwayatTable() {
        colNamaHadiah.setCellValueFactory(new PropertyValueFactory<>("namaHadiah"));
        colPoinDigunakan.setCellValueFactory(new PropertyValueFactory<>("poinDigunakan"));
        colTanggalTukar.setCellValueFactory(new PropertyValueFactory<>("tanggalPenukaran"));
    }

    private void updatePoinDanRiwayat() {
        lblJumlahPoin.setText(String.valueOf(pelangganAktif.getJumlahPoin()));
        riwayatTable.setItems(FXCollections.observableArrayList(
                pelangganDAO.getRiwayatPenukaran(pelangganAktif.getIdPelanggan())
        ));
    }

    private void loadHadiah() {
        hadiahTilePane.getChildren().clear();
        for (Hadiah hadiah : hadiahDAO.getAllHadiah()) {
            VBox card = createHadiahCard(hadiah);
            hadiahTilePane.getChildren().add(card);
        }
    }

    private VBox createHadiahCard(Hadiah hadiah) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setPrefWidth(200);

        Label namaHadiah = new Label(hadiah.getNamaHadiah());
        namaHadiah.getStyleClass().add("item-name");
        namaHadiah.setWrapText(true);

        Label jenisHadiahLabel = new Label(hadiah.getJenisHadiah());
        jenisHadiahLabel.getStyleClass().add("item-stock");

        Text deskripsiHadiah = new Text(hadiah.getDeskripsiHadiah());
        deskripsiHadiah.setWrappingWidth(170);

        Label poinHadiah = new Label(hadiah.getBiayaPoin() + " Poin");
        poinHadiah.getStyleClass().add("item-price");

        Button btnTukar = new Button("Tukar Poin");
        btnTukar.setPrefWidth(Double.MAX_VALUE);

        if (pelangganAktif == null || pelangganAktif.getJumlahPoin() < hadiah.getBiayaPoin()) {
            btnTukar.setDisable(true);
        }

        btnTukar.setOnAction(event -> handleTukarPoin(hadiah));

        card.getChildren().addAll(namaHadiah, jenisHadiahLabel, deskripsiHadiah, poinHadiah, btnTukar);
        return card;
    }

    private void handleTukarPoin(Hadiah hadiah) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Tukarkan " + hadiah.getBiayaPoin() + " poin dengan " + hadiah.getNamaHadiah() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText("Konfirmasi Penukaran Poin");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                pelangganDAO.tukarPoin(pelangganAktif.getIdPelanggan(), hadiah);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Penukaran hadiah berhasil!");

                // Refresh data pelanggan dari database setelah tukar poin
                Pelanggan pelangganTerbaru = pelangganDAO.validateLogin(pelangganAktif.getEmailPelanggan(), pelangganAktif.getPasswordPelanggan());
                if (pelangganTerbaru != null) {
                    UserSession.getInstance().setLoggedInPelanggan(pelangganTerbaru);
                    pelangganAktif = pelangganTerbaru; // Update pelanggan aktif di controller ini
                }

                updatePoinDanRiwayat();
                loadHadiah();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menukarkan poin.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleKembali(ActionEvent event) {
        Navigasi.goBack(event, "PelangganDashboardView.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}