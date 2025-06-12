package com.example.bd.dao;

import com.example.bd.model.Kategori;
import com.example.bd.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public List<Kategori> getAllKategori() {
        List<Kategori> kategoriList = new ArrayList<>();
        String sql = "SELECT * FROM kategori ORDER BY id_kategori ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Kategori k = new Kategori();
                k.setIdKategori(rs.getInt("id_kategori"));
                k.setNamaKategori(rs.getString("nama_kategori"));
                kategoriList.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return kategoriList;
    }

    public void addKategori(Kategori kategori) {
        String sql = "INSERT INTO kategori (nama_kategori) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kategori.getNamaKategori());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateKategori(Kategori kategori) {
        String sql = "UPDATE kategori SET nama_kategori = ? WHERE id_kategori = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kategori.getNamaKategori());
            pstmt.setInt(2, kategori.getIdKategori());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteKategori(int idKategori) {
        String sql = "DELETE FROM kategori WHERE id_kategori = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idKategori);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}