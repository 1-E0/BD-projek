package com.example.bd.dao;

import com.example.bd.model.Kota;
import com.example.bd.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KotaDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public List<Kota> getAllKota() {
        List<Kota> kotaList = new ArrayList<>();
        String sql = "SELECT * FROM kota ORDER BY nama_kota ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Kota k = new Kota();
                k.setIdKota(rs.getInt("id_kota"));
                k.setNamaKota(rs.getString("nama_kota"));
                kotaList.add(k);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return kotaList;
    }

    public void addKota(String namaKota) {
        String sql = "INSERT INTO kota (nama_kota) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, namaKota);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}