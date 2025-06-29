package com.example.bd.dao;

import com.example.bd.model.Admin;
import com.example.bd.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public Admin validateLogin(String email, String password) {
        String sql = "SELECT * FROM admin WHERE email_admin = ? AND password_admin = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setIdAdmin(rs.getInt("id_admin"));
                admin.setIdCabang(rs.getInt("id_cabang"));
                admin.setNamaAdmin(rs.getString("nama_admin"));
                admin.setEmailAdmin(rs.getString("email_admin"));
                admin.setJenisAdmin(rs.getString("jenis_admin"));
                admin.setNoTelpAdmin(rs.getString("no_telp_admin"));
                admin.setStatusAdmin(rs.getString("status_admin"));
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public List<Admin> getAllAdmins() {
        List<Admin> adminList = new ArrayList<>();
        String sql = "SELECT * FROM admin ORDER BY id_admin ASC";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Admin admin = new Admin();
                admin.setIdAdmin(rs.getInt("id_admin"));
                admin.setIdCabang(rs.getInt("id_cabang"));
                admin.setNamaAdmin(rs.getString("nama_admin"));
                admin.setEmailAdmin(rs.getString("email_admin"));
                admin.setJenisAdmin(rs.getString("jenis_admin"));
                admin.setNoTelpAdmin(rs.getString("no_telp_admin"));
                admin.setStatusAdmin(rs.getString("status_admin"));
                adminList.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminList;
    }

    public void addAdmin(Admin admin) throws SQLException {
        String sql = "INSERT INTO admin (id_cabang, jenis_admin, nama_admin, email_admin, password_admin, no_telp_admin, status_admin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (admin.getIdCabang() == 0) {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(1, admin.getIdCabang());
            }
            pstmt.setString(2, admin.getJenisAdmin());
            pstmt.setString(3, admin.getNamaAdmin());
            pstmt.setString(4, admin.getEmailAdmin());
            pstmt.setString(5, admin.getPasswordAdmin());
            pstmt.setString(6, admin.getNoTelpAdmin());
            pstmt.setString(7, admin.getStatusAdmin());
            pstmt.executeUpdate();
        }
    }

    public void updateAdmin(Admin admin) throws SQLException {
        String sql = "UPDATE admin SET id_cabang = ?, jenis_admin = ?, nama_admin = ?, email_admin = ?, no_telp_admin = ?, status_admin = ? WHERE id_admin = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (admin.getIdCabang() == 0) {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(1, admin.getIdCabang());
            }
            pstmt.setString(2, admin.getJenisAdmin());
            pstmt.setString(3, admin.getNamaAdmin());
            pstmt.setString(4, admin.getEmailAdmin());
            pstmt.setString(5, admin.getNoTelpAdmin());
            pstmt.setString(6, admin.getStatusAdmin());
            pstmt.setInt(7, admin.getIdAdmin());
            pstmt.executeUpdate();
        }
    }


    public void updatePasswordAdmin(int idAdmin, String newPassword) throws SQLException {
        String sql = "UPDATE admin SET password_admin = ? WHERE id_admin = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, idAdmin);
            pstmt.executeUpdate();
        }
    }

    public void deleteAdmin(int idAdmin) throws SQLException {
        String sql = "DELETE FROM admin WHERE id_admin = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAdmin);
            pstmt.executeUpdate();
        }
    }
}