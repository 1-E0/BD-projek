package com.example.bd.dao;

import com.example.bd.model.Diskon;
import com.example.bd.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiskonDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public List<Diskon> getAllDiskon() {
        List<Diskon> diskonList = new ArrayList<>();
        String sql = "SELECT * FROM diskon ORDER BY id_diskon ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Diskon d = new Diskon();
                d.setIdDiskon(rs.getInt("id_diskon"));
                d.setNamaDiskon(rs.getString("nama_diskon"));
                d.setPersenDiskon(rs.getDouble("persen_diskon"));
                d.setSyaratDanKetentuanDiskon(rs.getString("syarat_dan_ketentuan_diskon"));
                d.setMinPembelian(rs.getDouble("min_pembelian")); // Ambil field baru
                Date tglMulai = rs.getDate("tanggal_mulai_diskon");
                if (tglMulai != null) d.setTanggalMulaiDiskon(tglMulai.toLocalDate());
                Date tglAkhir = rs.getDate("tanggal_akhir_diskon");
                if (tglAkhir != null) d.setTanggalAkhirDiskon(tglAkhir.toLocalDate());
                diskonList.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return diskonList;
    }

    public List<Diskon> getActiveDiscounts() {
        List<Diskon> diskonList = new ArrayList<>();
        String sql = "SELECT * FROM diskon WHERE CURRENT_DATE BETWEEN tanggal_mulai_diskon AND tanggal_akhir_diskon ORDER BY nama_diskon ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Diskon d = new Diskon();
                d.setIdDiskon(rs.getInt("id_diskon"));
                d.setNamaDiskon(rs.getString("nama_diskon"));
                d.setPersenDiskon(rs.getDouble("persen_diskon"));
                d.setMinPembelian(rs.getDouble("min_pembelian")); // Ambil field baru
                d.setSyaratDanKetentuanDiskon(rs.getString("syarat_dan_ketentuan_diskon"));
                d.setTanggalMulaiDiskon(rs.getDate("tanggal_mulai_diskon").toLocalDate());
                d.setTanggalAkhirDiskon(rs.getDate("tanggal_akhir_diskon").toLocalDate());
                diskonList.add(d);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return diskonList;
    }

    public void addDiskon(Diskon diskon) {
        String sql = "INSERT INTO diskon (nama_diskon, persen_diskon, syarat_dan_ketentuan_diskon, tanggal_mulai_diskon, tanggal_akhir_diskon, min_pembelian) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, diskon.getNamaDiskon());
            pstmt.setDouble(2, diskon.getPersenDiskon());
            pstmt.setString(3, diskon.getSyaratDanKetentuanDiskon());
            pstmt.setDate(4, Date.valueOf(diskon.getTanggalMulaiDiskon()));
            pstmt.setDate(5, Date.valueOf(diskon.getTanggalAkhirDiskon()));
            pstmt.setDouble(6, diskon.getMinPembelian()); // Tambahkan field baru
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void updateDiskon(Diskon diskon) {
        String sql = "UPDATE diskon SET nama_diskon = ?, persen_diskon = ?, syarat_dan_ketentuan_diskon = ?, tanggal_mulai_diskon = ?, tanggal_akhir_diskon = ?, min_pembelian = ? WHERE id_diskon = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, diskon.getNamaDiskon());
            pstmt.setDouble(2, diskon.getPersenDiskon());
            pstmt.setString(3, diskon.getSyaratDanKetentuanDiskon());
            pstmt.setDate(4, Date.valueOf(diskon.getTanggalMulaiDiskon()));
            pstmt.setDate(5, Date.valueOf(diskon.getTanggalAkhirDiskon()));
            pstmt.setDouble(6, diskon.getMinPembelian()); // Tambahkan field baru
            pstmt.setInt(7, diskon.getIdDiskon());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteDiskon(int idDiskon) {
        String sql = "DELETE FROM diskon WHERE id_diskon = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idDiskon);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}