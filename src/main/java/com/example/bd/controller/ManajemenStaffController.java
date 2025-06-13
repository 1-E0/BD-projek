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
    @FXML private TextField txtUmur; // Alterado de DatePicker
    @FXML private TextField txtAlamat;
    @FXML private Button btnSimpan;
    @FXML private Label formTitleLabel;

    private final StaffDAO staffDAO = new StaffDAO();
    private final CabangDAO cabangDAO = new CabangDAO();
    private final ObservableList<Staff> staffList = FXCollections.observableArrayList();
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
        staffList.clear();
        staffList.addAll(staffDAO.getAllStaff());
        staffTable.setItems(staffList);
    }

    private void populateCabangComboBox() {
        ObservableList<Cabang> cabangList = FXCollections.observableArrayList(cabangDAO.getAllCabang());
        comboCabang.setItems(cabangList);
    }

    private void setupTableSelectionListener() {
        staffTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                staffTerpilih = newSelection;
                txtNama.setText(staffTerpilih.getNamaStaff());
                txtJabatan.setText(staffTerpilih.getJabatanStaff());
                txtTelepon.setText(staffTerpilih.getNoTelpStaff());
                txtAlamat.setText(staffTerpilih.getAlamatStaff());
                txtUmur.setText(String.valueOf(staffTerpilih.getUmurStaff()));
                for (Cabang cabang : comboCabang.getItems()) {
                    if (cabang.getIdCabang() == staffTerpilih.getIdCabang()) {
                        comboCabang.setValue(cabang);
                        break;
                    }
                }
                formTitleLabel.setText("Formulário de Edição de Funcionário");
                btnSimpan.setText("Atualizar");
            }
        });
    }

    @FXML
    private void handleSimpanButton() {
        if (txtNama.getText().isEmpty() || comboCabang.getValue() == null || txtUmur.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Nome, Filial e Idade são obrigatórios!");
            return;
        }
        try {
            if (staffTerpilih == null) {
                Staff newStaff = new Staff();
                fillStaffFromForm(newStaff);
                staffDAO.addStaff(newStaff);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Novo funcionário adicionado com sucesso!");
            } else {
                fillStaffFromForm(staffTerpilih);
                staffDAO.updateStaff(staffTerpilih);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Dados do funcionário atualizados com sucesso!");
            }
            loadStaffData();
            clearFormAndSelection();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Entrada", "A idade deve ser um número válido.");
        }
    }

    private void fillStaffFromForm(Staff staff) {
        staff.setNamaStaff(txtNama.getText());
        staff.setJabatanStaff(txtJabatan.getText());
        staff.setIdCabang(comboCabang.getValue().getIdCabang());
        staff.setNoTelpStaff(txtTelepon.getText());
        staff.setAlamatStaff(txtAlamat.getText());
        staff.setUmurStaff(Integer.parseInt(txtUmur.getText()));
    }

    @FXML
    private void handleHapusButton() {
        if (staffTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Selecione um funcionário para deletar.");
            return;
        }
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmar Exclusão");
        confirmationAlert.setHeaderText("Deletar funcionário: " + staffTerpilih.getNamaStaff() + "?");
        confirmationAlert.setContentText("Você tem certeza?");
        Optional<ButtonType> response = confirmationAlert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            staffDAO.deleteStaff(staffTerpilih.getIdStaff());
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Dados do funcionário foram deletados.");
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
        txtUmur.clear();
        comboCabang.setValue(null);
        formTitleLabel.setText("Formulário de Adição de Novo Funcionário");
        btnSimpan.setText("Salvar");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}