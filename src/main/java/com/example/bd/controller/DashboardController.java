package com.example.bd.controller;

import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardController {

    @FXML
    void goToManajemenMenu(ActionEvent event) {
        Navigasi.switchScene(event, "ManajemenMenuView.fxml");
    }

    @FXML
    void goToManajemenKategori(ActionEvent event) {
        Navigasi.switchScene(event, "ManajemenKategoriView.fxml");
    }

    @FXML
    void goToManajemenPelanggan(ActionEvent event) {
        Navigasi.switchScene(event, "ManajemenPelangganView.fxml");
    }

    @FXML
    void goToManajemenStaff(ActionEvent event) {
        Navigasi.switchScene(event, "ManajemenStaffView.fxml");
    }

    @FXML
    void goToManajemenCabang(ActionEvent event) {
        Navigasi.switchScene(event, "ManajemenCabangView.fxml");
    }

    @FXML
    void goToManajemenReview(ActionEvent event) {
        Navigasi.switchScene(event, "ManajemenReviewView.fxml");
    }

    @FXML
    void goToManajemenDiskon(ActionEvent event) {
        Navigasi.switchScene(event, "ManajemenDiskonView.fxml");
    }

    @FXML
    void goToManajemenMenuHarian(ActionEvent event) {
        Navigasi.switchScene(event, "ManajemenMenuHarianView.fxml");
    }

    @FXML
    void goToManajemenMetodePembayaran(ActionEvent event) {
        Navigasi.switchScene(event, "ManajemenMetodePembayaranView.fxml");
    }

    @FXML
    void goToManajemenVoucher(ActionEvent event) {
        Navigasi.switchScene(event, "ManajemenVoucherView.fxml");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        UserSession.getInstance().cleanUserSession();
        Navigasi.switchScene(event, "LoginView.fxml");
    }

    public void goToManajemenPesanan(ActionEvent actionEvent) {
        Navigasi.switchScene(actionEvent, "ManajemenPesananView.fxml");
    }
}