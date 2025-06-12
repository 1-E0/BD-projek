package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.PengirimanDAO;
import com.example.bd.dao.PesananDAO;
import com.example.bd.model.DetailPesanan;
import com.example.bd.model.Pesanan;
import com.example.bd.util.Navigasi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class ManajemenPesananController implements Initializable {

    // FXML Components
    @FXML private TableView<Pesanan> pesananTable;
    @FXML private TableColumn<Pesanan, Integer> colIdPesanan;
    @FXML private TableColumn<Pesanan, String> colNamaPelanggan;
    @FXML private TableColumn<Pesanan, Timestamp> colTanggal;
    @FXML private TableColumn<Pesanan, Double> colTotal;
    @FXML private TableColumn<Pesanan, String> colStatusBayar;
    @FXML private TableColumn<Pesanan, String> colStatusKirim;

    @FXML private TableView<DetailPesanan> detailTable;
    @FXML private TableColumn<DetailPesanan, String> colNamaItem;
    @FXML private TableColumn<DetailPesanan, Integer> colKuantitas;
    @FXML private TableColumn<DetailPesanan, Double> colHargaSatuan;

    @FXML private VBox panelAksi;
    @FXML private ComboBox<String> comboStatusBayar;
    @FXML private ComboBox<String> comboStatusKirim;
    @FXML private TextField txtCatatanKirim;

    // DAOs
    private final PesananDAO pesananDAO = new PesananDAO();
    private final PengirimanDAO pengirimanDAO = new PengirimanDAO();

    private Pesanan pesananTerpilih;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupPesananTable();
        setupDetailTable();
        loadPesananData();

        // Listener untuk klik pada tabel pesanan
        pesananTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    pesananTerpilih = newSelection;
                    if (newSelection != null) {
                        panelAksi.setDisable(false); // Aktifkan panel aksi
                        loadDetailData(newSelection.getIdPesanan());
                        // Tampilkan status saat ini di ComboBox
                        comboStatusBayar.setValue(newSelection.getStatusPembayaran());
                        comboStatusKirim.setValue(newSelection.getStatusPengiriman());
                    } else {
                        // Jika tidak ada yang dipilih, nonaktifkan panel dan bersihkan
                        panelAksi.setDisable(true);
                        detailTable.getItems().clear();
                        comboStatusBayar.setValue(null);
                        comboStatusKirim.setValue(null);
                        txtCatatanKirim.clear();
                    }
                });

        // Isi pilihan untuk ComboBox Status
        comboStatusBayar.setItems(FXCollections.observableArrayList("Belum Lunas", "Lunas", "Dibatalkan"));
        comboStatusKirim.setItems(FXCollections.observableArrayList("Sedang Disiapkan", "Dalam Perjalanan", "Tiba di Tujuan"));
    }

    private void setupPesananTable() {
        colIdPesanan.setCellValueFactory(new PropertyValueFactory<>("idPesanan"));
        colNamaPelanggan.setCellValueFactory(new PropertyValueFactory<>("namaPelanggan"));
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalPesanan"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalHargaPesanan"));
        colStatusBayar.setCellValueFactory(new PropertyValueFactory<>("statusPembayaran"));
        colStatusKirim.setCellValueFactory(new PropertyValueFactory<>("statusPengiriman"));
    }

    private void setupDetailTable() {
        colNamaItem.setCellValueFactory(new PropertyValueFactory<>("namaMenu"));
        colKuantitas.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));
        colHargaSatuan.setCellValueFactory(new PropertyValueFactory<>("hargaProduk"));
    }

    private void loadPesananData() {
        ObservableList<Pesanan> pesananList = FXCollections.observableArrayList(pesananDAO.getAllPesanan());
        pesananTable.setItems(pesananList);
    }

    private void loadDetailData(int idPesanan) {
        ObservableList<DetailPesanan> detailList = FXCollections.observableArrayList(pesananDAO.getDetailByPesanan(idPesanan));
        detailTable.setItems(detailList);
    }

    @FXML
    private void handleUpdateSemuaStatus() {
        if (pesananTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih pesanan terlebih dahulu.");
            return;
        }

        String statusBayarBaru = comboStatusBayar.getValue();
        String statusKirimBaru = comboStatusKirim.getValue();
        String catatanKirim = txtCatatanKirim.getText();

        // Update status pembayaran jika ada perubahan
        if (statusBayarBaru != null && !statusBayarBaru.equals(pesananTerpilih.getStatusPembayaran())) {
            pesananDAO.updateStatusPembayaran(pesananTerpilih.getIdPesanan(), statusBayarBaru);
        }

        // Update atau buat data pengiriman
        pengirimanDAO.saveOrUpdatePengiriman(pesananTerpilih.getIdPesanan(), statusKirimBaru, catatanKirim);

        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Status pesanan berhasil diupdate.");

        // Refresh tabel untuk melihat perubahan
        loadPesananData();
    }

    @FXML
    private void handleKembali(ActionEvent event) throws IOException {
        Navigasi.goBack(event, "DashboardView.fxml");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}