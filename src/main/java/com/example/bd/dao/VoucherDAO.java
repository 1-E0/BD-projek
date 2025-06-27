package com.example.bd.dao;

import com.example.bd.model.VoucherPelanggan;
import com.example.bd.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public List<VoucherPelanggan> getVoucherTersediaByPelanggan(int idPelanggan) {
        List<VoucherPelanggan> voucherList = new ArrayList<>();
        String sql = "SELECT vp.*, h.nama_hadiah, h.nilai_voucher " +
                "FROM voucher_pelanggan vp " +
                "JOIN hadiah h ON vp.id_hadiah = h.id_hadiah " +
                "WHERE vp.id_pelanggan = ? AND vp.status_voucher = 'TERSEDIA' " +
                "AND (vp.tanggal_kadaluarsa IS NULL OR vp.tanggal_kadaluarsa >= CURRENT_DATE)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                VoucherPelanggan v = new VoucherPelanggan();
                v.setIdVoucherPelanggan(rs.getInt("id_voucher_pelanggan"));
                v.setKodeVoucher(rs.getString("kode_voucher"));
                v.setNamaHadiah(rs.getString("nama_hadiah"));
                v.setNilaiVoucher(rs.getDouble("nilai_voucher"));
                v.setTanggalKadaluarsa(rs.getDate("tanggal_kadaluarsa"));
                voucherList.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voucherList;
    }

    public void gunakanVoucher(int idVoucherPelanggan) {
        String sql = "UPDATE voucher_pelanggan SET status_voucher = 'TERPAKAI' WHERE id_voucher_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVoucherPelanggan);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}