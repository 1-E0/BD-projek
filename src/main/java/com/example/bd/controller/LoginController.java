package com.example.bd.controller;

import com.example.bd.dao.AdminDAO;
import com.example.bd.dao.PelangganDAO;
import com.example.bd.model.Admin;
import com.example.bd.model.Pelanggan;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> comboPeran;
    @FXML private PasswordField txtPasswordHidden;
    @FXML private TextField txtPasswordVisible;
    @FXML private FontAwesomeIconView iconTogglePassword;

    private final PelangganDAO pelangganDAO = new PelangganDAO();
    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        comboPeran.setItems(FXCollections.observableArrayList("Pelanggan", "Admin Cabang", "Admin Pusat"));
        comboPeran.setValue("Pelanggan");
        txtPasswordVisible.textProperty().bindBidirectional(txtPasswordHidden.textProperty());
    }

    @FXML
    void togglePasswordVisibility(MouseEvent event) {
        if (txtPasswordHidden.isVisible()) {
            txtPasswordHidden.setVisible(false);
            txtPasswordHidden.setManaged(false);
            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setManaged(true);
            iconTogglePassword.setGlyphName("EYE_SLASH");
        } else {
            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);
            txtPasswordHidden.setVisible(true);
            txtPasswordHidden.setManaged(true);
            iconTogglePassword.setGlyphName("EYE");
        }
    }

    @FXML
    void handleLoginButton(ActionEvent event) {
        String email = txtEmail.getText();
        String password = txtPasswordHidden.getText();
        String peran = comboPeran.getValue();

        if (email.isEmpty() || password.isEmpty() || peran == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Semua field harus diisi.");
            return;
        }


        switch (peran) {
            case "Pelanggan":
                loginSebagaiPelanggan(event, email, password);
                break;
            case "Admin Pusat":
            case "Admin Cabang":
                loginSebagaiAdmin(event, email, password, peran);
                break;
        }
    }

    private void loginSebagaiPelanggan(ActionEvent event, String email, String password) {
        Pelanggan pelanggan = pelangganDAO.validateLogin(email, password);
        if (pelanggan != null) {
            UserSession.getInstance().setLoggedInPelanggan(pelanggan);
            UserSession.getInstance().setLoggedInAdmin(null);
            Navigasi.switchScene(event, "PelangganDashboardView.fxml");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Email atau password pelanggan salah.");
        }
    }

    private void loginSebagaiAdmin(ActionEvent event, String email, String password, String peranDipilih) {
        Admin admin = adminDAO.validateLogin(email, password);

        if (admin == null) {
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Email atau password admin salah.");
            return;
        }


        String peranDatabase = admin.getJenisAdmin();
        boolean peranCocok = (peranDipilih.equals("Admin Pusat") && peranDatabase.equalsIgnoreCase("Pusat")) ||
                (peranDipilih.equals("Admin Cabang") && peranDatabase.equalsIgnoreCase("Cabang"));

        if (peranCocok) {
            UserSession.getInstance().setLoggedInAdmin(admin);
            UserSession.getInstance().setLoggedInPelanggan(null);
            Navigasi.switchScene(event, "DashboardView.fxml");
        } else {

            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Peran yang Anda pilih (" + peranDipilih + ") tidak sesuai dengan akun ini.");
        }
    }

    @FXML
    void goToRegister(ActionEvent event) {
        Navigasi.switchScene(event, "RegisterView.fxml");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}