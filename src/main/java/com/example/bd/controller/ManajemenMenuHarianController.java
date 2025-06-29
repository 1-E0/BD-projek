package com.example.bd.controller;

import com.example.bd.dao.CabangDAO;
import com.example.bd.dao.MenuDAO;
import com.example.bd.model.Admin;
import com.example.bd.model.Cabang;
import com.example.bd.model.Menu;
import com.example.bd.model.MenuHarian;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManajemenMenuHarianController implements Initializable {

    @FXML private TableView<Menu> masterMenuTable;
    @FXML private TableColumn<Menu, Integer> colMasterId;
    @FXML private TableColumn<Menu, String> colMasterNama;
    @FXML private TableColumn<Menu, Double> colMasterHarga;

    @FXML private TableView<MenuHarian> menuHarianTable;
    @FXML private TableColumn<MenuHarian, String> colHarianNama;
    @FXML private TableColumn<MenuHarian, Integer> colHarianStok;
    @FXML private TableColumn<MenuHarian, Double> colHarianHarga;

    @FXML private Label lblMenuTerpilih;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Cabang> comboCabang;
    @FXML private TextField txtStok;


    @FXML private Button btnHapus;

    private final MenuDAO menuDAO = new MenuDAO();
    private final CabangDAO cabangDAO = new CabangDAO();
    private Menu menuTerpilih;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTables();
        populateControls();
        addListeners();


        Admin loggedInAdmin = UserSession.getInstance().getLoggedInAdmin();
        if (loggedInAdmin != null && "Cabang".equalsIgnoreCase(loggedInAdmin.getJenisAdmin())) {
            for (Cabang c : comboCabang.getItems()) {
                if (c.getIdCabang() == loggedInAdmin.getIdCabang()) {
                    comboCabang.setValue(c);
                    break;
                }
            }
            comboCabang.setDisable(true);
        }
        loadMenuHarianTable();
    }

    private void setupTables() {
        colMasterId.setCellValueFactory(new PropertyValueFactory<>("idMenu"));
        colMasterNama.setCellValueFactory(new PropertyValueFactory<>("namaMenu"));
        colMasterHarga.setCellValueFactory(new PropertyValueFactory<>("hargaMenu"));

        colHarianNama.setCellValueFactory(new PropertyValueFactory<>("namaMenu"));
        colHarianStok.setCellValueFactory(new PropertyValueFactory<>("stokMenuHarian"));
        colHarianHarga.setCellValueFactory(new PropertyValueFactory<>("hargaMenu"));
    }

    private void populateControls() {
        masterMenuTable.setItems(FXCollections.observableArrayList(menuDAO.getAllMenu()));
        comboCabang.setItems(FXCollections.observableArrayList(cabangDAO.getAllCabang()));
        datePicker.setValue(LocalDate.now());
        if (!comboCabang.getItems().isEmpty()) {
            comboCabang.getSelectionModel().selectFirst();
        }
    }

    private void addListeners() {
        masterMenuTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            menuTerpilih = newSelection;
            lblMenuTerpilih.setText(newSelection != null ? newSelection.getNamaMenu() : "-");
        });

        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> loadMenuHarianTable());
        comboCabang.valueProperty().addListener((obs, oldCabang, newCabang) -> loadMenuHarianTable());


        menuHarianTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnHapus.setDisable(newSelection == null);
        });
    }

    private void loadMenuHarianTable() {
        LocalDate tanggal = datePicker.getValue();
        Cabang cabang = comboCabang.getValue();

        if (tanggal != null && cabang != null) {
            ObservableList<MenuHarian> list = FXCollections.observableArrayList(
                    menuDAO.getMenuHarianByDateAndBranch(tanggal, cabang.getIdCabang())
            );
            menuHarianTable.setItems(list);
        } else {
            menuHarianTable.getItems().clear();
        }
    }

    @FXML
    void handleTambahMenuHarian(ActionEvent event) {
        if (menuTerpilih == null || datePicker.getValue() == null || comboCabang.getValue() == null || txtStok.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error Validasi", "Semua field (menu, tanggal, cabang, stok) harus diisi.");
            return;
        }

        try {
            int stok = Integer.parseInt(txtStok.getText());
            if (stok < 0) {
                showAlert(Alert.AlertType.ERROR, "Error Validasi", "Stok tidak boleh negatif.");
                return;
            }

            MenuHarian menuHarian = new MenuHarian();
            menuHarian.setIdMenu(menuTerpilih.getIdMenu());
            menuHarian.setIdCabang(comboCabang.getValue().getIdCabang());
            menuHarian.setTanggalMenuHarian(datePicker.getValue());
            menuHarian.setStokMenuHarian(stok);

            menuDAO.saveOrUpdateMenuHarian(menuHarian);

            loadMenuHarianTable();
            txtStok.clear();
            masterMenuTable.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error Input", "Stok harus berupa angka yang valid.");
        }
    }


    @FXML
    void handleHapusMenuHarian(ActionEvent event) {
        MenuHarian selectedMenuHarian = menuHarianTable.getSelectionModel().getSelectedItem();

        if (selectedMenuHarian == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih item dari tabel 'Menu Aktif' yang ingin dihapus.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Anda yakin ingin menghapus " + selectedMenuHarian.getNamaMenu() + " dari daftar menu harian?", ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText("Konfirmasi Hapus");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            menuDAO.deleteMenuHarian(selectedMenuHarian.getIdMenuHarian());
            loadMenuHarianTable();
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Menu berhasil dihapus dari daftar harian.");
        }
    }

    @FXML
    void handleKembali(ActionEvent event) {
        Navigasi.goBack(event, "DashboardView.fxml");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}