package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.KategoriDAO;
import com.example.bd.model.Kategori;
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

public class ManajemenKategoriController implements Initializable {

    @FXML private TableView<Kategori> kategoriTable;
    @FXML private TableColumn<Kategori, Integer> colId;
    @FXML private TableColumn<Kategori, String> colNama;
    @FXML private TextField txtNamaKategori;
    @FXML private Button btnSimpan;
    @FXML private Label formTitleLabel;

    private final KategoriDAO kategoriDAO = new KategoriDAO();
    private final ObservableList<Kategori> kategoriList = FXCollections.observableArrayList();
    private Kategori kategoriTerpilih = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupTableSelectionListener();
        loadTableData();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idKategori"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaKategori"));
    }

    private void loadTableData() {
        kategoriList.clear();
        kategoriList.addAll(kategoriDAO.getAllKategori());
        kategoriTable.setItems(kategoriList);
    }

    private void setupTableSelectionListener() {
        kategoriTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                kategoriTerpilih = newSelection;
                txtNamaKategori.setText(kategoriTerpilih.getNamaKategori());
                formTitleLabel.setText("Form Edit Kategori");
                btnSimpan.setText("Update");
            }
        });
    }

    @FXML
    private void handleSimpanButton() {
        String namaKategori = txtNamaKategori.getText();
        if (namaKategori.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Nama kategori tidak boleh kosong.");
            return;
        }

        if (kategoriTerpilih == null) { // Tambah baru
            Kategori newKategori = new Kategori();
            newKategori.setNamaKategori(namaKategori);
            kategoriDAO.addKategori(newKategori);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Kategori baru berhasil ditambahkan.");
        } else { // Update
            kategoriTerpilih.setNamaKategori(namaKategori);
            kategoriDAO.updateKategori(kategoriTerpilih);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Kategori berhasil diupdate.");
        }
        loadTableData();
        clearFormAndSelection();
    }

    @FXML
    private void handleHapusButton() {
        if (kategoriTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih kategori yang ingin dihapus.");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus kategori: " + kategoriTerpilih.getNamaKategori() + "?", ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText("Konfirmasi Hapus");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            kategoriDAO.deleteKategori(kategoriTerpilih.getIdKategori());
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Kategori berhasil dihapus.");
            loadTableData();
            clearFormAndSelection();
        }
    }

    @FXML
    private void handleBatalButton() {
        clearFormAndSelection();
    }

    private void clearFormAndSelection() {
        kategoriTerpilih = null;
        txtNamaKategori.clear();
        kategoriTable.getSelectionModel().clearSelection();
        formTitleLabel.setText("Form Tambah Kategori Baru");
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