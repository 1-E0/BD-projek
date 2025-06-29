package com.example.bd.controller;

import com.example.bd.dao.PelangganDAO;
import com.example.bd.model.Pelanggan;
import com.example.bd.util.Navigasi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManajemenPelangganController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private TableView<Pelanggan> pelangganTable;
    @FXML private TableColumn<Pelanggan, Integer> colId;
    @FXML private TableColumn<Pelanggan, String> colNama;
    @FXML private TableColumn<Pelanggan, String> colEmail;
    @FXML private TableColumn<Pelanggan, String> colAlamat;
    @FXML private TableColumn<Pelanggan, String> colTelepon;
    @FXML private TextField txtNama;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPassword;
    @FXML private TextField txtTelepon;
    @FXML private TextField txtAlamat;
    @FXML private Button btnSimpan;
    @FXML private Label formTitleLabel;

    private final PelangganDAO pelangganDAO = new PelangganDAO();
    private final ObservableList<Pelanggan> pelangganList = FXCollections.observableArrayList();
    private Pelanggan pelangganTerpilih = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupTableSelectionListener();
        loadTableData();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPelanggan"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaPelanggan"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailPelanggan"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamatPelanggan"));
        colTelepon.setCellValueFactory(new PropertyValueFactory<>("noTelpPelanggan"));
    }

    private void loadTableData() {
        pelangganList.clear();
        pelangganList.addAll(pelangganDAO.getAllPelanggan());
        pelangganTable.setItems(pelangganList);
    }

    private void setupTableSelectionListener() {
        pelangganTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                pelangganTerpilih = newSelection;
                txtNama.setText(pelangganTerpilih.getNamaPelanggan());
                txtEmail.setText(pelangganTerpilih.getEmailPelanggan());
                txtAlamat.setText(pelangganTerpilih.getAlamatPelanggan());
                txtTelepon.setText(pelangganTerpilih.getNoTelpPelanggan());
                txtPassword.setPromptText("Biarkan kosong jika tidak ingin diubah");
                txtPassword.setDisable(true);
                formTitleLabel.setText("Form Edit Pelanggan");
                btnSimpan.setText("Update");
            }
        });
    }

    @FXML
    private void handleSimpanButton() {
        String nama = txtNama.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String alamat = txtAlamat.getText();
        String telepon = txtTelepon.getText();

        if (nama.isEmpty() || email.isEmpty() || alamat.isEmpty() || telepon.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error Validasi", "Semua field harus diisi!");
            return;
        }

        if (pelangganTerpilih == null) {
            if (password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error Validasi", "Password wajib diisi saat menambah pelanggan baru!");
                return;
            }
            Pelanggan newPelanggan = new Pelanggan();
            newPelanggan.setNamaPelanggan(nama);
            newPelanggan.setEmailPelanggan(email);
            newPelanggan.setPasswordPelanggan(password);
            newPelanggan.setAlamatPelanggan(alamat);
            newPelanggan.setNoTelpPelanggan(telepon);


            try {

                pelangganDAO.addPelanggan(newPelanggan);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pelanggan baru berhasil ditambahkan!");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menyimpan pelanggan baru.");
                e.printStackTrace();
            }


        } else {
            pelangganTerpilih.setNamaPelanggan(nama);
            pelangganTerpilih.setEmailPelanggan(email);
            pelangganTerpilih.setAlamatPelanggan(alamat);
            pelangganTerpilih.setNoTelpPelanggan(telepon);
            pelangganDAO.updatePelanggan(pelangganTerpilih);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data pelanggan berhasil diupdate!");
        }

        loadTableData();
        clearFormAndSelection();
    }

    @FXML
    private void handleHapusButton() {
        if (pelangganTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih pelanggan yang ingin dihapus.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Hapus");
        confirmationAlert.setHeaderText("Hapus pelanggan: " + pelangganTerpilih.getNamaPelanggan() + "?");
        confirmationAlert.setContentText("Apakah Anda yakin?");

        Optional<ButtonType> response = confirmationAlert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            pelangganDAO.deletePelanggan(pelangganTerpilih.getIdPelanggan());
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data pelanggan telah dihapus.");
            loadTableData();
            clearFormAndSelection();
        }
    }

    @FXML
    private void handleBatalButton() {
        clearFormAndSelection();
    }

    @FXML
    private void handleKembali(ActionEvent event) throws IOException {
        Navigasi.goBack(event, "DashboardView.fxml");
    }

    private void clearFormAndSelection() {
        pelangganTerpilih = null;
        txtNama.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtAlamat.clear();
        txtTelepon.clear();
        txtPassword.setPromptText("Wajib diisi saat menambah baru");
        txtPassword.setDisable(false);
        pelangganTable.getSelectionModel().clearSelection();
        formTitleLabel.setText("Form Tambah Pelanggan Baru");
        btnSimpan.setText("Simpan");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}