package com.example.bd.controller;

import com.example.bd.dao.AdminDAO;
import com.example.bd.dao.CabangDAO;
import com.example.bd.model.Admin;
import com.example.bd.model.Cabang;
import com.example.bd.util.Navigasi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManajemenAdminController implements Initializable {

    // FXML Table and Columns
    @FXML private TableView<Admin> adminTable;
    @FXML private TableColumn<Admin, Integer> colId;
    @FXML private TableColumn<Admin, String> colNama;
    @FXML private TableColumn<Admin, String> colEmail;
    @FXML private TableColumn<Admin, String> colJenis;
    @FXML private TableColumn<Admin, Integer> colCabang;
    @FXML private TableColumn<Admin, String> colStatus;

    // FXML Form Fields
    @FXML private Label formTitleLabel;
    @FXML private TextField txtNama;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtTelepon;
    @FXML private ComboBox<String> comboJenis;
    @FXML private ComboBox<Cabang> comboCabang;
    @FXML private Button btnSimpan;

    // DAOs and Data Lists
    private final AdminDAO adminDAO = new AdminDAO();
    private final CabangDAO cabangDAO = new CabangDAO();
    private Admin adminTerpilih = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        populateComboBoxes();
        setupTableSelectionListener();
        loadTableData();
        clearForm();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idAdmin"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaAdmin"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailAdmin"));
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenisAdmin"));
        colCabang.setCellValueFactory(new PropertyValueFactory<>("idCabang"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusAdmin"));
    }

    private void populateComboBoxes() {
        comboJenis.setItems(FXCollections.observableArrayList("Pusat", "Cabang"));
        comboCabang.setItems(FXCollections.observableArrayList(cabangDAO.getAllCabang()));

        comboCabang.setConverter(new StringConverter<>() {
            @Override
            public String toString(Cabang cabang) {
                return cabang == null ? "Tidak Ditugaskan" : cabang.getAlamatCabang();
            }
            @Override
            public Cabang fromString(String s) { return null; }
        });

        // Hanya aktifkan pilihan cabang jika jenisnya "Cabang"
        comboJenis.valueProperty().addListener((obs, oldVal, newVal) -> {
            comboCabang.setDisable(!"Cabang".equals(newVal));
            if (!"Cabang".equals(newVal)) {
                comboCabang.setValue(null);
            }
        });
    }

    private void loadTableData() {
        ObservableList<Admin> adminList = FXCollections.observableArrayList(adminDAO.getAllAdmins());
        adminTable.setItems(adminList);
    }

    private void setupTableSelectionListener() {
        adminTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                adminTerpilih = newSelection;
                fillFormFromSelection(newSelection);
            }
        });
    }

    private void fillFormFromSelection(Admin admin) {
        formTitleLabel.setText("Form Edit Admin");
        txtNama.setText(admin.getNamaAdmin());
        txtEmail.setText(admin.getEmailAdmin());
        txtEmail.setDisable(true); // Email tidak boleh diubah, sebagai primary key login
        txtTelepon.setText(admin.getNoTelpAdmin());
        comboJenis.setValue(admin.getJenisAdmin());

        if (admin.getIdCabang() != 0) {
            for (Cabang c : comboCabang.getItems()) {
                if (c.getIdCabang() == admin.getIdCabang()) {
                    comboCabang.setValue(c);
                    break;
                }
            }
        } else {
            comboCabang.setValue(null);
        }

        txtPassword.setPromptText("Biarkan kosong jika tidak ingin diubah");
        btnSimpan.setText("Update");
    }

    @FXML
    private void handleSimpan() {
        if (txtNama.getText().isEmpty() || txtEmail.getText().isEmpty() || comboJenis.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Nama, Email, dan Jenis Admin harus diisi.");
            return;
        }

        try {
            if (adminTerpilih == null) { // Mode Tambah
                if (txtPassword.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Validasi Gagal", "Password wajib diisi untuk admin baru.");
                    return;
                }
                Admin newAdmin = new Admin();
                fillAdminFromForm(newAdmin);
                newAdmin.setPasswordAdmin(txtPassword.getText());
                adminDAO.addAdmin(newAdmin);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Admin baru berhasil ditambahkan.");
            } else { // Mode Update
                fillAdminFromForm(adminTerpilih);
                adminDAO.updateAdmin(adminTerpilih);
                // Update password jika diisi
                if (!txtPassword.getText().isEmpty()) {
                    adminDAO.updatePasswordAdmin(adminTerpilih.getIdAdmin(), txtPassword.getText());
                }
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data admin berhasil diperbarui.");
            }
            loadTableData();
            clearForm();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat menyimpan data.");
            e.printStackTrace();
        }
    }

    private void fillAdminFromForm(Admin admin) {
        admin.setNamaAdmin(txtNama.getText());
        admin.setEmailAdmin(txtEmail.getText());
        admin.setNoTelpAdmin(txtTelepon.getText());
        admin.setJenisAdmin(comboJenis.getValue());
        admin.setStatusAdmin("Aktif"); // Default status
        if (comboCabang.getValue() != null) {
            admin.setIdCabang(comboCabang.getValue().getIdCabang());
        } else {
            admin.setIdCabang(0);
        }
    }

    @FXML
    private void handleBatal() {
        clearForm();
    }

    private void clearForm() {
        adminTerpilih = null;
        adminTable.getSelectionModel().clearSelection();
        formTitleLabel.setText("Form Tambah Admin Baru");
        txtNama.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtTelepon.clear();
        comboJenis.setValue(null);
        comboCabang.setValue(null);
        comboCabang.setDisable(true);
        txtEmail.setDisable(false);
        txtPassword.setPromptText("Wajib diisi untuk admin baru");
        btnSimpan.setText("Simpan");
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