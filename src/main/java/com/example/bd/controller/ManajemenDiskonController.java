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

    // O campo txtMinPembelian foi removido da UI

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
                formTitleLabel.setText("Formulário de Edição de Desconto");
                btnSimpan.setText("Atualizar");
            }
        });
    }

    @FXML
    private void handleSimpan(ActionEvent event) {
        if (txtNama.getText().isEmpty() || txtPersen.getText().isEmpty() || dateMulai.getValue() == null || dateAkhir.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Erro de Validação", "Todos os campos são obrigatórios!");
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
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Novo desconto adicionado com sucesso.");
            } else {
                diskonDAO.updateDiskon(diskon);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Desconto atualizado com sucesso.");
            }
            loadDiskonData();
            clearForm();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Entrada", "A porcentagem deve ser um número.");
        }
    }

    @FXML
    private void handleHapus(ActionEvent event) {
        if (diskonTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Selecione um desconto para deletar.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja deletar o desconto: " + diskonTerpilih.getNamaDiskon() + "?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirmar Exclusão");
        Optional<ButtonType> response = confirm.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.YES) {
            diskonDAO.deleteDiskon(diskonTerpilih.getIdDiskon());
            showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Desconto deletado com sucesso.");
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
        formTitleLabel.setText("Formulário de Adição de Novo Desconto");
        btnSimpan.setText("Salvar");
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