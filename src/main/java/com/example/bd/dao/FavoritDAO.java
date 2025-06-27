package com.example.bd.dao;

import com.example.bd.model.Menu;
import com.example.bd.model.MenuHarian;
import com.example.bd.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    /**
     * Mengambil daftar ID menu yang difavoritkan oleh seorang pelanggan.
     * Menggunakan Set untuk performa pengecekan yang lebih cepat.
     * @param idPelanggan ID pelanggan
     * @return Set berisi ID menu favorit.
     */
    public Set<Integer> getFavoritIdsByPelanggan(int idPelanggan) {
        Set<Integer> favoritIds = new HashSet<>();
        String sql = "SELECT id_menu FROM favorit_pelanggan WHERE id_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                favoritIds.add(rs.getInt("id_menu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoritIds;
    }

    /**
     * Mengambil daftar objek MenuHarian yang tersedia hari ini
     * dari menu yang difavoritkan pelanggan.
     * @param idPelanggan ID pelanggan
     * @param idCabang ID cabang saat ini
     * @return List dari objek MenuHarian.
     */
    public List<MenuHarian> getFavoritMenuHarianByPelanggan(int idPelanggan, int idCabang) {
        List<MenuHarian> favoritList = new ArrayList<>();
        // Query ini menggabungkan menu favorit dengan menu harian yang tersedia hari ini
        String sql = "SELECT mh.*, m.nama_menu, m.harga_menu FROM favorit_pelanggan fp " +
                "JOIN menu m ON fp.id_menu = m.id_menu " +
                "JOIN menu_harian mh ON m.id_menu = mh.id_menu " +
                "WHERE fp.id_pelanggan = ? " +
                "AND mh.id_cabang = ? " +
                "AND mh.tanggal_menu_harian = CURRENT_DATE " +
                "AND mh.stok_menu_harian > 0";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
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
                favoritList.add(mh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoritList;
    }

    /**
     * Menambahkan item ke daftar favorit.
     * ON CONFLICT DO NOTHING memastikan tidak ada error jika data sudah ada.
     * @param idPelanggan ID pelanggan
     * @param idMenu ID menu
     */
    public void addFavorit(int idPelanggan, int idMenu) {
        String sql = "INSERT INTO favorit_pelanggan (id_pelanggan, id_menu) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
            pstmt.setInt(2, idMenu);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Menghapus item dari daftar favorit.
     * @param idPelanggan ID pelanggan
     * @param idMenu ID menu
     */
    public void removeFavorit(int idPelanggan, int idMenu) {
        String sql = "DELETE FROM favorit_pelanggan WHERE id_pelanggan = ? AND id_menu = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
            pstmt.setInt(2, idMenu);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}