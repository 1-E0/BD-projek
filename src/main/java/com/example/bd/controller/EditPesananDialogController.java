package com.example.bd.controller;

import com.example.bd.dao.PengirimanDAO;
import com.example.bd.dao.PesananDAO;
import com.example.bd.model.DetailPesanan;
import com.example.bd.model.Pesanan;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class EditPesananDialogController {

    @FXML private Label lblDialogTitle;
    @FXML private Label lblNamaPelanggan;
    @FXML private TextArea txtAlamatTujuan;
    @FXML private ComboBox<String> comboStatusBayar;
    @FXML private ComboBox<String> comboStatusKirim;
    @FXML private ListView<String> itemListView;

    private Pesanan pesanan;
    private final PesananDAO pesananDAO = new PesananDAO();
    private final PengirimanDAO pengirimanDAO = new PengirimanDAO();
    private boolean saved = false;

    public void initialize() {
        comboStatusBayar.setItems(FXCollections.observableArrayList("Belum Lunas", "Lunas", "Dibatalkan"));
        comboStatusKirim.setItems(FXCollections.observableArrayList("Sedang Disiapkan", "Dalam Perjalanan", "Tiba di Tujuan"));
    }

    public void setData(Pesanan pesanan) {
        this.pesanan = pesanan;

        lblDialogTitle.setText("Detail Pesanan #" + pesanan.getIdPesanan());
        lblNamaPelanggan.setText(pesanan.getNamaPelanggan());
        txtAlamatTujuan.setText(pesanan.getAlamatTujuan());
        comboStatusBayar.setValue(pesanan.getStatusPembayaran());
        comboStatusKirim.setValue(pesanan.getStatusPengiriman());

        // Muat detail item ke ListView
        List<DetailPesanan> detailItems = pesananDAO.getDetailByPesanan(pesanan.getIdPesanan());
        itemListView.setItems(FXCollections.observableArrayList(
                detailItems.stream()
                        .map(d -> d.getJumlah() + "x " + d.getNamaMenu())
                        .collect(Collectors.toList())
        ));
    }

    @FXML
    private void handleSimpan(ActionEvent event) {
        String alamatBaru = txtAlamatTujuan.getText();
        String statusBayarBaru = comboStatusBayar.getValue();
        String statusKirimBaru = comboStatusKirim.getValue();

        // Update alamat jika berubah
        if (alamatBaru != null && !alamatBaru.trim().isEmpty() && !alamatBaru.equals(pesanan.getAlamatTujuan())) {
            pesananDAO.updateAlamatTujuan(pesanan.getIdPesanan(), alamatBaru);
        }

        // Update status pembayaran jika berubah
        if (statusBayarBaru != null && !statusBayarBaru.equals(pesanan.getStatusPembayaran())) {
            pesananDAO.updateStatusPembayaran(pesanan.getIdPesanan(), statusBayarBaru);
        }

        // Update pengiriman
        pengirimanDAO.saveOrUpdatePengiriman(pesanan.getIdPesanan(), statusKirimBaru, ""); // Catatan dikosongkan untuk simplicity

        this.saved = true;
        closeStage(event);
    }

    @FXML
    private void handleBatal(ActionEvent event) {
        this.saved = false;
        closeStage(event);
    }

    public boolean isSaved() {
        return saved;
    }

    private void closeStage(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}