package com.example.bd.controller;

import com.example.bd.HelloApplication;
import com.example.bd.dao.MenuDAO;
import com.example.bd.dao.PesananDAO;
import com.example.bd.dao.ReviewDAO;
import com.example.bd.model.DetailPesanan;
import com.example.bd.model.Menu;
import com.example.bd.model.Pesanan;
import com.example.bd.model.Review;
import com.example.bd.util.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    private final MenuDAO menuDAO = new MenuDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    public void setData(Pesanan pesanan) {
        this.pesanan = pesanan;
        lblIdPesanan.setText("Pesanan #" + pesanan.getIdPesanan());
        lblTotalHarga.setText(String.format("Rp %.0f", pesanan.getTotalHargaPesanan()));

        // Menampilkan status pengiriman jika ada, jika tidak tampilkan status pembayaran
        String statusTampil = pesanan.getStatusPengiriman() != null ? pesanan.getStatusPengiriman() : pesanan.getStatusPembayaran();
        lblStatus.setText("Status: " + statusTampil);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        lblTanggal.setText(sdf.format(pesanan.getTanggalPesanan()));

        // Logika untuk menampilkan tombol review
        boolean sudahDireview = reviewDAO.hasBeenReviewed(pesanan.getIdPesanan());
        btnBeriReview.setVisible(!sudahDireview);
        btnBeriReview.setManaged(!sudahDireview);
    }

    // === PERBAIKAN UTAMA ADA DI METHOD INI ===
    @FXML
    private void handleBeriReview() {
        try {
            // 1. Buat Dialog baru
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Beri Ulasan untuk Pesanan #" + pesanan.getIdPesanan());

            // 2. Muat FXML dialog sebagai Parent
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("view/ReviewDialog.fxml"));
            Parent root = loader.load();

            // 3. Ambil controller dari dialog
            ReviewDialogController controller = loader.getController();

            // 4. Set konten dialog dengan FXML yang sudah dimuat
            dialog.getDialogPane().setContent(root);

            // 5. Hapus tombol default dan tunggu dialog ditutup oleh tombol di dalam FXML
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);

            dialog.showAndWait();

            // 6. Proses hasilnya jika pengguna menekan "Kirim" (rating > 0)
            if (controller.getRating() > 0) {
                Review review = new Review();
                review.setIdPelanggan(UserSession.getInstance().getLoggedInPelanggan().getIdPelanggan());
                review.setIdPesanan(pesanan.getIdPesanan());
                review.setReviewPesanan(controller.getRating());
                review.setKomentar(controller.getKomentar());

                reviewDAO.addReview(review);

                showAlert(Alert.AlertType.INFORMATION, "Terima Kasih", "Ulasan Anda telah kami terima!");
                btnBeriReview.setVisible(false);
                btnBeriReview.setManaged(false);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handlePesanLagi(ActionEvent event) {
        List<DetailPesanan> detailLama = pesananDAO.getDetailByPesanan(pesanan.getIdPesanan());
        UserSession.getInstance().clearKeranjang();
        for (DetailPesanan detail : detailLama) {
            Menu menu = menuDAO.getMenuById(detail.getIdMenu());
            if (menu != null) {
                UserSession.getInstance().addItemToCart(menu, detail.getKuantitas());
            }
        }
        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Semua item dari pesanan #" + pesanan.getIdPesanan() + " telah ditambahkan ke keranjang Anda.");
        try {
            URL resourceUrl = HelloApplication.class.getResource("view/BuatPesananView.fxml");
            Parent root = FXMLLoader.load(resourceUrl);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}