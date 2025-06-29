package com.example.bd.controller;

import com.example.bd.model.Admin;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    @FXML private Button btnManajemenMenu;
    @FXML private Button btnManajemenKategori;
    @FXML private Button btnManajemenPelanggan;
    @FXML private Button btnManajemenStaff;
    @FXML private Button btnManajemenCabang;
    @FXML private Button btnManajemenReview;
    @FXML private Button btnManajemenDiskon;
    @FXML private Button btnManajemenMenuHarian;
    @FXML private Button btnManajemenMetodePembayaran;
    @FXML private Button btnManajemenVoucher;
    @FXML private Button btnLaporanPerforma;
    @FXML private Button btnManajemenPesanan;
    @FXML private Button btnManajemenAdmin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Admin loggedInAdmin = UserSession.getInstance().getLoggedInAdmin();
        if (loggedInAdmin == null) {

            System.err.println("Tidak ada admin yang login, kembali ke halaman login.");

            return;
        }

        String jenisAdmin = loggedInAdmin.getJenisAdmin();

        if ("Pusat".equalsIgnoreCase(jenisAdmin)) {

            setButtonVisibility(true, btnManajemenMenu, btnManajemenKategori, btnManajemenPelanggan,
                    btnManajemenStaff, btnManajemenCabang, btnManajemenReview, btnManajemenDiskon,
                    btnManajemenMenuHarian, btnManajemenMetodePembayaran, btnManajemenVoucher,
                    btnLaporanPerforma, btnManajemenPesanan, btnManajemenAdmin);
        } else if ("Cabang".equalsIgnoreCase(jenisAdmin)) {

            setButtonVisibility(true, btnManajemenMenuHarian, btnManajemenPesanan, btnManajemenStaff);


            setButtonVisibility(false, btnManajemenMenu, btnManajemenKategori, btnManajemenPelanggan,
                    btnManajemenCabang, btnManajemenReview, btnManajemenDiskon,
                    btnManajemenMetodePembayaran, btnManajemenVoucher, btnLaporanPerforma, btnManajemenAdmin);
        }
    }

    private void setButtonVisibility(boolean isVisible, Button... buttons) {
        for (Button button : buttons) {
            button.setVisible(isVisible);
            button.setManaged(isVisible);
        }
    }


    @FXML void goToManajemenMenu(ActionEvent event) { Navigasi.switchScene(event, "ManajemenMenuView.fxml"); }
    @FXML void goToManajemenKategori(ActionEvent event) { Navigasi.switchScene(event, "ManajemenKategoriView.fxml"); }
    @FXML void goToManajemenPelanggan(ActionEvent event) { Navigasi.switchScene(event, "ManajemenPelangganView.fxml"); }
    @FXML void goToManajemenStaff(ActionEvent event) { Navigasi.switchScene(event, "ManajemenStaffView.fxml"); }
    @FXML void goToManajemenCabang(ActionEvent event) { Navigasi.switchScene(event, "ManajemenCabangView.fxml"); }
    @FXML void goToManajemenReview(ActionEvent event) { Navigasi.switchScene(event, "ManajemenReviewView.fxml"); }
    @FXML void goToManajemenDiskon(ActionEvent event) { Navigasi.switchScene(event, "ManajemenDiskonView.fxml"); }
    @FXML void goToManajemenMenuHarian(ActionEvent event) { Navigasi.switchScene(event, "ManajemenMenuHarianView.fxml"); }
    @FXML void goToManajemenMetodePembayaran(ActionEvent event) { Navigasi.switchScene(event, "ManajemenMetodePembayaranView.fxml"); }
    @FXML void goToManajemenVoucher(ActionEvent event) { Navigasi.switchScene(event, "ManajemenVoucherView.fxml"); }
    @FXML void goToLaporanPerforma(ActionEvent event) { Navigasi.switchScene(event, "LaporanPerformaView.fxml"); }
    @FXML void goToManajemenPesanan(ActionEvent event) { Navigasi.switchScene(event, "ManajemenPesananView.fxml"); }


    @FXML
    void goToManajemenAdmin(ActionEvent event) {
        Navigasi.switchScene(event, "ManajemenAdminView.fxml");
    }

    @FXML
    void handleLogout(ActionEvent event) {
        UserSession.getInstance().cleanUserSession();
        Navigasi.switchScene(event, "LoginView.fxml");
    }
}