package com.example.bd.dao;

import com.example.bd.model.Hadiah;
import com.example.bd.util.DatabaseConnection;
import java.math.BigDecimal; // <-- Tambahkan import ini
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HadiahDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public List<Hadiah> getAllHadiah() {
        List<Hadiah> hadiahList = new ArrayList<>();
        String sql = "SELECT * FROM hadiah ORDER BY biaya_poin ASC"; // Mengambil semua, aktif maupun non-aktif untuk admin
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Hadiah h = new Hadiah();
                h.setIdHadiah(rs.getInt("id_hadiah"));
                h.setNamaHadiah(rs.getString("nama_hadiah"));
                h.setJenisHadiah(rs.getString("jenis_hadiah"));
                h.setDeskripsiHadiah(rs.getString("deskripsi_hadiah"));
                h.setBiayaPoin(rs.getInt("biaya_poin"));
                h.setStatusHadiah(rs.getString("status_hadiah"));

                // --- PERBAIKAN UTAMA DI SINI ---
                // Ambil sebagai BigDecimal, lalu konversi ke Double jika tidak null
                BigDecimal nilaiVoucherDecimal = rs.getBigDecimal("nilai_voucher");
                if (nilaiVoucherDecimal != null) {
                    h.setNilaiVoucher(nilaiVoucherDecimal.doubleValue());
                } else {
                    h.setNilaiVoucher(null);
                }
                // --- AKHIR PERBAIKAN ---

                hadiahList.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hadiahList;
    }

    // ... (Sisa metode di HadiahDAO tidak perlu diubah, sudah benar)
    public Hadiah getHadiahById(int idHadiah) {
        String sql = "SELECT * FROM hadiah WHERE id_hadiah = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idHadiah);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Hadiah h = new Hadiah();
                h.setIdHadiah(rs.getInt("id_hadiah"));
                h.setNamaHadiah(rs.getString("nama_hadiah"));
                h.setJenisHadiah(rs.getString("jenis_hadiah"));
                h.setDeskripsiHadiah(rs.getString("deskripsi_hadiah"));
                h.setBiayaPoin(rs.getInt("biaya_poin"));
                BigDecimal nilaiVoucherDecimal = rs.getBigDecimal("nilai_voucher");
                if (nilaiVoucherDecimal != null) {
                    h.setNilaiVoucher(nilaiVoucherDecimal.doubleValue());
                }
                h.setStatusHadiah(rs.getString("status_hadiah"));
                return h;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public void addHadiah(Hadiah hadiah) {
        String sql = "INSERT INTO hadiah (nama_hadiah, jenis_hadiah, deskripsi_hadiah, biaya_poin, nilai_voucher, status_hadiah) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hadiah.getNamaHadiah());
            pstmt.setString(2, hadiah.getJenisHadiah());
            pstmt.setString(3, hadiah.getDeskripsiHadiah());
            pstmt.setInt(4, hadiah.getBiayaPoin());
            pstmt.setObject(5, hadiah.getNilaiVoucher());
            pstmt.setString(6, hadiah.getStatusHadiah());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateHadiah(Hadiah hadiah) {
        String sql = "UPDATE hadiah SET nama_hadiah = ?, jenis_hadiah = ?, deskripsi_hadiah = ?, biaya_poin = ?, nilai_voucher = ?, status_hadiah = ? WHERE id_hadiah = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hadiah.getNamaHadiah());
            pstmt.setString(2, hadiah.getJenisHadiah());
            pstmt.setString(3, hadiah.getDeskripsiHadiah());
            pstmt.setInt(4, hadiah.getBiayaPoin());
            pstmt.setObject(5, hadiah.getNilaiVoucher());
            pstmt.setString(6, hadiah.getStatusHadiah());
            pstmt.setInt(7, hadiah.getIdHadiah());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteHadiah(int idHadiah) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM penukaran_hadiah WHERE id_hadiah = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, idHadiah);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                String updateSql = "UPDATE hadiah SET status_hadiah = 'NONAKTIF' WHERE id_hadiah = ?";
                try(PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, idHadiah);
                    updateStmt.executeUpdate();
                }
                return;
            }
        }

        String deleteSql = "DELETE FROM hadiah WHERE id_hadiah = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setInt(1, idHadiah);
            pstmt.executeUpdate();
        }
    }
}