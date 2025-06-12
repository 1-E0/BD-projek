package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.MetodePembayaranDAO;
import com.example.bd.model.MetodePembayaran;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManajemenMetodePembayaranController implements Initializable {

    @FXML private TableView<MetodePembayaran> metodeTable;
    @FXML private TableColumn<MetodePembayaran, Integer> colId;
    @FXML private TableColumn<MetodePembayaran, String> colNama;
    @FXML private TextField txtNamaMetode;
    @FXML private Button btnSimpan;
    @FXML private Label formTitleLabel;

    private final MetodePembayaranDAO metodeDAO = new MetodePembayaranDAO();
    private final ObservableList<MetodePembayaran> metodeList = FXCollections.observableArrayList();
    private MetodePembayaran metodeTerpilih = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupTableSelectionListener();
        loadTableData();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idMetode"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("caraMetode"));
    }

    private void loadTableData() {
        metodeList.clear();
        metodeList.setAll(metodeDAO.getAll());
        metodeTable.setItems(metodeList);
    }

    private void setupTableSelectionListener() {
        metodeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                metodeTerpilih = newSelection;
                txtNamaMetode.setText(newSelection.getCaraMetode());
                formTitleLabel.setText("Form Edit Metode");
                btnSimpan.setText("Update");
            }
        });
    }

    @FXML
    private void handleSimpan(ActionEvent event) {
        String namaMetode = txtNamaMetode.getText();
        if (namaMetode.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Nama metode tidak boleh kosong.");
            return;
        }

        if (metodeTerpilih == null) {
            MetodePembayaran newMetode = new MetodePembayaran();
            newMetode.setCaraMetode(namaMetode);
            metodeDAO.add(newMetode);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Metode baru berhasil ditambahkan.");
        } else {
            metodeTerpilih.setCaraMetode(namaMetode);
            metodeDAO.update(metodeTerpilih);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Metode berhasil diupdate.");
        }
        loadTableData();
        clearForm();
    }

    @FXML
    private void handleHapus(ActionEvent event) {
        if (metodeTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih metode yang ingin dihapus.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus: " + metodeTerpilih.getCaraMetode() + "?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Konfirmasi Hapus");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            metodeDAO.delete(metodeTerpilih.getIdMetode());
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Metode berhasil dihapus.");
            loadTableData();
            clearForm();
        }
    }

    @FXML
    private void handleBatal(ActionEvent event) {
        clearForm();
    }

    private void clearForm() {
        metodeTerpilih = null;
        txtNamaMetode.clear();
        metodeTable.getSelectionModel().clearSelection();
        formTitleLabel.setText("Form Tambah Metode Baru");
        btnSimpan.setText("Simpan");
    }

    @FXML
    private void handleKembali(ActionEvent event) throws IOException {
        Navigasi.goBack(event, "DashboardView.fxml");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}