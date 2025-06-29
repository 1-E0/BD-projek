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
     @param idPelanggan
     @return
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
     *
     *
     * @param idPelanggan
     * @param idCabang
     * @return
     */
    public List<MenuHarian> getFavoritMenuHarianByPelanggan(int idPelanggan, int idCabang) {
        List<MenuHarian> favoritList = new ArrayList<>();

        String sql = "SELECT m.id_menu, m.nama_menu, m.harga_menu, mh.id_menu_harian, mh.stok_menu_harian " +
                "FROM favorit_pelanggan fp " +
                "JOIN menu m ON fp.id_menu = m.id_menu " +
                "LEFT JOIN menu_harian mh ON m.id_menu = mh.id_menu AND mh.id_cabang = ? AND mh.tanggal_menu_harian = CURRENT_DATE " +
                "WHERE fp.id_pelanggan = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCabang);
            pstmt.setInt(2, idPelanggan);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MenuHarian mh = new MenuHarian();
                mh.setIdMenu(rs.getInt("id_menu"));
                mh.setNamaMenu(rs.getString("nama_menu"));
                mh.setHargaMenu(rs.getDouble("harga_menu"));

                mh.setIdMenuHarian(rs.getInt("id_menu_harian"));
                mh.setStokMenuHarian(rs.getInt("stok_menu_harian"));
                mh.setIdCabang(idCabang);
                favoritList.add(mh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoritList;
    }

    /**
     *
     *
     * @param idPelanggan
     * @param idMenu
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
     *
     * @param idPelanggan
     * @param idMenu
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