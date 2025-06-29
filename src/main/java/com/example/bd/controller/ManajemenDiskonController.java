package com.example.bd.controller;

import com.example.bd.dao.DiskonDAO;
import com.example.bd.model.Diskon;
import com.example.bd.util.Navigasi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManajemenDiskonController implements Initializable {

    @FXML private TableView<Diskon> diskonTable;
    @FXML private TableColumn<Diskon, Integer> colId;
    @FXML private TableColumn<Diskon, String> colNama;
    @FXML private TableColumn<Diskon, Double> colPersen;
    @FXML private TableColumn<Diskon, String> colSyarat;
    @FXML private TableColumn<Diskon, LocalDate> colMulai;
    @FXML private TableColumn<Diskon, LocalDate> colAkhir;

    @FXML private TextField txtNama;
    @FXML private TextField txtPersen;
    @FXML private TextField txtSyarat;
    @FXML private DatePicker dateMulai;
    @FXML private DatePicker dateAkhir;
    @FXML private Label formTitleLabel;
    @FXML private Button btnSimpan;

    private final DiskonDAO diskonDAO = new DiskonDAO();
    private final ObservableList<Diskon> diskonList = FXCollections.observableArrayList();
    private Diskon diskonTerpilih;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupTableSelectionListener();
        loadDiskonData();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idDiskon"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaDiskon"));
        colPersen.setCellValueFactory(new PropertyValueFactory<>("persenDiskon"));
        colSyarat.setCellValueFactory(new PropertyValueFactory<>("syaratDanKetentuanDiskon"));
        colMulai.setCellValueFactory(new PropertyValueFactory<>("tanggalMulaiDiskon"));
        colAkhir.setCellValueFactory(new PropertyValueFactory<>("tanggalAkhirDiskon"));
    }

    private void loadDiskonData() {
        diskonList.clear();
        diskonList.setAll(diskonDAO.getAllDiskon());
        diskonTable.setItems(diskonList);
    }

    private void setupTableSelectionListener() {
        diskonTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                diskonTerpilih = newVal;
                txtNama.setText(newVal.getNamaDiskon());
                txtPersen.setText(String.valueOf(newVal.getPersenDiskon()));
                txtSyarat.setText(newVal.getSyaratDanKetentuanDiskon());
                dateMulai.setValue(newVal.getTanggalMulaiDiskon());
                dateAkhir.setValue(newVal.getTanggalAkhirDiskon());
                formTitleLabel.setText("Formulir Edit Diskon");
                btnSimpan.setText("Perbarui");
            }
        });
    }

    @FXML
    private void handleSimpan(ActionEvent event) {
        if (txtNama.getText().isEmpty() || txtPersen.getText().isEmpty() || dateMulai.getValue() == null || dateAkhir.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan Validasi", "Semua kolom wajib diisi!");
            return;
        }

        try {
            Diskon diskon = (diskonTerpilih == null) ? new Diskon() : diskonTerpilih;
            diskon.setNamaDiskon(txtNama.getText());
            diskon.setPersenDiskon(Double.parseDouble(txtPersen.getText()));
            diskon.setSyaratDanKetentuanDiskon(txtSyarat.getText());
            diskon.setTanggalMulaiDiskon(dateMulai.getValue());
            diskon.setTanggalAkhirDiskon(dateAkhir.getValue());

            if (diskonTerpilih == null) {
                diskonDAO.addDiskon(diskon);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Diskon baru berhasil ditambahkan.");
            } else {
                diskonDAO.updateDiskon(diskon);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Diskon berhasil diperbarui.");
            }
            loadDiskonData();
            clearForm();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan Input", "Persentase harus berupa angka.");
        }
    }

    @FXML
    private void handleHapus(ActionEvent event) {
        if (diskonTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan", "Pilih diskon untuk dihapus.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Apakah Anda yakin ingin menghapus diskon: " + diskonTerpilih.getNamaDiskon() + "?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Konfirmasi Penghapusan");
        Optional<ButtonType> response = confirm.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.YES) {
            diskonDAO.deleteDiskon(diskonTerpilih.getIdDiskon());
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Diskon berhasil dihapus.");
            loadDiskonData();
            clearForm();
        }
    }

    @FXML
    private void handleBatal(ActionEvent event) {
        clearForm();
    }

    private void clearForm() {
        diskonTerpilih = null;
        txtNama.clear();
        txtPersen.clear();
        txtSyarat.clear();
        dateMulai.setValue(null);
        dateAkhir.setValue(null);
        diskonTable.getSelectionModel().clearSelection();
        formTitleLabel.setText("Formulir Penambahan Diskon Baru");
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