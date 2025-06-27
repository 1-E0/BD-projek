package com.example.bd.controller;

import com.example.bd.dao.HadiahDAO;
import com.example.bd.dao.MemberDAO;
import com.example.bd.model.Hadiah;
import com.example.bd.model.Member;
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

    private final MemberDAO memberDAO = new MemberDAO();
    private final HadiahDAO hadiahDAO = new HadiahDAO();
    private Member memberAktif;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupRiwayatTable();
        Pelanggan pelanggan = UserSession.getInstance().getLoggedInPelanggan();
        if (pelanggan != null) {
            memberAktif = memberDAO.getMemberByPelangganId(pelanggan.getIdPelanggan());
            if (memberAktif != null) {
                lblNamaMember.setText("Member: " + memberAktif.getNamaMember());
                updatePoinDanRiwayat();
            }
        }
        loadHadiah();
    }

    private void setupRiwayatTable() {
        colNamaHadiah.setCellValueFactory(new PropertyValueFactory<>("namaHadiah"));
        colPoinDigunakan.setCellValueFactory(new PropertyValueFactory<>("poinDigunakan"));
        colTanggalTukar.setCellValueFactory(new PropertyValueFactory<>("tanggalPenukaran"));
    }

    private void updatePoinDanRiwayat() {
        lblJumlahPoin.setText(String.valueOf(memberAktif.getJumlahPoin()));
        riwayatTable.setItems(FXCollections.observableArrayList(
                memberDAO.getRiwayatPenukaranByIdMember(memberAktif.getIdMember())
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

        // Menampilkan jenis hadiah
        Label jenisHadiahLabel = new Label(hadiah.getJenisHadiah());
        jenisHadiahLabel.getStyleClass().add("item-stock"); // Memakai style yang mirip

        // Menampilkan deskripsi jika ada
        Text deskripsiHadiah = new Text(hadiah.getDeskripsiHadiah());
        deskripsiHadiah.setWrappingWidth(170); // Agar teks tidak melebar

        Label poinHadiah = new Label(hadiah.getBiayaPoin() + " Poin");
        poinHadiah.getStyleClass().add("item-price");

        Button btnTukar = new Button("Tukar Poin");
        btnTukar.setPrefWidth(Double.MAX_VALUE);

        if (memberAktif == null || memberAktif.getJumlahPoin() < hadiah.getBiayaPoin()) {
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
                // Mengirim idPelanggan ke metode tukarPoin
                memberDAO.tukarPoin(memberAktif.getIdMember(), memberAktif.getIdPelanggan(), hadiah);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Penukaran hadiah berhasil!");

                memberAktif = memberDAO.getMemberByPelangganId(memberAktif.getIdPelanggan());
                updatePoinDanRiwayat();
                loadHadiah(); // Refresh daftar hadiah untuk update status tombol
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