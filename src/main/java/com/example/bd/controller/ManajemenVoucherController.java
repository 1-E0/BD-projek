package com.example.bd.controller;

import com.example.bd.dao.VoucherDAO;
import com.example.bd.model.Voucher;
import com.example.bd.util.Navigasi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ManajemenVoucherController implements Initializable {

    @FXML private TableView<Voucher> voucherTable;
    @FXML private TableColumn<Voucher, String> colNama;
    @FXML private TableColumn<Voucher, Integer> colPoin;
    @FXML private TableColumn<Voucher, Double> colPotongan;
    @FXML private TableColumn<Voucher, Boolean> colStatus;

    @FXML private TextField txtNamaVoucher;
    @FXML private TextArea txtDeskripsi;
    @FXML private TextField txtPotonganHarga;
    @FXML private TextField txtPoinDibutuhkan;
    @FXML private CheckBox checkAktif;
    @FXML private Button btnSimpan;

    private final VoucherDAO voucherDAO = new VoucherDAO();
    private Voucher voucherTerpilih = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadTableData();

        voucherTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                voucherTerpilih = newSelection;
                fillForm(newSelection);
            } else {
                clearForm();
            }
        });
    }

    private void setupTableColumns() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaVoucher"));
        colPoin.setCellValueFactory(new PropertyValueFactory<>("poinDibutuhkan"));
        colPotongan.setCellValueFactory(new PropertyValueFactory<>("potonganHarga"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("aktif"));
    }

    private void loadTableData() {
        ObservableList<Voucher> list = FXCollections.observableArrayList(voucherDAO.getAllVouchers());
        voucherTable.setItems(list);
    }

    private void fillForm(Voucher voucher) {
        txtNamaVoucher.setText(voucher.getNamaVoucher());
        txtDeskripsi.setText(voucher.getDeskripsi());
        txtPotonganHarga.setText(String.format("%.0f", voucher.getPotonganHarga()));
        txtPoinDibutuhkan.setText(String.valueOf(voucher.getPoinDibutuhkan()));
        checkAktif.setSelected(voucher.isAktif());
        btnSimpan.setText("Update");
    }

    @FXML
    private void handleSimpan() {
        if (txtNamaVoucher.getText().isEmpty() || txtPotonganHarga.getText().isEmpty() || txtPoinDibutuhkan.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Nama, Potongan Harga, dan Poin harus diisi.");
            return;
        }

        try {
            Voucher voucher = (voucherTerpilih == null) ? new Voucher() : voucherTerpilih;
            voucher.setNamaVoucher(txtNamaVoucher.getText());
            voucher.setDeskripsi(txtDeskripsi.getText());
            voucher.setPotonganHarga(Double.parseDouble(txtPotonganHarga.getText()));
            voucher.setPoinDibutuhkan(Integer.parseInt(txtPoinDibutuhkan.getText()));
            voucher.setAktif(checkAktif.isSelected());

            if (voucherTerpilih == null) {
                voucherDAO.addVoucher(voucher);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Voucher baru berhasil ditambahkan.");
            } else {
                voucherDAO.updateVoucher(voucher);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Voucher berhasil diperbarui.");
            }

            loadTableData();
            clearForm();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error Input", "Potongan Harga dan Poin harus berupa angka.");
        }
    }

    @FXML
    private void handleBatal() {
        clearForm();
    }

    private void clearForm() {
        voucherTerpilih = null;
        voucherTable.getSelectionModel().clearSelection();
        txtNamaVoucher.clear();
        txtDeskripsi.clear();
        txtPotonganHarga.clear();
        txtPoinDibutuhkan.clear();
        checkAktif.setSelected(true);
        btnSimpan.setText("Simpan");
    }

    @FXML
    void handleKembali(ActionEvent event) {
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