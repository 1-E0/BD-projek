package com.example.bd.dao;

import com.example.bd.model.LaporanPerforma;
import com.example.bd.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LaporanDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public List<LaporanPerforma> getLaporanPerformaByYear(int tahun) {
        List<LaporanPerforma> laporanList = new ArrayList<>();
        // Query ini akan mengagregasi data pesanan per bulan dan per cabang
        String sql = "SELECT " +
                "    EXTRACT(MONTH FROM p.tanggal_pesanan) AS bulan, " +
                "    EXTRACT(YEAR FROM p.tanggal_pesanan) AS tahun, " +
                "    c.alamat_cabang AS nama_cabang, " +
                "    COUNT(DISTINCT p.id_pesanan) AS jumlah_pesanan, " +
                "    SUM(p.total_harga_pesanan) AS total_pendapatan " +
                "FROM pesanan p " +
                "JOIN detail_pesanan dp ON p.id_pesanan = dp.id_pesanan " +
                "JOIN menu_harian mh ON dp.id_menu_harian = mh.id_menu_harian " +
                "JOIN cabang c ON mh.id_cabang = c.id_cabang " +
                "WHERE EXTRACT(YEAR FROM p.tanggal_pesanan) = ? " +
                "GROUP BY bulan, tahun, nama_cabang " +
                "ORDER BY bulan ASC, total_pendapatan DESC";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tahun);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                LaporanPerforma laporan = new LaporanPerforma();
                int monthNumber = rs.getInt("bulan");

                // Konversi nomor bulan (1-12) ke nama bulan dalam Bahasa Indonesia
                DateFormatSymbols dfs = new DateFormatSymbols(new Locale("id", "ID"));
                laporan.setNamaBulan(dfs.getMonths()[monthNumber-1]);

                laporan.setTahun(rs.getInt("tahun"));
                laporan.setNamaCabang(rs.getString("nama_cabang"));
                laporan.setJumlahPesanan(rs.getInt("jumlah_pesanan"));
                laporan.setTotalPendapatan(rs.getDouble("total_pendapatan"));
                laporanList.add(laporan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return laporanList;
    }
}
