package com.example.bd.dao;

import com.example.bd.model.Menu;
import com.example.bd.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public void addMenu(Menu menu) {
        String sql = "INSERT INTO menu (id_kategori, nama_menu, harga_menu, gambar_menu) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, menu.getIdKategori());
            pstmt.setString(2, menu.getNamaMenu());
            pstmt.setDouble(3, menu.getHargaMenu());
            pstmt.setString(4, menu.getGambarMenu());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Menu> searchMenu(String keyword) {
        List<Menu> menuList = new ArrayList<>();
        // ILIKE adalah versi case-insensitive dari LIKE di PostgreSQL
        // Tanda '%' berarti cocok dengan karakter apa pun (sebelum dan sesudah keyword)
        String sql = "SELECT * FROM menu WHERE nama_menu ILIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setIdMenu(rs.getInt("id_menu"));
                menu.setIdKategori(rs.getInt("id_kategori"));
                menu.setNamaMenu(rs.getString("nama_menu"));
                menu.setHargaMenu(rs.getDouble("harga_menu"));
                menu.setGambarMenu(rs.getString("gambar_menu"));
                menuList.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuList;
    }

    public List<Menu> getAllMenu() {
        List<Menu> menuList = new ArrayList<>();
        String sql = "SELECT * FROM menu ORDER BY id_menu ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setIdMenu(rs.getInt("id_menu"));
                menu.setIdKategori(rs.getInt("id_kategori"));
                menu.setNamaMenu(rs.getString("nama_menu"));
                menu.setHargaMenu(rs.getDouble("harga_menu"));
                menu.setGambarMenu(rs.getString("gambar_menu"));
                menuList.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuList;
    }

    public void updateMenu(Menu menu) {
        String sql = "UPDATE menu SET id_kategori = ?, nama_menu = ?, harga_menu = ? WHERE id_menu = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, menu.getIdKategori());
            pstmt.setString(2, menu.getNamaMenu());
            pstmt.setDouble(3, menu.getHargaMenu());
            pstmt.setInt(4, menu.getIdMenu());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMenu(int idMenu) {
        String sql = "DELETE FROM menu WHERE id_menu = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMenu);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // === METHOD BARU UNTUK MENGAMBIL MENU BERDASARKAN KATEGORI ===
    public List<Menu> getMenuByKategori(int idKategori) {
        List<Menu> menuList = new ArrayList<>();
        String sql = "SELECT * FROM menu WHERE id_kategori = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idKategori);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setIdMenu(rs.getInt("id_menu"));
                menu.setIdKategori(rs.getInt("id_kategori"));
                menu.setNamaMenu(rs.getString("nama_menu"));
                menu.setHargaMenu(rs.getDouble("harga_menu"));
                menu.setGambarMenu(rs.getString("gambar_menu"));
                menuList.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuList;
    }
    public Menu getMenuById(int idMenu) {
        String sql = "SELECT * FROM menu WHERE id_menu = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMenu);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Menu menu = new Menu();
                menu.setIdMenu(rs.getInt("id_menu"));
                menu.setIdKategori(rs.getInt("id_kategori"));
                menu.setNamaMenu(rs.getString("nama_menu"));
                menu.setHargaMenu(rs.getDouble("harga_menu"));
                menu.setGambarMenu(rs.getString("gambar_menu"));
                return menu;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}