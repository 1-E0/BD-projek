package com.example.bd.dao;

import com.example.bd.model.Menu;
import com.example.bd.model.MenuHarian;
import com.example.bd.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public void addMenu(Menu menu) {
        String sql = "INSERT INTO menu (id_kategori, nama_menu, harga_menu) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, menu.getIdKategori());
            pstmt.setString(2, menu.getNamaMenu());
            pstmt.setDouble(3, menu.getHargaMenu());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MenuHarian> searchMenuHarian(String keyword, int idCabang) {
        List<MenuHarian> menuHarianList = new ArrayList<>();
        String sql = "SELECT mh.*, m.nama_menu, m.harga_menu FROM menu_harian mh " +
                "JOIN menu m ON mh.id_menu = m.id_menu " +
                "WHERE m.nama_menu ILIKE ? AND mh.id_cabang = ? AND mh.tanggal_menu_harian = CURRENT_DATE AND mh.stok_menu_harian > 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setInt(2, idCabang);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MenuHarian mh = new MenuHarian();
                mh.setIdMenuHarian(rs.getInt("id_menu_harian"));
                mh.setIdMenu(rs.getInt("id_menu"));
                mh.setIdCabang(rs.getInt("id_cabang"));
                mh.setTanggalMenuHarian(rs.getDate("tanggal_menu_harian").toLocalDate());
                mh.setStokMenuHarian(rs.getInt("stok_menu_harian"));
                mh.setNamaMenu(rs.getString("nama_menu"));
                mh.setHargaMenu(rs.getDouble("harga_menu"));
                menuHarianList.add(mh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuHarianList;
    }
    public List<MenuHarian> getMenuHarianByDateAndBranch(LocalDate date, int idCabang) {
        List<MenuHarian> menuHarianList = new ArrayList<>();
        String sql = "SELECT mh.*, m.nama_menu, m.harga_menu " +
                "FROM menu_harian mh " +
                "JOIN menu m ON mh.id_menu = m.id_menu " +
                "WHERE mh.tanggal_menu_harian = ? AND mh.id_cabang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setInt(2, idCabang);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MenuHarian mh = new MenuHarian();
                mh.setIdMenuHarian(rs.getInt("id_menu_harian"));
                mh.setIdMenu(rs.getInt("id_menu"));
                mh.setIdCabang(rs.getInt("id_cabang"));
                mh.setTanggalMenuHarian(rs.getDate("tanggal_menu_harian").toLocalDate());
                mh.setStokMenuHarian(rs.getInt("stok_menu_harian"));
                // Mengambil data dari join dengan tabel menu
                mh.setNamaMenu(rs.getString("nama_menu"));
                mh.setHargaMenu(rs.getDouble("harga_menu"));
                menuHarianList.add(mh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuHarianList;
    }
    public void saveOrUpdateMenuHarian(MenuHarian menuHarian) {
        if (checkIfMenuHarianExists(menuHarian.getIdMenu(), menuHarian.getIdCabang(), menuHarian.getTanggalMenuHarian())) {

            updateStokMenuHarian(menuHarian);
        } else {

            addMenuHarian(menuHarian);
        }
    }
    private boolean checkIfMenuHarianExists(int idMenu, int idCabang, LocalDate tanggal) {
        String sql = "SELECT COUNT(*) FROM menu_harian WHERE id_menu = ? AND id_cabang = ? AND tanggal_menu_harian = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMenu);
            pstmt.setInt(2, idCabang);
            pstmt.setDate(3, java.sql.Date.valueOf(tanggal));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void addMenuHarian(MenuHarian menuHarian) {
        String sql = "INSERT INTO menu_harian (id_menu, id_cabang, tanggal_menu_harian, stok_menu_harian) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, menuHarian.getIdMenu());
            pstmt.setInt(2, menuHarian.getIdCabang());
            pstmt.setDate(3, java.sql.Date.valueOf(menuHarian.getTanggalMenuHarian()));
            pstmt.setInt(4, menuHarian.getStokMenuHarian());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void updateStokMenuHarian(MenuHarian menuHarian) {
        String sql = "UPDATE menu_harian SET stok_menu_harian = ? WHERE id_menu = ? AND id_cabang = ? AND tanggal_menu_harian = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, menuHarian.getStokMenuHarian());
            pstmt.setInt(2, menuHarian.getIdMenu());
            pstmt.setInt(3, menuHarian.getIdCabang());
            pstmt.setDate(4, java.sql.Date.valueOf(menuHarian.getTanggalMenuHarian()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMenuHarian(int idMenuHarian) {
        String sql = "DELETE FROM menu_harian WHERE id_menu_harian = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMenuHarian);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                menuList.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuList;
    }

    public List<MenuHarian> getMenuHarianByKategori(int idKategori, int idCabang) {
        List<MenuHarian> menuHarianList = new ArrayList<>();
        String sql = "SELECT mh.*, m.nama_menu, m.harga_menu FROM menu_harian mh " +
                "JOIN menu m ON mh.id_menu = m.id_menu " +
                "WHERE m.id_kategori = ? AND mh.id_cabang = ? AND mh.tanggal_menu_harian = CURRENT_DATE AND mh.stok_menu_harian > 0";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idKategori);
            pstmt.setInt(2, idCabang);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MenuHarian mh = new MenuHarian();
                mh.setIdMenuHarian(rs.getInt("id_menu_harian"));
                mh.setIdMenu(rs.getInt("id_menu"));
                mh.setIdCabang(rs.getInt("id_cabang"));
                mh.setTanggalMenuHarian(rs.getDate("tanggal_menu_harian").toLocalDate());
                mh.setStokMenuHarian(rs.getInt("stok_menu_harian"));
                mh.setNamaMenu(rs.getString("nama_menu"));
                mh.setHargaMenu(rs.getDouble("harga_menu"));
                menuHarianList.add(mh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuHarianList;
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
                return menu;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public MenuHarian findMenuHarianByMenuId(int idMenu, int idCabang) {
        String sql = "SELECT * FROM menu_harian " +
                "WHERE id_menu = ? AND id_cabang = ? AND tanggal_menu_harian = CURRENT_DATE";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMenu);
            pstmt.setInt(2, idCabang);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                MenuHarian mh = new MenuHarian();
                mh.setIdMenuHarian(rs.getInt("id_menu_harian"));
                mh.setIdMenu(rs.getInt("id_menu"));
                mh.setIdCabang(rs.getInt("id_cabang"));
                mh.setTanggalMenuHarian(rs.getDate("tanggal_menu_harian").toLocalDate());
                mh.setStokMenuHarian(rs.getInt("stok_menu_harian"));
                return mh;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}