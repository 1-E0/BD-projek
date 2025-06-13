package com.example.bd.dao;

import com.example.bd.model.DetailPesanan;
import com.example.bd.model.Pembayaran;
import com.example.bd.model.Pesanan;
import com.example.bd.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PesananDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public List<Pesanan> getPesananByPelanggan(int idPelanggan) {
        List<Pesanan> pesananList = new ArrayList<>();
        String sql = "SELECT p.*, png.status_pengiriman FROM pesanan p " +
                "LEFT JOIN pengiriman png ON p.id_pesanan = png.id_pesanan " +
                "WHERE p.id_pelanggan = ? ORDER BY p.tanggal_pesanan DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Pesanan p = new Pesanan();
                p.setIdPesanan(rs.getInt("id_pesanan"));
                p.setIdPelanggan(rs.getInt("id_pelanggan"));
                p.setTanggalPesanan(rs.getDate("tanggal_pesanan"));
                p.setTotalHargaPesanan(rs.getDouble("total_harga_pesanan"));
                p.setStatusPembayaran(rs.getString("status_pembayaran"));
                p.setAlamatTujuan(rs.getString("alamat_tujuan"));
                p.setStatusPengiriman(rs.getString("status_pengiriman"));
                pesananList.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pesananList;
    }

    public List<DetailPesanan> getDetailByPesanan(int idPesanan) {
        List<DetailPesanan> detailList = new ArrayList<>();
        String sql = "SELECT dp.*, m.nama_menu, mh.id_menu FROM detail_pesanan dp " +
                "JOIN menu_harian mh ON dp.id_menu_harian = mh.id_menu_harian " +
                "JOIN menu m ON mh.id_menu = m.id_menu " +
                "WHERE dp.id_pesanan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPesanan);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                DetailPesanan d = new DetailPesanan();
                d.setIdDetailPesanan(rs.getInt("id_detail_pesanan"));
                d.setIdPesanan(rs.getInt("id_pesanan"));
                d.setIdMenuHarian(rs.getInt("id_menu_harian"));
                d.setJumlah(rs.getInt("jumlah"));
                d.setHargaProduk(rs.getDouble("harga_produk"));
                d.setCatatan(rs.getString("catatan"));
                d.setNamaMenu(rs.getString("nama_menu"));
                d.setIdMenu(rs.getInt("id_menu"));
                detailList.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detailList;
    }

    public List<Pesanan> getAllPesanan() {
        List<Pesanan> pesananList = new ArrayList<>();
        String sql = "SELECT p.*, pl.nama_pelanggan, png.status_pengiriman " +
                "FROM pesanan p " +
                "JOIN pelanggan pl ON p.id_pelanggan = pl.id_pelanggan " +
                "LEFT JOIN pengiriman png ON p.id_pesanan = png.id_pesanan " +
                "ORDER BY p.tanggal_pesanan DESC";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pesanan p = new Pesanan();
                p.setIdPesanan(rs.getInt("id_pesanan"));
                p.setIdPelanggan(rs.getInt("id_pelanggan"));
                p.setTanggalPesanan(rs.getDate("tanggal_pesanan"));
                p.setTotalHargaPesanan(rs.getDouble("total_harga_pesanan"));
                p.setStatusPembayaran(rs.getString("status_pembayaran"));
                p.setAlamatTujuan(rs.getString("alamat_tujuan"));
                p.setNamaPelanggan(rs.getString("nama_pelanggan"));
                p.setStatusPengiriman(rs.getString("status_pengiriman"));
                pesananList.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pesananList;
    }

    public void updateStatusPembayaran(int idPesanan, String status) {
        String sql = "UPDATE pesanan SET status_pembayaran = ? WHERE id_pesanan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, idPesanan);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void simpanPesananLengkap(Pesanan pesanan, List<DetailPesanan> detailList, Pembayaran pembayaran) throws SQLException {
        String sqlPesanan = "INSERT INTO pesanan (id_pelanggan, tanggal_pesanan, total_harga_pesanan, status_pembayaran, alamat_tujuan) VALUES (?, CURRENT_DATE, ?, ?, ?) RETURNING id_pesanan";
        String sqlDetail = "INSERT INTO detail_pesanan (id_pesanan, id_menu_harian, jumlah, harga_produk) VALUES (?, ?, ?, ?)";
        String sqlPembayaran = "INSERT INTO pembayaran (id_pesanan, id_metode, total_pembayaran, tanggal_pembayaran) VALUES (?, ?, ?, CURRENT_DATE)";
        String sqlUpdateStok = "UPDATE menu_harian SET stok_menu_harian = stok_menu_harian - ? WHERE id_menu_harian = ?";

        try {
            conn.setAutoCommit(false);

            int idPesananBaru;
            try (PreparedStatement pstmtPesanan = conn.prepareStatement(sqlPesanan)) {
                pstmtPesanan.setInt(1, pesanan.getIdPelanggan());
                pstmtPesanan.setDouble(2, pesanan.getTotalHargaPesanan());
                pstmtPesanan.setString(3, pesanan.getStatusPembayaran());
                pstmtPesanan.setString(4, pesanan.getAlamatTujuan());
                ResultSet rs = pstmtPesanan.executeQuery();
                if (rs.next()) {
                    idPesananBaru = rs.getInt(1);
                } else {
                    throw new SQLException("Gagal membuat pesanan.");
                }
            }

            try (PreparedStatement pstmtDetail = conn.prepareStatement(sqlDetail);
                 PreparedStatement pstmtStok = conn.prepareStatement(sqlUpdateStok)) {
                for (DetailPesanan detail : detailList) {
                    // Adicionar ao lote de detalhes do pedido
                    pstmtDetail.setInt(1, idPesananBaru);
                    pstmtDetail.setInt(2, detail.getIdMenuHarian());
                    pstmtDetail.setInt(3, detail.getJumlah());
                    pstmtDetail.setDouble(4, detail.getHargaProduk());
                    pstmtDetail.addBatch();

                    // Adicionar ao lote de atualização de estoque
                    pstmtStok.setInt(1, detail.getJumlah());
                    pstmtStok.setInt(2, detail.getIdMenuHarian());
                    pstmtStok.addBatch();
                }
                pstmtDetail.executeBatch();
                pstmtStok.executeBatch();
            }

            try (PreparedStatement pstmtPembayaran = conn.prepareStatement(sqlPembayaran)) {
                pstmtPembayaran.setInt(1, idPesananBaru);
                pstmtPembayaran.setInt(2, pembayaran.getIdMetode());
                pstmtPembayaran.setDouble(3, pesanan.getTotalHargaPesanan()); // O total do pagamento é o total do pedido
                pstmtPembayaran.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}