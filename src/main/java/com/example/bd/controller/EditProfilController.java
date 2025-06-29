package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.PelangganDAO;
import com.example.bd.model.Pelanggan;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditProfilController implements Initializable {

    @FXML private TextField txtNama;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelepon;
    @FXML private TextField txtAlamat;
    @FXML private PasswordField txtPasswordLama;
    @FXML private PasswordField txtPasswordBaru;
    @FXML private PasswordField txtKonfirmasiPassword;

    private Pelanggan pelangganAktif;
    private final PelangganDAO pelangganDAO = new PelangganDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.pelangganAktif = UserSession.getInstance().getLoggedInPelanggan();
        if (pelangganAktif != null) {
            populateForm(pelangganAktif);
        }
    }

    private void populateForm(Pelanggan pelanggan) {
        txtNama.setText(pelanggan.getNamaPelanggan());
        txtEmail.setText(pelanggan.getEmailPelanggan());
        txtTelepon.setText(pelanggan.getNoTelpPelanggan());
        txtAlamat.setText(pelanggan.getAlamatPelanggan());
    }

    @FXML
    private void handleSimpanProfil() {
        if (txtNama.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Nama dan Email tidak boleh kosong.");
            return;
        }

        pelangganAktif.setNamaPelanggan(txtNama.getText());
        pelangganAktif.setEmailPelanggan(txtEmail.getText());
        pelangganAktif.setNoTelpPelanggan(txtTelepon.getText());
        pelangganAktif.setAlamatPelanggan(txtAlamat.getText());

        pelangganDAO.updateProfil(pelangganAktif);
        UserSession.getInstance().setLoggedInPelanggan(pelangganAktif);

        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Profil berhasil diperbarui.");
    }

    @FXML
    private void handleUbahPassword() {
        String passLama = txtPasswordLama.getText();
        String passBaru = txtPasswordBaru.getText();
        String konfirmasiPass = txtKonfirmasiPassword.getText();

        if (passLama.isEmpty() || passBaru.isEmpty() || konfirmasiPass.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Semua field password harus diisi.");
            return;
        }
        if (!passBaru.equals(konfirmasiPass)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password baru dan konfirmasi tidak cocok.");
            return;
        }


        Pelanggan verifikasi = pelangganDAO.validateLogin(pelangganAktif.getEmailPelanggan(), passLama);
        if (verifikasi == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password lama yang Anda masukkan salah.");
            return;
        }


        pelangganDAO.updatePassword(pelangganAktif.getIdPelanggan(), passBaru);
        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Password berhasil diubah.");
        txtPasswordLama.clear();
        txtPasswordBaru.clear();
        txtKonfirmasiPassword.clear();
    }

    @FXML
    private void handleKembali(ActionEvent event) throws IOException {
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