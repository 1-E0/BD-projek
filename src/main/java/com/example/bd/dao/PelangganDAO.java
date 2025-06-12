package com.example.bd.dao;

import com.example.bd.model.Pelanggan;
import com.example.bd.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PelangganDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public List<Pelanggan> getAllPelanggan() {
        List<Pelanggan> pelangganList = new ArrayList<>();
        String sql = "SELECT * FROM pelanggan ORDER BY id_pelanggan ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pelanggan p = new Pelanggan();
                p.setIdPelanggan(rs.getInt("id_pelanggan"));
                p.setNamaPelanggan(rs.getString("nama_pelanggan"));
                p.setEmailPelanggan(rs.getString("email_pelanggan"));
                p.setAlamatPelanggan(rs.getString("alamat_pelanggan"));
                p.setNoTelpPelanggan(rs.getString("no_telp_pelanggan"));
                pelangganList.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pelangganList;
    }
    public void updateProfil(Pelanggan pelanggan) {
        String sql = "UPDATE pelanggan SET nama_pelanggan = ?, email_pelanggan = ?, alamat_pelanggan = ?, no_telp_pelanggan = ? WHERE id_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelanggan.getNamaPelanggan());
            pstmt.setString(2, pelanggan.getEmailPelanggan());
            pstmt.setString(3, pelanggan.getAlamatPelanggan());
            pstmt.setString(4, pelanggan.getNoTelpPelanggan());
            pstmt.setInt(5, pelanggan.getIdPelanggan());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(int idPelanggan, String passwordBaru) {
        String sql = "UPDATE pelanggan SET password_pelanggan = ? WHERE id_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Di aplikasi nyata, passwordBaru harusnya sudah dalam bentuk hash
            pstmt.setString(1, passwordBaru);
            pstmt.setInt(2, idPelanggan);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPelanggan(Pelanggan pelanggan) {
        String sql = "INSERT INTO pelanggan (nama_pelanggan, email_pelanggan, password_pelanggan, alamat_pelanggan, no_telp_pelanggan) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelanggan.getNamaPelanggan());
            pstmt.setString(2, pelanggan.getEmailPelanggan());
            pstmt.setString(3, pelanggan.getPasswordPelanggan());
            pstmt.setString(4, pelanggan.getAlamatPelanggan());
            pstmt.setString(5, pelanggan.getNoTelpPelanggan());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePelanggan(Pelanggan pelanggan) {
        String sql = "UPDATE pelanggan SET nama_pelanggan = ?, email_pelanggan = ?, alamat_pelanggan = ?, no_telp_pelanggan = ? WHERE id_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelanggan.getNamaPelanggan());
            pstmt.setString(2, pelanggan.getEmailPelanggan());
            pstmt.setString(3, pelanggan.getAlamatPelanggan());
            pstmt.setString(4, pelanggan.getNoTelpPelanggan());
            pstmt.setInt(5, pelanggan.getIdPelanggan());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deletePelanggan(int idPelanggan) {
        String sql = "DELETE FROM pelanggan WHERE id_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPelanggan);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Pelanggan validateLogin(String email, String password) {
        String sql = "SELECT * FROM pelanggan WHERE email_pelanggan = ? AND password_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Pelanggan p = new Pelanggan();
                p.setIdPelanggan(rs.getInt("id_pelanggan"));
                p.setNamaPelanggan(rs.getString("nama_pelanggan"));
                p.setEmailPelanggan(rs.getString("email_pelanggan"));
                p.setAlamatPelanggan(rs.getString("alamat_pelanggan"));
                p.setNoTelpPelanggan(rs.getString("no_telp_pelanggan"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isEmailRegistered(String email) {
        String sql = "SELECT COUNT(*) FROM pelanggan WHERE email_pelanggan = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Jika count > 0, berarti email sudah ada
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Anggap belum terdaftar jika ada error
    }
}