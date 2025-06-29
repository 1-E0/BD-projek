package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.MenuDAO;
import com.example.bd.dao.PesananDAO;
import com.example.bd.dao.ReviewDAO;
import com.example.bd.model.*;
import com.example.bd.util.Navigasi;
import com.example.bd.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class RiwayatPesananItemController {

    @FXML private Label lblIdPesanan;
    @FXML private Label lblTotalHarga;
    @FXML private Label lblStatus;
    @FXML private Label lblTanggal;
    @FXML private Button btnBeriReview;
    @FXML private Button btnPesanLagi;

    private Pesanan pesanan;
    private final PesananDAO pesananDAO = new PesananDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final MenuDAO menuDAO = new MenuDAO();

    public void setData(Pesanan pesanan) {
        this.pesanan = pesanan;
        lblIdPesanan.setText("Pesanan #" + pesanan.getIdPesanan());
        lblTotalHarga.setText(String.format("Rp %.0f", pesanan.getTotalHargaPesanan()));

        String statusTampil = pesanan.getStatusPengiriman() != null ? pesanan.getStatusPengiriman() : pesanan.getStatusPembayaran();
        lblStatus.setText("Status: " + statusTampil);

        if (pesanan.getTanggalPesanan() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            lblTanggal.setText(sdf.format(pesanan.getTanggalPesanan()));
        } else {
            lblTanggal.setText("Tanggal tidak tersedia");
        }

        boolean sudahDireview = reviewDAO.hasBeenReviewed(pesanan.getIdPesanan());
        btnBeriReview.setVisible(!sudahDireview);
        btnBeriReview.setManaged(!sudahDireview);
    }

    @FXML
    private void handleBeriReview() {
        try {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Beri Ulasan untuk Pesanan #" + pesanan.getIdPesanan());

            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("view/ReviewDialog.fxml"));
            Parent root = loader.load();
            ReviewDialogController controller = loader.getController();

            dialog.getDialogPane().setContent(root);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);

            dialog.showAndWait();

            // --- PERUBAHAN LOGIKA UTAMA ADA DI SINI ---
            // Sekarang kita periksa apakah tombol "Kirim" yang benar-benar ditekan
            if (controller.isSubmitted()) {
                Review review = new Review();
                review.setIdPelanggan(UserSession.getInstance().getLoggedInPelanggan().getIdPelanggan());
                review.setIdPesanan(pesanan.getIdPesanan());
                review.setRating(controller.getRating());
                review.setKomentar(controller.getKomentar());

                reviewDAO.addReview(review);

                showAlert(Alert.AlertType.INFORMATION, "Terima Kasih", "Ulasan Anda telah kami terima!");
                btnBeriReview.setVisible(false);
                btnBeriReview.setManaged(false);
            }
            // Jika dialog ditutup dengan "X", controller.isSubmitted() akan false, dan blok ini akan dilewati.

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handlePesanLagi(ActionEvent event) {
        List<DetailPesanan> detailLama = pesananDAO.getDetailByPesanan(pesanan.getIdPesanan());
        UserSession.getInstance().clearKeranjang();

        StringBuilder itemTidakTersedia = new StringBuilder();
        int itemDitambahkan = 0;

        // HAPUS: final int ID_CABANG_PADRAO = 1;
        int idCabangAktif = UserSession.getInstance().getSelectedCabangId(); // <-- AMBIL CABANG DARI SESI

        for (DetailPesanan detail : detailLama) {
            Menu menuLama = menuDAO.getMenuById(detail.getIdMenu());
            if (menuLama == null) continue;

            MenuHarian menuHariIni = menuDAO.findMenuHarianByMenuId(detail.getIdMenu(), idCabangAktif); // <-- GUNAKAN idCabangAktif

            if (menuHariIni == null) {
                itemTidakTersedia.append("- ").append(menuLama.getNamaMenu()).append(" (tidak tersedia di cabang ini)\n");
                continue;
            }

            if (menuHariIni.getStokMenuHarian() < detail.getJumlah()) {
                itemTidakTersedia.append("- ").append(menuLama.getNamaMenu()).append(" (stok tidak cukup)\n");
                continue;
            }

            UserSession.getInstance().addItemToCart(menuLama, detail.getJumlah(), menuHariIni.getIdMenuHarian());
            itemDitambahkan++;
        }

        if (itemDitambahkan > 0) {
            String pesanSukses = itemDitambahkan + " item berhasil ditambahkan kembali ke keranjang.";
            if (itemTidakTersedia.length() > 0) {
                pesanSukses += "\n\nItem berikut tidak dapat ditambahkan:\n" + itemTidakTersedia.toString();
            }
            showAlert(Alert.AlertType.INFORMATION, "Sukses Sebagian", pesanSukses);
        } else {
            String pesanGagal = "Semua item dari pesanan ini tidak tersedia saat ini.\n\nAlasan:\n" + itemTidakTersedia.toString();
            showAlert(Alert.AlertType.WARNING, "Gagal Memesan Lagi", pesanGagal);
            return;
        }
        Navigasi.switchScene(event, "BuatPesananView.fxml");
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}