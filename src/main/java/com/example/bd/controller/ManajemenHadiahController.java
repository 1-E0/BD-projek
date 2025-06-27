package com.example.bd.controller;

import com.example.bd.dao.HadiahDAO;
import com.example.bd.model.Hadiah;
import com.example.bd.util.Navigasi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManajemenHadiahController implements Initializable {

    @FXML private TableView<Hadiah> hadiahTable;
    @FXML private TableColumn<Hadiah, String> colNama;
    @FXML private TableColumn<Hadiah, String> colJenis;
    @FXML private TableColumn<Hadiah, Integer> colPoin;
    @FXML private TableColumn<Hadiah, String> colStatus;

    @FXML private TextField txtNamaHadiah;
    @FXML private ComboBox<String> comboJenisHadiah;
    @FXML private TextArea txtDeskripsi;
    @FXML private TextField txtBiayaPoin;
    @FXML private TextField txtNilaiVoucher;
    @FXML private ComboBox<String> comboStatus;

    @FXML private VBox voucherFields; // VBox untuk field khusus voucher

    private final HadiahDAO hadiahDAO = new HadiahDAO();
    private final ObservableList<Hadiah> hadiahList = FXCollections.observableArrayList();
    private Hadiah hadiahTerpilih = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupForm();
        loadTableData();
    }

    private void setupTableColumns() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaHadiah"));
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenisHadiah"));
        colPoin.setCellValueFactory(new PropertyValueFactory<>("biayaPoin"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusHadiah"));
    }

    private void setupForm() {
        comboJenisHadiah.setItems(FXCollections.observableArrayList("BARANG", "VOUCHER"));
        comboStatus.setItems(FXCollections.observableArrayList("AKTIF", "NONAKTIF"));

        // Sembunyikan field voucher secara default
        comboJenisHadiah.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            voucherFields.setVisible("VOUCHER".equals(newVal));
            voucherFields.setManaged("VOUCHER".equals(newVal));
        });

        hadiahTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillFormWithData(newSelection);
            }
        });
        clearForm();
    }

    private void fillFormWithData(Hadiah hadiah) {
        hadiahTerpilih = hadiah;
        txtNamaHadiah.setText(hadiah.getNamaHadiah());
        comboJenisHadiah.setValue(hadiah.getJenisHadiah());
        txtDeskripsi.setText(hadiah.getDeskripsiHadiah());
        txtBiayaPoin.setText(String.valueOf(hadiah.getBiayaPoin()));
        comboStatus.setValue(hadiah.getStatusHadiah());

        if ("VOUCHER".equals(hadiah.getJenisHadiah())) {
            txtNilaiVoucher.setText(String.valueOf(hadiah.getNilaiVoucher()));
        }
    }

    private void loadTableData() {
        hadiahList.setAll(hadiahDAO.getAllHadiah());
        hadiahTable.setItems(hadiahList);
    }

    @FXML
    private void handleSimpanButton() {
        if (txtNamaHadiah.getText().isEmpty() || comboJenisHadiah.getValue() == null || txtBiayaPoin.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error Validasi", "Nama, Jenis, dan Biaya Poin harus diisi!");
            return;
        }

        Hadiah hadiah = (hadiahTerpilih == null) ? new Hadiah() : hadiahTerpilih;

        try {
            hadiah.setNamaHadiah(txtNamaHadiah.getText());
            hadiah.setJenisHadiah(comboJenisHadiah.getValue());
            hadiah.setDeskripsiHadiah(txtDeskripsi.getText());
            hadiah.setBiayaPoin(Integer.parseInt(txtBiayaPoin.getText()));
            hadiah.setStatusHadiah(comboStatus.getValue() != null ? comboStatus.getValue() : "AKTIF");

            if ("VOUCHER".equals(hadiah.getJenisHadiah())) {
                if (txtNilaiVoucher.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error Validasi", "Nilai Voucher harus diisi untuk jenis VOUCHER!");
                    return;
                }
                hadiah.setNilaiVoucher(Double.parseDouble(txtNilaiVoucher.getText()));
            } else {
                hadiah.setNilaiVoucher(null); // Set null jika bukan voucher
            }

            if (hadiahTerpilih == null) {
                hadiahDAO.addHadiah(hadiah);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Hadiah baru berhasil ditambahkan.");
            } else {
                hadiahDAO.updateHadiah(hadiah);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Hadiah berhasil diupdate.");
            }
            loadTableData();
            clearForm();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error Input", "Biaya Poin dan Nilai Voucher harus berupa angka.");
        }
    }

    @FXML
    private void handleBatalButton() {
        clearForm();
    }

    private void clearForm() {
        hadiahTerpilih = null;
        hadiahTable.getSelectionModel().clearSelection();
        txtNamaHadiah.clear();
        comboJenisHadiah.getSelectionModel().clearSelection();
        txtDeskripsi.clear();
        txtBiayaPoin.clear();
        txtNilaiVoucher.clear();
        comboStatus.getSelectionModel().clearSelection();
        voucherFields.setVisible(false);
        voucherFields.setManaged(false);
    }

    // Metode handleHapusButton() dan handleKembali() tidak berubah
    @FXML
    private void handleHapusButton() {
        if (hadiahTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih hadiah yang ingin dihapus.");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus hadiah: " + hadiahTerpilih.getNamaHadiah() + "?", ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText("Konfirmasi Hapus");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                hadiahDAO.deleteHadiah(hadiahTerpilih.getIdHadiah());
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Hadiah berhasil dihapus.");
                loadTableData();
                clearForm();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Gagal Menghapus", "Tidak dapat menghapus hadiah ini karena mungkin sudah pernah ditukarkan oleh pelanggan.");
            }
        }
    }

    @FXML
    private void handleKembali(ActionEvent event) {
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