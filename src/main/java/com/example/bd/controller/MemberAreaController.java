package com.example.bd.controller;

import com.example.bd.dao.PelangganDAO;
import com.example.bd.dao.VoucherDAO;
import com.example.bd.model.Pelanggan;
import com.example.bd.model.PelangganVoucher;
import com.example.bd.model.Voucher;
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
    @FXML private TilePane voucherTilePane;

    @FXML private TableView<PelangganVoucher> riwayatTable;
    @FXML private TableColumn<PelangganVoucher, String> colNamaVoucher;
    @FXML private TableColumn<PelangganVoucher, Integer> colPoinDigunakan;
    @FXML private TableColumn<PelangganVoucher, Date> colTanggalTukar;
    @FXML private TableColumn<PelangganVoucher, String> colStatusVoucher;

    private final PelangganDAO pelangganDAO = new PelangganDAO();
    private final VoucherDAO voucherDAO = new VoucherDAO();
    private Pelanggan pelangganAktif;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupRiwayatTable();
        pelangganAktif = UserSession.getInstance().getLoggedInPelanggan();
        if (pelangganAktif != null) {
            lblNamaMember.setText("Member: " + pelangganAktif.getNamaPelanggan());
            updatePoinDanRiwayat();
        }
        loadVoucher();
    }

    private void setupRiwayatTable() {
        colNamaVoucher.setCellValueFactory(new PropertyValueFactory<>("namaVoucher"));
        colPoinDigunakan.setCellValueFactory(new PropertyValueFactory<>("poinDigunakan"));
        colTanggalTukar.setCellValueFactory(new PropertyValueFactory<>("tanggalPenukaran"));
        colStatusVoucher.setCellValueFactory(new PropertyValueFactory<>("statusVoucher"));
    }

    private void updatePoinDanRiwayat() {
        lblJumlahPoin.setText(String.valueOf(pelangganAktif.getJumlahPoin()));
        riwayatTable.setItems(FXCollections.observableArrayList(
                pelangganDAO.getRiwayatPenukaranVoucher(pelangganAktif.getIdPelanggan())
        ));
    }

    private void loadVoucher() {
        voucherTilePane.getChildren().clear();
        for (Voucher voucher : voucherDAO.getAllVoucherAktif()) {
            VBox card = createVoucherCard(voucher);
            voucherTilePane.getChildren().add(card);
        }
    }

    private VBox createVoucherCard(Voucher voucher) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");
        card.setPrefWidth(200);

        Label namaVoucher = new Label(voucher.getNamaVoucher());
        namaVoucher.getStyleClass().add("item-name");
        namaVoucher.setWrapText(true);

        Text deskripsiVoucher = new Text(voucher.getDeskripsi());
        deskripsiVoucher.setWrappingWidth(170);

        Label poinVoucher = new Label(voucher.getPoinDibutuhkan() + " Poin");
        poinVoucher.getStyleClass().add("item-price");

        Button btnTukar = new Button("Tukar Poin");
        btnTukar.setPrefWidth(Double.MAX_VALUE);

        if (pelangganAktif == null || pelangganAktif.getJumlahPoin() < voucher.getPoinDibutuhkan()) {
            btnTukar.setDisable(true);
        }

        btnTukar.setOnAction(event -> handleTukarPoin(voucher));

        card.getChildren().addAll(namaVoucher, deskripsiVoucher, poinVoucher, btnTukar);
        return card;
    }

    private void handleTukarPoin(Voucher voucher) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Tukarkan " + voucher.getPoinDibutuhkan() + " poin dengan voucher '" + voucher.getNamaVoucher() + "'?",
                ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText("Konfirmasi Penukaran Poin");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                // Metode DAO sekarang mengembalikan boolean
                boolean sukses = pelangganDAO.tukarPoinDenganVoucher(pelangganAktif.getIdPelanggan(), voucher);

                if (sukses) {
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Penukaran voucher berhasil!");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Gagal", "Poin Anda tidak mencukupi untuk menukar voucher ini.");
                }

                // Selalu refresh data setelah mencoba transaksi
                Pelanggan pelangganTerbaru = pelangganDAO.getPelangganById(pelangganAktif.getIdPelanggan());
                if (pelangganTerbaru != null) {
                    UserSession.getInstance().setLoggedInPelanggan(pelangganTerbaru);
                    pelangganAktif = pelangganTerbaru;
                }

                updatePoinDanRiwayat();
                loadVoucher();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menukarkan poin karena terjadi masalah pada database.");
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