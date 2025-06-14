package com.example.bd.dao;

import com.example.bd.model.Admin;
import com.example.bd.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {
    private final Connection conn = DatabaseConnection.getConnection();

    public Admin validateLogin(String email, String password) {
        String sql = "SELECT * FROM admin WHERE email_admin = ? AND password_admin = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password); // Em uma aplicação real, compare um hash de senha
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
}