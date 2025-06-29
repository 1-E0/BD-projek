package com.example.bd.controller;

import com.example.bd.dao.CabangDAO;
import com.example.bd.dao.StaffDAO;
import com.example.bd.model.Cabang;
import com.example.bd.model.Staff;
import com.example.bd.util.Navigasi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManajemenStaffController implements Initializable {

    @FXML private AnchorPane rootPane;
    @FXML private TableView<Staff> staffTable;
    @FXML private TableColumn<Staff, Integer> colId;
    @FXML private TableColumn<Staff, String> colNama;
    @FXML private TableColumn<Staff, String> colJabatan;
    @FXML private TableColumn<Staff, String> colCabang;
    @FXML private TableColumn<Staff, String> colTelepon;
    @FXML private TableColumn<Staff, String> colAlamat;
    @FXML private TextField txtNama;
    @FXML private TextField txtJabatan;
    @FXML private ComboBox<Cabang> comboCabang;
    @FXML private TextField txtTelepon;
    @FXML private TextField txtAlamat;
    @FXML private Button btnSimpan;
    @FXML private Label formTitleLabel;

    private final StaffDAO staffDAO = new StaffDAO();
    private final CabangDAO cabangDAO = new CabangDAO();
    private Staff staffTerpilih = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        populateCabangComboBox();
        setupTableSelectionListener();
        loadStaffData();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idStaff"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaStaff"));
        colJabatan.setCellValueFactory(new PropertyValueFactory<>("jabatanStaff"));
        colCabang.setCellValueFactory(new PropertyValueFactory<>("namaCabang"));
        colTelepon.setCellValueFactory(new PropertyValueFactory<>("noTelpStaff"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamatStaff"));
    }

    private void loadStaffData() {
        ObservableList<Staff> staffList = FXCollections.observableArrayList(staffDAO.getAllStaff());
        staffTable.setItems(staffList);
    }

    private void populateCabangComboBox() {
        comboCabang.setItems(FXCollections.observableArrayList(cabangDAO.getAllCabang()));
    }

    private void setupTableSelectionListener() {
        staffTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                staffTerpilih = newSelection;
                txtNama.setText(staffTerpilih.getNamaStaff());
                txtJabatan.setText(staffTerpilih.getJabatanStaff());
                txtTelepon.setText(staffTerpilih.getNoTelpStaff());
                txtAlamat.setText(staffTerpilih.getAlamatStaff());
                for (Cabang cabang : comboCabang.getItems()) {
                    if (cabang.getIdCabang() == staffTerpilih.getIdCabang()) {
                        comboCabang.setValue(cabang);
                        break;
                    }
                }
                formTitleLabel.setText("Form Edit Staff");
                btnSimpan.setText("Update");
            }
        });
    }

    @FXML
    private void handleSimpanButton() {
        if (txtNama.getText().isEmpty() || comboCabang.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error Validasi", "Nama dan Cabang wajib diisi!");
            return;
        }

        if (staffTerpilih == null) {
            Staff newStaff = new Staff();
            fillStaffFromForm(newStaff);
            staffDAO.addStaff(newStaff);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Staff baru berhasil ditambahkan!");
        } else {
            fillStaffFromForm(staffTerpilih);
            staffDAO.updateStaff(staffTerpilih);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data staff berhasil diupdate!");
        }
        loadStaffData();
        clearFormAndSelection();
    }

    private void fillStaffFromForm(Staff staff) {
        staff.setNamaStaff(txtNama.getText());
        staff.setJabatanStaff(txtJabatan.getText());
        staff.setIdCabang(comboCabang.getValue().getIdCabang());
        staff.setNoTelpStaff(txtTelepon.getText());
        staff.setAlamatStaff(txtAlamat.getText());
    }

    @FXML
    private void handleHapusButton() {
        if (staffTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih staff yang ingin dihapus.");
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Konfirmasi Hapus");
        confirmationAlert.setHeaderText("Hapus staff: " + staffTerpilih.getNamaStaff() + "?");
        confirmationAlert.setContentText("Apakah Anda yakin?");
        Optional<ButtonType> response = confirmationAlert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            staffDAO.deleteStaff(staffTerpilih.getIdStaff());
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data staff telah dihapus.");
            loadStaffData();
            clearFormAndSelection();
        }
    }

    @FXML
    private void handleBatalButton() {
        clearFormAndSelection();
    }

    @FXML
    private void handleKembali(ActionEvent event) throws IOException {
        Navigasi.goBack(event, "DashboardView.fxml");
    }

    private void clearFormAndSelection() {
        staffTerpilih = null;
        staffTable.getSelectionModel().clearSelection();
        txtNama.clear();
        txtJabatan.clear();
        txtTelepon.clear();
        txtAlamat.clear();
        comboCabang.setValue(null);
        formTitleLabel.setText("Form Tambah Staff Baru");
        btnSimpan.setText("Simpan");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}