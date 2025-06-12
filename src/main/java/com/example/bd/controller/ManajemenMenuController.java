package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.KategoriDAO;
import com.example.bd.dao.MenuDAO;
import com.example.bd.model.Kategori;
import com.example.bd.model.Menu;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManajemenMenuController implements Initializable {

    // FXML fields
    @FXML private TableView<Menu> menuTable;
    @FXML private TableColumn<Menu, Integer> colIdMenu;
    @FXML private TableColumn<Menu, String> colNamaMenu;
    @FXML private TableColumn<Menu, Double> colHargaMenu;
    @FXML private TextField txtNamaMenu;
    @FXML private TextField txtHargaMenu;
    @FXML private Button btnSimpan;
    @FXML private Label formTitleLabel;

    // PERUBAHAN TIPE DARI ComboBox MENJADI ChoiceBox
    @FXML private ChoiceBox<Kategori> choiceKategori;

    // DAOs dan list
    private final MenuDAO menuDAO = new MenuDAO();
    private final KategoriDAO kategoriDAO = new KategoriDAO();
    private final ObservableList<Menu> menuList = FXCollections.observableArrayList();
    private Menu menuTerpilih = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        populateKategoriChoiceBox(); // Panggil method baru
        setupTableSelectionListener();
        loadTableData();
    }

    private void setupTableColumns() {
        colIdMenu.setCellValueFactory(new PropertyValueFactory<>("idMenu"));
        colNamaMenu.setCellValueFactory(new PropertyValueFactory<>("namaMenu"));
        colHargaMenu.setCellValueFactory(new PropertyValueFactory<>("hargaMenu"));
    }

    private void loadTableData() {
        menuList.clear();
        menuList.addAll(menuDAO.getAllMenu());
        menuTable.setItems(menuList);
    }

    private void populateKategoriChoiceBox() {
        choiceKategori.setItems(FXCollections.observableArrayList(kategoriDAO.getAllKategori()));
        choiceKategori.setConverter(new StringConverter<>() {
            @Override
            public String toString(Kategori kategori) {
                return kategori == null ? "Pilih Kategori..." : kategori.getNamaKategori();
            }

            @Override
            public Kategori fromString(String s) {
                return null;
            }
        });
    }

    private void setupTableSelectionListener() {
        menuTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                menuTerpilih = newSelection;
                txtNamaMenu.setText(newSelection.getNamaMenu());
                txtHargaMenu.setText(String.format("%.0f", newSelection.getHargaMenu()));
                formTitleLabel.setText("Form Edit Menu");
                btnSimpan.setText("Update");

                // Logika baru untuk memilih kategori di ChoiceBox
                for (Kategori k : choiceKategori.getItems()) {
                    if (k.getIdKategori() == newSelection.getIdKategori()) {
                        choiceKategori.setValue(k);
                        break;
                    }
                }
            }
        });
    }

    @FXML
    private void handleSimpanButton() {
        if (txtNamaMenu.getText().isEmpty() || txtHargaMenu.getText().isEmpty() || choiceKategori.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error Validasi", "Nama, Harga, dan Kategori harus diisi!");
            return;
        }

        try {
            if (menuTerpilih == null) {
                Menu newMenu = new Menu();
                fillMenuFromForm(newMenu);
                menuDAO.addMenu(newMenu);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Menu baru berhasil ditambahkan!");
            } else {
                fillMenuFromForm(menuTerpilih);
                menuDAO.updateMenu(menuTerpilih);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Menu berhasil diupdate!");
            }
            loadTableData();
            clearFormAndSelection();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error Input", "Harga harus berupa angka yang valid!");
        }
    }

    private void fillMenuFromForm(Menu menu) {
        menu.setNamaMenu(txtNamaMenu.getText());
        menu.setHargaMenu(Double.parseDouble(txtHargaMenu.getText()));
        menu.setIdKategori(choiceKategori.getValue().getIdKategori());
    }

    @FXML
    private void handleHapusButton() {
        if (menuTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih menu yang ingin dihapus.");
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin menghapus menu: " + menuTerpilih.getNamaMenu() + "?", ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText("Konfirmasi Hapus");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            menuDAO.deleteMenu(menuTerpilih.getIdMenu());
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Menu berhasil dihapus.");
            loadTableData();
            clearFormAndSelection();
        }
    }

    @FXML
    private void handleBatalButton() {
        clearFormAndSelection();
    }

    private void clearFormAndSelection() {
        menuTerpilih = null;
        txtNamaMenu.clear();
        txtHargaMenu.clear();
        choiceKategori.setValue(null);
        menuTable.getSelectionModel().clearSelection();
        formTitleLabel.setText("Form Tambah Menu Baru");
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