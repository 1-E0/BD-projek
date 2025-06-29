package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.CabangDAO;
import com.example.bd.dao.DiskonDAO;
import com.example.bd.dao.MenuDAO;
import com.example.bd.dao.PelangganDAO;
import com.example.bd.dao.PesananDAO;
import com.example.bd.model.*;
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
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BuatPesananController implements Initializable {

    // FXML Fields
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
    @FXML private ComboBox<PelangganVoucher> comboVoucher;
    @FXML private ComboBox<Cabang> comboCabang;
    @FXML private Label lblSubtotal;
    @FXML private Label lblJumlahDiskon;
    @FXML private Label lblTotalAkhir;
    @FXML private DatePicker tanggalPicker;
    @FXML private ComboBox<String> jamComboBox;

    // DAOs
    private final MenuDAO menuDAO = new MenuDAO();
    private final PesananDAO pesananDAO = new PesananDAO();
    private final DiskonDAO diskonDAO = new DiskonDAO();
    private final PelangganDAO pelangganDAO = new PelangganDAO();
    private final CabangDAO cabangDAO = new CabangDAO();

    // Data Collections
    private final ObservableList<MenuHarian> menuList = FXCollections.observableArrayList();
    private ObservableList<ItemKeranjang> keranjangList;
    private Pelanggan pelangganAktif;
    private PelangganVoucher voucherTerpilih = null;

    // Definisikan jam operasional
    private static final LocalTime JAM_BUKA = LocalTime.of(8, 0); // Jam 8 Pagi
    private static final LocalTime JAM_TUTUP = LocalTime.of(20, 0); // Jam 8 Malam

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pelangganAktif = UserSession.getInstance().getLoggedInPelanggan();
        this.keranjangList = UserSession.getInstance().getKeranjang();
        keranjangList.addListener((ListChangeListener<ItemKeranjang>) c -> updateTotalHarga());

        if (pelangganAktif != null) {
            lblNamaPelanggan.setText("Pemesan: " + pelangganAktif.getNamaPelanggan());
            txtAlamat.setText(pelangganAktif.getAlamatPelanggan());
        }

        setupMenuTable();
        setupKeranjangTable();
        setupDiskonComboBox();
        setupVoucherComboBox();
        setupCabangComboBox();
        setupJadwalControls();

        tanggalPicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            refreshMenuTableBasedOnSelection();
        });

        comboCabang.valueProperty().addListener((obs, oldCabang, newCabang) -> {
            refreshMenuTableBasedOnSelection();
        });


        loadInitialData();
        updateTotalHarga();
    }

    private void refreshMenuTableBasedOnSelection() {
        LocalDate tanggal = tanggalPicker.getValue();
        if (tanggal == null) {
            // Jika tanggal tidak dipilih, anggap hari ini untuk menampilkan menu
            tanggal = LocalDate.now();
        }
        refreshMenuTable(tanggal);
    }

    private void setupJadwalControls() {
        ObservableList<String> jamList = FXCollections.observableArrayList();
        for (int i = JAM_BUKA.getHour(); i <= JAM_TUTUP.getHour(); i++) {
            jamList.add(String.format("%02d:00", i));
        }
        jamComboBox.setItems(jamList);

        tanggalPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
    }

    private void setupCabangComboBox() {
        comboCabang.setItems(FXCollections.observableArrayList(cabangDAO.getAllCabang()));
        comboCabang.setConverter(new StringConverter<>() {
            @Override
            public String toString(Cabang cabang) {
                return cabang == null ? "Pilih Cabang" : cabang.getAlamatCabang() + " - " + cabang.getNamaKota();
            }
            @Override
            public Cabang fromString(String string) { return null; }
        });

        comboCabang.getSelectionModel().selectedItemProperty().addListener((obs, oldCabang, newCabang) -> {
            if (newCabang != null) {
                UserSession.getInstance().setSelectedCabang(newCabang);
                if (oldCabang != null && !oldCabang.equals(newCabang) && !keranjangList.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Mengganti cabang akan mengosongkan keranjang Anda. Lanjutkan?", ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES) {
                        UserSession.getInstance().clearKeranjang();
                    } else {
                        comboCabang.setValue(oldCabang);
                        return;
                    }
                }
                refreshMenuTableBasedOnSelection();
            }
        });

        Cabang cabangSesi = UserSession.getInstance().getSelectedCabang();
        if (cabangSesi != null) {
            comboCabang.setValue(cabangSesi);
        } else if (!comboCabang.getItems().isEmpty()) {
            comboCabang.getSelectionModel().selectFirst();
        }
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
            if (newVal != null) comboVoucher.getSelectionModel().clearSelection();
            updateTotalHarga();
        });
    }

    private void setupVoucherComboBox() {
        comboVoucher.setConverter(new StringConverter<>() {
            @Override
            public String toString(PelangganVoucher voucher) {
                return (voucher == null || voucher.getNamaVoucher() == null) ? "Tanpa Voucher" : voucher.getNamaVoucher();
            }
            @Override
            public PelangganVoucher fromString(String string) { return null; }
        });

        comboVoucher.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            this.voucherTerpilih = newVal;
            comboDiskon.setDisable(newVal != null);
            if (newVal != null) comboDiskon.getSelectionModel().clearSelection();
            updateTotalHarga();
        });
    }

    private void refreshMenuTable(LocalDate tanggal) {
        menuList.clear();
        Cabang cabangTerpilih = comboCabang.getValue();
        if (cabangTerpilih != null && tanggal != null) {
            menuList.addAll(menuDAO.getMenuHarianByDateAndBranch(tanggal, cabangTerpilih.getIdCabang()));
        }
        menuTable.setItems(menuList);
        menuTable.setPlaceholder(new Label("Tidak ada menu untuk tanggal & cabang ini."));
    }

    private void refreshVoucherComboBox() {
        if (pelangganAktif == null) return;
        ObservableList<PelangganVoucher> vouchers = FXCollections.observableArrayList(
                pelangganDAO.getVoucherAktifByPelanggan(pelangganAktif.getIdPelanggan())
        );
        vouchers.add(0, null);
        comboVoucher.setItems(vouchers);
        comboVoucher.getSelectionModel().selectFirst();
    }

    private void loadInitialData() {
        refreshVoucherComboBox();
        ObservableList<Diskon> diskonAktifList = FXCollections.observableArrayList(diskonDAO.getActiveDiscounts());
        diskonAktifList.add(0, null);
        comboDiskon.setItems(diskonAktifList);

        refreshMenuTable(LocalDate.now());
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
                com.example.bd.model.Menu menuBase = menuDAO.getMenuById(menuTerpilih.getIdMenu());
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
        Diskon diskonAktif = comboDiskon.getValue();

        if (voucherTerpilih != null) {
            jumlahDiskon = voucherTerpilih.getPotonganHarga();
        } else if (diskonAktif != null) {
            jumlahDiskon = subtotal * (diskonAktif.getPersenDiskon() / 100.0);
        }

        double totalAkhir = subtotal - jumlahDiskon;
        if (totalAkhir < 0) {
            totalAkhir = 0;
        }

        lblSubtotal.setText(String.format("Rp %.0f", subtotal));
        lblJumlahDiskon.setText(String.format("- Rp %.0f", jumlahDiskon));
        lblTotalAkhir.setText(String.format("Rp %.0f", totalAkhir));
    }

    @FXML
    void handleBuatPesanan(ActionEvent event) {
        if (keranjangList.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Keranjang pesanan tidak boleh kosong.");
            return;
        }
        if (txtAlamat.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Alamat pengiriman harus diisi.");
            return;
        }

        Timestamp jadwalPengiriman = null;
        LocalDate tanggalDipilih = tanggalPicker.getValue();
        String jamDipilih = jamComboBox.getValue();

        boolean isScheduled = tanggalDipilih != null && jamDipilih != null && !jamDipilih.isEmpty();

        if (isScheduled) {
            try {
                LocalTime waktuDipilih = LocalTime.parse(jamDipilih);
                LocalDateTime jadwalLengkap = LocalDateTime.of(tanggalDipilih, waktuDipilih);

                if (tanggalDipilih.isEqual(LocalDate.now()) && jadwalLengkap.isBefore(LocalDateTime.now().plusHours(1))) {
                    showAlert(Alert.AlertType.ERROR, "Jadwal Tidak Valid", "Untuk pengiriman hari ini, waktu pengiriman minimal 1 jam dari sekarang.");
                    return;
                }
                jadwalPengiriman = Timestamp.valueOf(jadwalLengkap);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Format Jadwal Salah", "Terjadi kesalahan pada format jadwal.");
                return;
            }
        } else {
            // Logika untuk "Kirim Sekarang Juga" (jika jadwal tidak diisi)
            LocalTime sekarang = LocalTime.now();
            if (sekarang.isBefore(JAM_BUKA) || sekarang.isAfter(JAM_TUTUP)) {
                showAlert(Alert.AlertType.WARNING, "Toko Sedang Tutup",
                        "Pemesanan langsung hanya dapat dilakukan selama jam operasional ("
                                + JAM_BUKA + " - " + JAM_TUTUP + ").\nSilakan pilih jadwal pengiriman untuk besok.");
                return;
            }
            // Jika jadwal null, akan dianggap "Kirim Sekarang"
        }

        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("view/PembayaranDialog.fxml"));
            Parent root = loader.load();
            PembayaranDialogController controller = loader.getController();

            String totalText = lblTotalAkhir.getText().replaceAll("[^\\d]", "");
            double totalHargaFinal = Double.parseDouble(totalText);

            controller.setTotal(totalHargaFinal);
            dialog.getDialogPane().setContent(root);
            dialog.setTitle("Konfirmasi Pembayaran");
            dialog.showAndWait();

            MetodePembayaran metode = controller.getMetodeTerpilih();
            if (metode != null) {
                Pesanan pesanan = new Pesanan();
                pesanan.setIdPelanggan(pelangganAktif.getIdPelanggan());
                pesanan.setAlamatTujuan(txtAlamat.getText());
                pesanan.setTotalHargaPesanan(totalHargaFinal);
                pesanan.setStatusPembayaran("Lunas");
                pesanan.setJadwalPengiriman(jadwalPengiriman);

                List<DetailPesanan> detailList = new ArrayList<>();
                for (ItemKeranjang item : keranjangList) {
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
                    pelangganDAO.gunakanVoucher(voucherTerpilih.getIdPelangganVoucher());
                }

                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Pesanan baru berhasil dibuat!");
                UserSession.getInstance().clearKeranjang();
                clearAllForm();
            }
        } catch (IOException | SQLException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memproses pesanan. Terjadi kesalahan.");
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
        keranjangList.clear();
        menuTable.getSelectionModel().clearSelection();
        keranjangTable.getSelectionModel().clearSelection();
        comboDiskon.getSelectionModel().selectFirst();
        comboVoucher.getSelectionModel().selectFirst();
        tanggalPicker.setValue(null);
        jamComboBox.setValue(null);
        updateTotalHarga();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}