package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.PelangganDAO;
import com.example.bd.model.Pelanggan;
import com.example.bd.util.Navigasi;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class RegisterController {

    @FXML private TextField txtNama;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtTelepon;
    @FXML private TextField txtAlamat;

    private final PelangganDAO pelangganDAO = new PelangganDAO();

    @FXML
    void handleRegisterButton(ActionEvent event) throws IOException {
        String nama = txtNama.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();
        String telepon = txtTelepon.getText();
        String alamat = txtAlamat.getText();

        if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || telepon.isEmpty() || alamat.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Semua field wajib diisi!");
            return;
        }

        // Anda bisa menambahkan validasi email unik di sini jika perlu

        Pelanggan newPelanggan = new Pelanggan();
        newPelanggan.setNamaPelanggan(nama);
        newPelanggan.setEmailPelanggan(email);
        newPelanggan.setPasswordPelanggan(password); // Ingat untuk hash di aplikasi nyata
        newPelanggan.setNoTelpPelanggan(telepon);
        newPelanggan.setAlamatPelanggan(alamat);

        pelangganDAO.addPelanggan(newPelanggan);

        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Registrasi berhasil! Silakan login dengan akun baru Anda.");
        goToLogin(event);
    }

    @FXML
    void goToLogin(ActionEvent event) throws IOException {
        Navigasi.goBack(event, "LoginView.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        URL resourceUrl = HelloApplication.class.getResource("view/" + fxmlFile);
        Parent root = FXMLLoader.load(resourceUrl);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}