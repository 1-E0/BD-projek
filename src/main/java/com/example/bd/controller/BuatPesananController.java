package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.DiskonDAO;
import com.example.bd.dao.MenuDAO;
import com.example.bd.dao.PesananDAO;
import com.example.bd.dao.VoucherDAO;
import com.example.bd.model.*;
import com.example.bd.model.Menu;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BuatPesananController implements Initializable {

    // --- FXML Components ---
    @FXML private TableView<MenuHarian> menuTable;
    @FXML private TableColumn<MenuHarian, String> colNamaMenu;
    @FXML private TableColumn<MenuHarian, Double> colHargaMenu;
    @FXML private TableColumn<MenuHarian, Integer> colStok;
    @FXML private TableView<ItemKeranjang> keranjangTable;
    @FXML private TableColumn<ItemKeranjang, String> colKeranjangNama;
    @FXML private TableColumn<ItemKeranjang, Integer> colKeranjangKuantitas;
    @FXML private TableColumn<ItemKeranjang, Double> colKeranjangSubtotal;
    @FXML private TextArea txtAlamat;
    @FXML private Label lblNamaPelanggan;
    @FXML private ComboBox<Diskon> comboDiskon;
    @FXML private ComboBox<VoucherPelanggan> comboVoucher;
    @FXML private Label lblSubtotal;
    @FXML private Label lblJumlahDiskon;
    @FXML private Label lblTotalAkhir;

    // --- DAOs ---
    private final MenuDAO menuDAO = new MenuDAO();
    private final PesananDAO pesananDAO = new PesananDAO();
    private final DiskonDAO diskonDAO = new DiskonDAO();
    private final VoucherDAO voucherDAO = new VoucherDAO();

    // --- Data Lists & Models ---
    private final ObservableList<MenuHarian> menuList = FXCollections.observableArrayList();
    private ObservableList<ItemKeranjang> keranjangList;
    private Pelanggan pelangganAktif;
    private VoucherPelanggan voucherTerpilih = null;

    private static final int ID_CABANG_PADRAO = 1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pelangganAktif = UserSession.getInstance().getLoggedInPelanggan();
        this.keranjangList = UserSession.getInstance().getKeranjang();

        keranjangList.addListener((ListChangeListener<ItemKeranjang>) c -> updateTotalHarga());

        if (pelangganAktif != null) {
            lblNamaPelanggan.setText(pelangganAktif.getNamaPelanggan());
            if (txtAlamat.getText() == null || txtAlamat.getText().isEmpty()) {
                txtAlamat.setText(pelangganAktif.getAlamatPelanggan());
            }
        } else {
            lblNamaPelanggan.setText("Error: Tidak ada pelanggan login!");
        }

        setupMenuTable();
        setupKeranjangTable();
        setupDiskonComboBox();
        setupVoucherComboBox();
        loadInitialData();
        updateTotalHarga();
    }

    private void setupMenuTable() {
        colNamaMenu.setCellValueFactory(new PropertyValueFactory<>("namaMenu"));
        colHargaMenu.setCellValueFactory(new PropertyValueFactory<>("hargaMenu"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stokMenuHarian"));
    }

    private void setupKeranjangTable() {
        colKeranjangNama.setCellValueFactory(new PropertyValueFactory<>("namaMenu"));
        colKeranjangKuantitas.setCellValueFactory(new PropertyValueFactory<>("kuantitas"));
        colKeranjangSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        keranjangTable.setItems(this.keranjangList);
    }

    private void setupDiskonComboBox() {
        comboDiskon.setConverter(new StringConverter<>() {
            @Override
            public String toString(Diskon diskon) {
                return diskon == null ? "Tanpa Diskon" : diskon.getNamaDiskon() + " (" + diskon.getPersenDiskon() + "%)";
            }
            @Override
            public Diskon fromString(String string) { return null; }
        });

        comboDiskon.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            comboVoucher.setDisable(newVal != null);
            if(newVal != null) comboVoucher.getSelectionModel().clearSelection();
            updateTotalHarga();
        });
    }

    private void setupVoucherComboBox() {
        refreshVoucherComboBox();

        comboVoucher.setConverter(new StringConverter<>() {
            @Override
            public String toString(VoucherPelanggan voucher) {
                if (voucher == null) {
                    return "Tanpa Voucher";
                } else {
                    return String.format("%s (Diskon %.0f%%)", voucher.getNamaHadiah(), voucher.getNilaiVoucher());
                }
            }
            @Override
            public VoucherPelanggan fromString(String string) { return null; }
        });

        comboVoucher.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            this.voucherTerpilih = newVal;
            comboDiskon.setDisable(newVal != null);
            if (newVal != null) comboDiskon.getSelectionModel().clearSelection();
            updateTotalHarga();
        });
    }

    private void refreshMenuTable() {
        menuList.clear();
        menuList.addAll(menuDAO.getMenuHarianByKategori(1, ID_CABANG_PADRAO));
        menuList.addAll(menuDAO.getMenuHarianByKategori(2, ID_CABANG_PADRAO));
        menuList.addAll(menuDAO.getMenuHarianByKategori(3, ID_CABANG_PADRAO));
        menuTable.setItems(menuList);
    }

    private void refreshVoucherComboBox() {
        VoucherPelanggan selected = comboVoucher.getSelectionModel().getSelectedItem();
        ObservableList<VoucherPelanggan> vouchers = FXCollections.observableArrayList(
                voucherDAO.getVoucherTersediaByPelanggan(pelangganAktif.getIdPelanggan())
        );
        vouchers.add(0, null);
        comboVoucher.setItems(vouchers);
        if (selected != null && comboVoucher.getItems().contains(selected)) {
            comboVoucher.getSelectionModel().select(selected);
        } else {
            comboVoucher.getSelectionModel().select(null);
        }
    }

    private void loadInitialData() {
        refreshMenuTable();
        refreshVoucherComboBox();
        ObservableList<Diskon> diskonAktifList = FXCollections.observableArrayList(diskonDAO.getActiveDiscounts());
        diskonAktifList.add(0, null);
        comboDiskon.setItems(diskonAktifList);
    }

    @FXML
    void handleTambahKeKeranjang(ActionEvent event) {
        MenuHarian menuTerpilih = menuTable.getSelectionModel().getSelectedItem();
        if (menuTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih menu dari daftar terlebih dahulu!");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Input Kuantitas");
        dialog.setHeaderText("Masukkan jumlah untuk: " + menuTerpilih.getNamaMenu());
        dialog.setContentText("Jumlah:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(qtyStr -> {
            try {
                int kuantitasDiminta = Integer.parseInt(qtyStr);
                if (kuantitasDiminta <= 0) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Kuantitas harus lebih dari 0.");
                    return;
                }
                int stokTersedia = menuTerpilih.getStokMenuHarian();
                int kuantitasDiKeranjang = 0;
                for (ItemKeranjang item : keranjangList) {
                    if (item.getIdMenuHarian() == menuTerpilih.getIdMenuHarian()) {
                        kuantitasDiKeranjang = item.getKuantitas();
                        break;
                    }
                }
                if (kuantitasDiminta + kuantitasDiKeranjang > stokTersedia) {
                    showAlert(Alert.AlertType.ERROR, "Stok Tidak Cukup",
                            "Jumlah melebihi stok yang tersedia.\nStok tersedia: " + stokTersedia +
                                    "\nSudah ada di keranjang: " + kuantitasDiKeranjang);
                    return;
                }
                Menu menuBase = menuDAO.getMenuById(menuTerpilih.getIdMenu());
                UserSession.getInstance().addItemToCart(menuBase, kuantitasDiminta, menuTerpilih.getIdMenuHarian());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Kuantitas harus berupa angka.");
            }
        });
    }

    @FXML
    void handleHapusDariKeranjang(ActionEvent event) {
        ItemKeranjang itemTerpilih = keranjangTable.getSelectionModel().getSelectedItem();
        if (itemTerpilih == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Pilih item di keranjang yang ingin dihapus.");
            return;
        }
        keranjangList.remove(itemTerpilih);
    }

    private void updateTotalHarga() {
        double subtotal = 0;
        for (ItemKeranjang item : this.keranjangList) {
            subtotal += item.getSubtotal();
        }
        double jumlahDiskon = 0;
        if (voucherTerpilih != null) {
            jumlahDiskon = subtotal * (voucherTerpilih.getNilaiVoucher() / 100.0);
        } else if (comboDiskon.getValue() != null) {
            jumlahDiskon = subtotal * (comboDiskon.getValue().getPersenDiskon() / 100.0);
        }
        double totalAkhir = subtotal - jumlahDiskon;
        lblSubtotal.setText(String.format("Rp %.0f", subtotal));
        lblJumlahDiskon.setText(String.format("- Rp %.0f", jumlahDiskon));
        lblTotalAkhir.setText(String.format("Rp %.0f", totalAkhir));
    }

    @FXML
    void handleBuatPesanan(ActionEvent event) {
        if (pelangganAktif == null) { showAlert(Alert.AlertType.ERROR, "Error Kritis", "Tidak ada data pelanggan yang login."); return; }
        if (keranjangList.isEmpty()) { showAlert(Alert.AlertType.ERROR, "Error", "Keranjang pesanan tidak boleh kosong."); return; }
        if (txtAlamat.getText().trim().isEmpty()) { showAlert(Alert.AlertType.ERROR, "Error", "Alamat pengiriman harus diisi."); return; }

        double subtotal = 0;
        for (ItemKeranjang item : keranjangList) { subtotal += item.getSubtotal(); }
        double jumlahDiskon = 0;
        if (voucherTerpilih != null) {
            jumlahDiskon = subtotal * (voucherTerpilih.getNilaiVoucher() / 100.0);
        } else if (comboDiskon.getValue() != null) {
            jumlahDiskon = subtotal * (comboDiskon.getValue().getPersenDiskon() / 100.0);
        }
        double totalHargaFinal = subtotal - jumlahDiskon;

        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("view/PembayaranDialog.fxml"));
            Parent root = loader.load();
            PembayaranDialogController controller = loader.getController();
            controller.setTotal(totalHargaFinal);
            dialog.getDialogPane().setContent(root);
            dialog.showAndWait();
            MetodePembayaran metode = controller.getMetodeTerpilih();
            if (metode != null) {
                Pesanan pesanan = new Pesanan();
                pesanan.setIdPelanggan(pelangganAktif.getIdPelanggan());
                pesanan.setAlamatTujuan(txtAlamat.getText());
                pesanan.setStatusPembayaran("Lunas");
                pesanan.setTotalHargaPesanan(totalHargaFinal);
                List<DetailPesanan> detailList = new ArrayList<>();
                for (ItemKeranjang item : this.keranjangList) {
                    DetailPesanan detail = new DetailPesanan();
                    detail.setIdMenuHarian(item.getIdMenuHarian());
                    detail.setJumlah(item.getKuantitas());
                    detail.setHargaProduk(item.getHargaMenu());
                    detailList.add(detail);
                }
                Pembayaran pembayaran = new Pembayaran();
                pembayaran.setIdMetode(metode.getIdMetode());
                pesananDAO.simpanPesananLengkap(pesanan, detailList, pembayaran);
                if (voucherTerpilih != null) {
                    voucherDAO.gunakanVoucher(voucherTerpilih.getIdVoucherPelanggan());
                }
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pesanan baru berhasil dibuat!");
                UserSession.getInstance().clearKeranjang();
                clearAllForm();
                refreshMenuTable();
                refreshVoucherComboBox();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menyimpan pesanan ke database.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleKembali(ActionEvent event) {
        Navigasi.goBack(event, "PelangganDashboardView.fxml");
    }

    private void clearAllForm() {
        if (pelangganAktif != null) {
            txtAlamat.setText(pelangganAktif.getAlamatPelanggan());
        }
        menuTable.getSelectionModel().clearSelection();
        keranjangTable.getSelectionModel().clearSelection();
        comboDiskon.getSelectionModel().clearSelection();
        comboVoucher.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}