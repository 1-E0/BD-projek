package com.example.bd.dao;

import com.example.bd.util.DatabaseConnection;
import java.sql.*;

public class PengirimanDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    // Method ini akan INSERT jika belum ada, atau UPDATE jika sudah ada.
    public void saveOrUpdatePengiriman(int idPesanan, String status, String catatan) {
        // Pertama, cek apakah data pengiriman untuk pesanan ini sudah ada
        String checkSql = "SELECT COUNT(*) FROM pengiriman WHERE id_pesanan = ?";
        String updateSql = "UPDATE pengiriman SET status_pengiriman = ?, catatan_pengiriman = ? WHERE id_pesanan = ?";
        String insertSql = "INSERT INTO pengiriman (id_pesanan, status_pengiriman, catatan_pengiriman) VALUES (?, ?, ?)";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, idPesanan);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            boolean exists = rs.getInt(1) > 0;

            String sql = exists ? updateSql : insertSql;
            try (PreparedStatement saveStmt = conn.prepareStatement(sql)) {
                if (exists) {
                    saveStmt.setString(1, status);
                    saveStmt.setString(2, catatan);
                    saveStmt.setInt(3, idPesanan);
                } else {
                    saveStmt.setInt(1, idPesanan);
                    saveStmt.setString(2, status);
                    saveStmt.setString(3, catatan);
                }
                saveStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}