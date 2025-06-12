package com.example.bd.dao;

import com.example.bd.model.MetodePembayaran;
import com.example.bd.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodePembayaranDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public List<MetodePembayaran> getAll() {
        List<MetodePembayaran> list = new ArrayList<>();
        String sql = "SELECT * FROM metode_pembayaran ORDER BY id_metode ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MetodePembayaran mp = new MetodePembayaran();
                mp.setIdMetode(rs.getInt("id_metode"));
                mp.setCaraMetode(rs.getString("cara_metode"));
                list.add(mp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void add(MetodePembayaran metodePembayaran) {
        String sql = "INSERT INTO metode_pembayaran (cara_metode) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, metodePembayaran.getCaraMetode());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(MetodePembayaran metodePembayaran) {
        String sql = "UPDATE metode_pembayaran SET cara_metode = ? WHERE id_metode = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, metodePembayaran.getCaraMetode());
            pstmt.setInt(2, metodePembayaran.getIdMetode());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int idMetode) {
        String sql = "DELETE FROM metode_pembayaran WHERE id_metode = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMetode);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}