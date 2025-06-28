package com.example.bd.dao;

import com.example.bd.model.Voucher;
import com.example.bd.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoucherDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public List<Voucher> getAllVouchers() {
        List<Voucher> voucherList = new ArrayList<>();
        String sql = "SELECT * FROM voucher ORDER BY id_voucher ASC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Voucher v = new Voucher();
                v.setIdVoucher(rs.getInt("id_voucher"));
                v.setNamaVoucher(rs.getString("nama_voucher"));
                v.setDeskripsi(rs.getString("deskripsi"));
                v.setPotonganHarga(rs.getDouble("potongan_harga"));
                v.setPoinDibutuhkan(rs.getInt("poin_dibutuhkan"));
                v.setAktif(rs.getBoolean("aktif"));
                voucherList.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voucherList;
    }

    public List<Voucher> getAllVoucherAktif() {
        List<Voucher> voucherList = new ArrayList<>();
        String sql = "SELECT * FROM voucher WHERE aktif = TRUE ORDER BY poin_dibutuhkan ASC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Voucher v = new Voucher();
                v.setIdVoucher(rs.getInt("id_voucher"));
                v.setNamaVoucher(rs.getString("nama_voucher"));
                v.setDeskripsi(rs.getString("deskripsi"));
                v.setPotonganHarga(rs.getDouble("potongan_harga"));
                v.setPoinDibutuhkan(rs.getInt("poin_dibutuhkan"));
                v.setAktif(rs.getBoolean("aktif"));
                voucherList.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return voucherList;
    }

    public void addVoucher(Voucher voucher) {
        String sql = "INSERT INTO voucher (nama_voucher, deskripsi, potongan_harga, poin_dibutuhkan, aktif) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, voucher.getNamaVoucher());
            pstmt.setString(2, voucher.getDeskripsi());
            pstmt.setDouble(3, voucher.getPotonganHarga());
            pstmt.setInt(4, voucher.getPoinDibutuhkan());
            pstmt.setBoolean(5, voucher.isAktif());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVoucher(Voucher voucher) {
        String sql = "UPDATE voucher SET nama_voucher = ?, deskripsi = ?, potongan_harga = ?, poin_dibutuhkan = ?, aktif = ? WHERE id_voucher = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, voucher.getNamaVoucher());
            pstmt.setString(2, voucher.getDeskripsi());
            pstmt.setDouble(3, voucher.getPotonganHarga());
            pstmt.setInt(4, voucher.getPoinDibutuhkan());
            pstmt.setBoolean(5, voucher.isAktif());
            pstmt.setInt(6, voucher.getIdVoucher());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}