package com.example.bd.controller;

import com.example.bd.dao.MetodePembayaranDAO;
import com.example.bd.model.MetodePembayaran;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PembayaranDialogController {

    @FXML private Label lblTotalBayar;
    @FXML private ComboBox<MetodePembayaran> comboMetode;
    @FXML private Button btnKonfirmasi;

    private final MetodePembayaranDAO metodeDAO = new MetodePembayaranDAO();
    private MetodePembayaran metodeTerpilih = null;

    public void initialize() {
        comboMetode.setItems(FXCollections.observableArrayList(metodeDAO.getAll()));
    }

    // Menerima data total harga dari controller pemanggil
    public void setTotal(double total) {
        lblTotalBayar.setText(String.format("Rp %.0f", total));
    }

    // Mengembalikan metode yang dipilih ke controller pemanggil
    public MetodePembayaran getMetodeTerpilih() {
        return metodeTerpilih;
    }

    @FXML
    private void handleKonfirmasi() {
        if (comboMetode.getValue() != null) {
            this.metodeTerpilih = comboMetode.getValue();
            closeDialog();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Pilih metode pembayaran terlebih dahulu.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBatal() {
        this.metodeTerpilih = null; // Menandakan pembatalan
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) btnKonfirmasi.getScene().getWindow();
        stage.close();
    }
}