package com.example.bd.dao;

import com.example.bd.model.Cabang;
import com.example.bd.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CabangDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public List<Cabang> getAllCabang() {
        List<Cabang> cabangList = new ArrayList<>();
        String sql = "SELECT c.*, k.nama_kota FROM cabang c JOIN kota k ON c.id_kota = k.id_kota ORDER BY c.id_cabang ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cabang c = new Cabang();
                c.setIdCabang(rs.getInt("id_cabang"));
                c.setIdKota(rs.getInt("id_kota"));
                c.setAlamatCabang(rs.getString("alamat_cabang"));
                c.setNoTelpCabang(rs.getString("no_telp_cabang"));
                c.setNamaKota(rs.getString("nama_kota"));
                cabangList.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cabangList;
    }

    public void addCabang(Cabang cabang) {
        String sql = "INSERT INTO cabang (id_kota, alamat_cabang, no_telp_cabang) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cabang.getIdKota());
            pstmt.setString(2, cabang.getAlamatCabang());
            pstmt.setString(3, cabang.getNoTelpCabang());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCabang(Cabang cabang) {
        String sql = "UPDATE cabang SET id_kota = ?, alamat_cabang = ?, no_telp_cabang = ? WHERE id_cabang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cabang.getIdKota());
            pstmt.setString(2, cabang.getAlamatCabang());
            pstmt.setString(3, cabang.getNoTelpCabang());
            pstmt.setInt(4, cabang.getIdCabang());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCabang(int idCabang) {
        String sql = "DELETE FROM cabang WHERE id_cabang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCabang);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}