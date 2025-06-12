package com.example.bd.dao;

import com.example.bd.model.Review;
import com.example.bd.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    // Cek apakah sebuah pesanan sudah pernah di-review
    public boolean hasBeenReviewed(int idPesanan) {
        String sql = "SELECT COUNT(*) FROM review WHERE id_pesanan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPesanan);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Menambahkan review baru
    public void addReview(Review review) {
        String sql = "INSERT INTO review (id_pelanggan, id_pesanan, review_pesanan, komentar) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, review.getIdPelanggan());
            pstmt.setInt(2, review.getIdPesanan());
            pstmt.setInt(3, review.getReviewPesanan());
            pstmt.setString(4, review.getKomentar());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Review> getAllReviews() {
        List<Review> reviewList = new ArrayList<>();
        String sql = "SELECT r.*, p.nama_pelanggan " +
                "FROM review r " +
                "JOIN pelanggan p ON r.id_pelanggan = p.id_pelanggan " +
                "ORDER BY r.tanggal_review DESC";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Review r = new Review();
                r.setIdReview(rs.getInt("id_review"));
                r.setIdPesanan(rs.getInt("id_pesanan"));
                r.setNamaPelanggan(rs.getString("nama_pelanggan")); // Ambil dari JOIN
                r.setReviewPesanan(rs.getInt("review_pesanan"));
                r.setKomentar(rs.getString("komentar"));
                r.setTanggalReview(rs.getTimestamp("tanggal_review"));
                reviewList.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviewList;
    }
}