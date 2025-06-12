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
        comboPeran.setItems(FXCollections.observableArrayList("Admin", "Pelanggan"));
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

        if (peran.equals("Admin")) {
            Admin admin = adminDAO.validateLogin(email, password);
            if (admin != null) {

                // Gunakan Navigasi.switchScene untuk transisi maju
                Navigasi.switchScene(event, "DashboardView.fxml");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "Email atau password admin salah.");
            }
        } else if (peran.equals("Pelanggan")) {
            Pelanggan pelanggan = pelangganDAO.validateLogin(email, password);
            if (pelanggan != null) {
                UserSession.getInstance().setLoggedInPelanggan(pelanggan);

                // Gunakan Navigasi.switchScene untuk transisi maju
                Navigasi.switchScene(event, "PelangganDashboardView.fxml");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Gagal", "Email atau password pelanggan salah.");
            }
        }
    }

    @FXML
    void goToRegister(ActionEvent event) {
        // Ini juga transisi maju
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