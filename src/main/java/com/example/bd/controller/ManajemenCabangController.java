package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.CabangDAO;
import com.example.bd.dao.KotaDAO;
import com.example.bd.model.Cabang;
import com.example.bd.model.Kota;
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

public class ManajemenCabangController implements Initializable {

    @FXML private TableView<Cabang> cabangTable;
    @FXML private TableColumn<Cabang, Integer> colId;
    @FXML private TableColumn<Cabang, String> colAlamat;
    @FXML private TableColumn<Cabang, String> colTelepon;
    @FXML private TableColumn<Cabang, String> colKota;

    @FXML private TextField txtAlamat;
    @FXML private TextField txtTelepon;
    @FXML private ComboBox<Kota> comboKota;

    @FXML private Label formTitleLabel;
    @FXML private Button btnSimpan;

    private final CabangDAO cabangDAO = new CabangDAO();
    private final KotaDAO kotaDAO = new KotaDAO();
    private final ObservableList<Cabang> cabangList = FXCollections.observableArrayList();
    private Cabang cabangTerpilih;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        populateKotaComboBox();
        setupTableSelectionListener();
        loadCabangData();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idCabang"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamatCabang"));
        colTelepon.setCellValueFactory(new PropertyValueFactory<>("noTelpCabang"));
        colKota.setCellValueFactory(new PropertyValueFactory<>("namaKota"));
    }

    private void populateKotaComboBox() {
        comboKota.setItems(FXCollections.observableArrayList(kotaDAO.getAllKota()));
    }

    private void loadCabangData() {
        cabangList.clear();
        cabangList.setAll(cabangDAO.getAllCabang());
        cabangTable.setItems(cabangList);
    }

    private void setupTableSelectionListener() {
        cabangTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cabangTerpilih = newSelection;
                txtAlamat.setText(newSelection.getAlamatCabang());
                txtTelepon.setText(newSelection.getNoTelpCabang());

                for (Kota kota : comboKota.getItems()) {
                    if (kota.getIdKota() == newSelection.getIdKota()) {
                        comboKota.setValue(kota);
                        break;
                    }
                }
                formTitleLabel.setText("Form Edit Cabang");
                btnSimpan.setText("Update");
            }
        });
    }

    @FXML
    private void handleSimpan(ActionEvent event) {
        if (txtAlamat.getText().isEmpty() || txtTelepon.getText().isEmpty() || comboKota.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error Validasi", "Semua field harus diisi!");
            return;
        }

        if (cabangTerpilih == null) { // Mode Tambah
            Cabang newCabang = new Cabang();
            fillCabangFromForm(newCabang);
            cabangDAO.addCabang(newCabang);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Cabang baru berhasil ditambahkan.");
        } else { // Mode Update
            fillCabangFromForm(cabangTerpilih);
            cabangDAO.updateCabang(cabangTerpilih);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data cabang berhasil diupdate.");
        }
        loadCabangData();
        clearForm();
    }

    @FXML
    private void handleHapus(ActionEvent event) {
        if (cabangTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih cabang yang ingin dihapus.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus cabang ini?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Konfirmasi Hapus");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                cabangDAO.deleteCabang(cabangTerpilih.getIdCabang());
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Cabang berhasil dihapus.");
                loadCabangData();
                clearForm();
            }
        });
    }

    @FXML
    private void handleBatal(ActionEvent event) {
        clearForm();
    }

    private void fillCabangFromForm(Cabang cabang) {
        cabang.setAlamatCabang(txtAlamat.getText());
        cabang.setNoTelpCabang(txtTelepon.getText());
        cabang.setIdKota(comboKota.getValue().getIdKota());
    }

    private void clearForm() {
        cabangTerpilih = null;
        txtAlamat.clear();
        txtTelepon.clear();
        comboKota.setValue(null);
        cabangTable.getSelectionModel().clearSelection();
        formTitleLabel.setText("Form Tambah Cabang Baru");
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